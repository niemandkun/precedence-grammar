package tech.niemandkun.grammarlib

class Grammar(val rules: List<Rule>) {
    val terminals: List<Terminal> = rules
                .flatMap { it.production }
                .filter { it is Terminal }
                .map { it as Terminal }
                .distinct()

    val nonterminals: List<Nonterminal> = rules
                .map { it.nonterminal }
                .distinct()

    val isUniquelyInversible = rules
            .map { rule -> Pair(rule.production, rules.filter { it != rule }.map { it.production })}
            .all { pair -> pair.second.all { it != pair.first } }

    val hasErasingRules = rules
            .any { rule -> rule.isErasing }

    val axiom = nonterminals[0]

    val first = findFirstSets({ it })

    val last = findFirstSets({ it.reversed() })

    private fun findFirstSets(direction: (List<Symbol>) -> List<Symbol>): Map<Symbol, Set<Symbol>> {
        val first = mutableMapOf<Symbol, MutableSet<Symbol>>()

        terminals.forEach { first[it] = mutableSetOf(it as Symbol) }
        val hasChanges = nonterminals.associateBy({ it }, { true }).toMutableMap()

        while (hasChanges.values.any { it }) {
            hasChanges.clear()

            rules.forEach { rule ->
                val firstOfNonterminal = first[rule.nonterminal] ?: mutableSetOf()
                first[rule.nonterminal] = firstOfNonterminal

                for (symbol in direction(rule.production)) {
                    val firstOfSymbol = first[symbol] ?: mutableSetOf()
                    first[symbol] = firstOfSymbol

                    hasChanges[rule.nonterminal] = hasChanges[symbol] ?: false
                            || firstOfNonterminal.addAll(firstOfSymbol)
                            || firstOfNonterminal.add(symbol)

                    if (!firstOfSymbol.contains(Symbol.LAMBDA)) {
                        break;
                    }
                }
            }
        }

        return first;
    }

    override fun toString(): String = rules.joinToString(separator = "\n")
}
