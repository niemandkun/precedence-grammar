package tech.niemandkun.grammarlib

class Rule(val nonterminal: Nonterminal, val production: List<Symbol>) {
    val isErasing get() = production.isEmpty()

    override fun toString(): String = "$nonterminal -> ${production.joinToString()}"

    override fun equals(other: Any?): Boolean =
            other is Rule && other.nonterminal == nonterminal && other.production == production

    override fun hashCode(): Int = 31 * nonterminal.hashCode() + production.hashCode()

    companion object {
        fun parse(input: String): Rule {
            val trimmed = input.trim()
            return Rule(Nonterminal(trimmed[0]), parseProduction(trimmed))
        }

        private fun parseProduction(input: String): List<Symbol> = Symbol.symbols(input.substring(1).trim())
    }
}
