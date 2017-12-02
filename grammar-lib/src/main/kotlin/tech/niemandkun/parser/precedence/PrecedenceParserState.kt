package tech.niemandkun.parser.precedence

import tech.niemandkun.grammarlib.GrammarRule
import tech.niemandkun.grammarlib.Symbol
import tech.niemandkun.parser.ParserState
import tech.niemandkun.utils.second
import tech.niemandkun.utils.suffixes

class PrecedenceParserState(
        private val table: PrecedenceTable,
        private val axiom: Symbol,
        val precedence: List<Set<Precedence>>,
        val stack: List<Symbol>,
        val input: String
    ) : ParserState(stack, input) {

    private val terminatingStack = terminatingStack(axiom)

    val isReducible get() = isProductEnd(peekPrecedence()) && !isPreFinal

    val isPreFinal get() = stack == terminatingStack.subList(0, 2) && input == Symbol.END_OF_LINE.toString()

    override val isTerminating get() = stack == terminatingStack && input.isEmpty()

    private fun peekPrecedence() = table[peekStack(), peekInput()]

    fun getProduction(rules: List<GrammarRule>): List<Symbol> {
        return if (isProductStart(peekPrecedence())) {
            emptyList()
        } else {
            stack.suffixes()
                    .filter { it.size > 1 }
                    .filter { isProductStart(table[it.first(), it.second()]) }
                    .map { it.drop(1) }
                    .filter { suffix -> rules.any { suffix == it.production } }
                    .sortedByDescending { it.size }
                    .first()
        }
    }

    override fun shift(): PrecedenceParserState {
        if (!hasMoreInput() || peekPrecedence().isEmpty()) {
            throw IllegalStateException()
        }

        return PrecedenceParserState(
                table,
                axiom,
                precedence + listOf(peekPrecedence()),
                stack + peekInput(),
                tailInput()
        )
    }

    override fun reduce(rule: GrammarRule): PrecedenceParserState {
        if (stack.size < rule.production.size) {
            throw IllegalStateException()
        }

        val newStack = stack.dropLast(rule.production.size)

        val newPrecedence = table[newStack.last(), rule.nonterminal]

        if (newPrecedence.isEmpty()) {
            throw IllegalStateException()
        }

        return PrecedenceParserState(
                table,
                axiom,
                precedence.dropLast(rule.production.size) + listOf(newPrecedence),
                newStack + rule.nonterminal,
                input
        )
    }

    override fun toString() = listOf(stack.joinToString(separator = ""), input)
            .filter { !it.isEmpty() }
            .joinToString(separator = " ")

    companion object {
        fun init(table: PrecedenceTable, axiom: Symbol, input: String) =
                PrecedenceParserState(table, axiom, emptyList(), listOf(Symbol.START_OF_LINE), input + Symbol.END_OF_LINE.symbol)

        private fun terminatingStack(axiom: Symbol) = listOf(Symbol.START_OF_LINE, axiom, Symbol.END_OF_LINE)

        private fun isProductStart(precedence: Set<Precedence>) = Precedence.LESS in precedence

        private fun isProductEnd(precedence: Set<Precedence>) = Precedence.GREATER in precedence
    }
}