package tech.niemandkun.parser.precedence

import tech.niemandkun.grammarlib.Grammar
import tech.niemandkun.grammarlib.Symbol
import tech.niemandkun.grammarlib.Terminal
import tech.niemandkun.utils.bigrams
import tech.niemandkun.utils.cartesianSquare
import tech.niemandkun.utils.isSuffixOf
import tech.niemandkun.utils.toMultiMap

class PrecedenceTable(private val grammar: Grammar) {
    private val precedence =
            getPrecedenceEqual(grammar).toMultiMap() +
            getPrecedenceLess(grammar) +
            getPrecedenceGreater(grammar)

    operator fun get(first: Symbol, second: Symbol): Set<Precedence> {
        return precedence[first to second] ?: emptySet()
    }

    private fun getNullable(first: Symbol?, second: Symbol?): Set<Precedence> {
        return if (first != null && second != null) {
            get(first, second)
        } else {
            emptySet()
        }
    }

    fun isSimple(): Boolean {
        return grammar.isUniquelyInversible
                && !grammar.hasErasingRules
                && precedence.values.all { it.size < 2 }
    }

    fun isWeak(): Boolean {
        return grammar.isUniquelyInversible
                && !grammar.hasErasingRules
                && precedence.values.all { it.size < 2 || Precedence.GREATER !in it }
                && grammar.rules
                .cartesianSquare()
                .all {
                    !it.first.production.isSuffixOf(it.second.production) ||
                            getNullable(
                                    it.second.production.reversed().drop(it.first.production.size).firstOrNull(),
                                    it.first.nonterminal
                            ).isEmpty()
                }
    }

    override fun toString(): String {
        val columnWidth = mutableMapOf<Symbol, Int>()

        for ((symbols, relation) in precedence) {
            if (columnWidth.getOrDefault(symbols.second, 0) < relation.size) {
                columnWidth[symbols.second] = relation.size
            }
        }

        val sb = StringBuilder(" ")

        (grammar.symbols + Symbol.END_OF_LINE).forEach {
            sb.append(" ").append(it.toString().padEnd(columnWidth.getOrDefault(it, 1)))
        }

        (grammar.symbols + Symbol.START_OF_LINE).forEach {
            sb.appendln()
            sb.append(it.toString())
            for (other in grammar.symbols + Symbol.END_OF_LINE) {
                sb.append(" ").append(getRelationStr(it, other).padEnd(columnWidth.getOrDefault(other, 1)))
            }
        }

        return sb.toString()
    }

    private fun getRelationStr(first: Symbol, second: Symbol): String {
        return if (get(first, second).isEmpty()) "."
        else get(first, second).sortedBy { it.ordinal }.joinToString(separator = "")
    }

    companion object {
        private fun getPrecedenceEqual(grammar: Grammar): Map<Pair<Symbol, Symbol>, Precedence> =
                grammar.rules
                        .flatMap { it.production.bigrams() }
                        .associate { it to Precedence.EQUAL }

        private fun getPrecedenceLess(grammar: Grammar): Map<Pair<Symbol, Symbol>, Precedence> =
                grammar.rules
                        .flatMap { it.production.bigrams() }
                        .flatMap { pair -> grammar.first(pair.second).map { pair.first to it } }
                        .plus(grammar.firstOrEqual(grammar.axiom).map { Symbol.START_OF_LINE to it })
                        .associate { it to Precedence.LESS }

        private fun getPrecedenceGreater(grammar: Grammar): Map<Pair<Symbol, Symbol>, Precedence> =
                grammar.rules
                        .flatMap { it.production.bigrams() }
                        .flatMap { pair -> grammar.last(pair.first).map { it to pair.second } }
                        .flatMap { pair -> grammar.firstOrEqual(pair.second).map { pair.first to it } }
                        .plus(grammar.lastOrEqual(grammar.axiom).map { it to Symbol.END_OF_LINE })
                        .filter { it.second is Terminal }
                        .associate { it to Precedence.GREATER }
    }
}
