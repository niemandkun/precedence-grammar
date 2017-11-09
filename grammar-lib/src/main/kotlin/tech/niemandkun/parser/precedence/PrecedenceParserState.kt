package tech.niemandkun.parser.precedence

import tech.niemandkun.grammarlib.GrammarRule
import tech.niemandkun.grammarlib.Symbol
import tech.niemandkun.parser.ParserState
import tech.niemandkun.utils.bigrams
import tech.niemandkun.utils.takeUntil

class PrecedenceParserState(
        private val table: PrecedenceTable,
        private val axiom: Symbol,
        val precedence: List<Set<Precedence>>,
        val stack: List<Symbol>,
        val input: String
    ) : ParserState(stack, input) {

    private val terminatingStack = terminatingStack(axiom)

    val isReducible get() = isProductEnd(peekPrecedence())

    override val isTerminating get() = stack == terminatingStack && input.isEmpty()

    private fun peekPrecedence() = table[peekStack(), peekInput()]

    fun getProduction(): List<Symbol> {
        return if (isProductStart(peekPrecedence())) {
            emptyList()
        } else {
            println()
            stack.reversed()
                    .bigrams()
                    .takeUntil { !isProductStart(table[it.second, it.first]) }
                    .map { it.first }
                    .reversed()
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

    companion object {
        fun init(table: PrecedenceTable, axiom: Symbol, input: String) =
                PrecedenceParserState(table, axiom, emptyList(), listOf(Symbol.START_OF_LINE), input + Symbol.END_OF_LINE.symbol)

        private fun terminatingStack(axiom: Symbol) =
                listOf(Symbol.START_OF_LINE, axiom, Symbol.END_OF_LINE)

        private fun isProductStart(precedence: Set<Precedence>) =
                Precedence.LESS in precedence && Precedence.EQUAL !in precedence

        private fun isProductEnd(precedence: Set<Precedence>) =
                Precedence.GREATER in precedence
    }
}