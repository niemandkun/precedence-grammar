package tech.niemandkun.grammarlib

open class Symbol(open val symbol: Char) {
    companion object {
        val LAMBDA = Terminal('\\')

        fun terminals(symbols: CharSequence): List<Terminal> = symbols.map { Terminal(it) }

        fun nonterminals(symbols: CharSequence): List<Nonterminal> = symbols.map { Nonterminal(it) }

        fun symbols(symbols: CharSequence): List<Symbol> = symbols.map { parse(it) }

        fun parse(char: Char): Symbol = if (char.isUpperCase()) Nonterminal(char) else Terminal(char)
    }
}