package tech.niemandkun.grammarlib

class Grammar(val rules: List<GrammarRule>) {
    val terminals = rules
                .flatMap { it.production }
                .filter { it is Terminal }
                .map { it as Terminal }
                .distinct()

    val nonterminals = rules
                .map { it.nonterminal }
                .distinct()

    val isUniquelyInversible = rules
            .map { rule -> rule.production to rules.filter { it != rule }.map { it.production }}
            .all { pair -> pair.second.all { it != pair.first } }

    val hasErasingRules = rules
            .any { rule -> rule.isErasing }

    val axiom = nonterminals[0]

    val symbols get() = nonterminals + terminals.sortedBy { it }

    private val first = findFirstSets({ it })

    private val last = findFirstSets({ it.reversed() })

    private fun findFirstSets(direction: (List<Symbol>) -> List<Symbol>): Map<Symbol, Set<Symbol>> {
        val first = mutableMapOf<Symbol, MutableSet<Symbol>>()

        terminals.forEach { first[it] = mutableSetOf() }
        val hasChanges = nonterminals.associate { it to true }.toMutableMap()

        while (hasChanges.values.any { it }) {
            hasChanges.clear()

            rules.forEach { rule ->
                val currentFirst = first[rule.nonterminal] ?: mutableSetOf()
                first[rule.nonterminal] = currentFirst

                for (symbol in direction(rule.production)) {
                    val firstOfSymbol = first[symbol] ?: mutableSetOf()
                    first[symbol] = firstOfSymbol

                    val firstAdded = currentFirst.addAll(firstOfSymbol)
                    val symbolAdded = currentFirst.add(symbol)

                    hasChanges[rule.nonterminal] = (hasChanges[symbol] ?: false) || firstAdded || symbolAdded

                    if (!firstOfSymbol.contains(Symbol.LAMBDA)) {
                        break
                    }
                }
            }
        }

        return first;
    }

    fun findRuleBy(production: List<Symbol>): GrammarRule {
        return rules.first { it.production == production }
    }

    fun first(symbol: Symbol): Collection<Symbol> {
        return first[symbol] ?: emptySet()
    }

    fun firstOrEqual(symbol: Symbol): Collection<Symbol> {
        return first(symbol) + symbol
    }

    fun last(symbol: Symbol): Collection<Symbol> {
        return last[symbol] ?: emptySet()
    }

    fun lastOrEqual(symbol: Symbol): Collection<Symbol> {
        return last(symbol) + symbol
    }

    override fun toString(): String = rules.joinToString(separator = "\n")
}
