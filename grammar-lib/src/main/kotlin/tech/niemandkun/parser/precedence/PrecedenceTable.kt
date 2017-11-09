package tech.niemandkun.parser.precedence

import tech.niemandkun.grammarlib.Grammar
import tech.niemandkun.grammarlib.Symbol
import tech.niemandkun.grammarlib.Terminal
import tech.niemandkun.utils.bigrams
import tech.niemandkun.utils.toMultiMap

class PrecedenceTable(grammar: Grammar) {
    private val precedence =
            getPrecedenceEqual(grammar).toMultiMap() +
            getPrecedenceLess(grammar) +
            getPrecedenceGreater(grammar)

    operator fun get(first: Symbol, second: Symbol): Set<Precedence> {
        return precedence[first to second] ?: emptySet()
    }

    override fun toString(): String {
        return precedence.toString()
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
