package tech.niemandkun.grammarlib

abstract class Symbol(open val symbol: Char) : Comparable<Symbol> {
    override fun compareTo(other: Symbol): Int =
            if (this.symbol.isDigit() && !other.symbol.isDigit()) 1
            else if (!this.symbol.isDigit() && other.symbol.isDigit()) -1
            else this.symbol.compareTo(other.symbol)

    override final fun toString(): String = symbol.toString()

    companion object {
        val LAMBDA = Terminal('\\')
        val END_OF_LINE = Terminal('$')
        val START_OF_LINE = Terminal('^')

        fun terminals(symbols: CharSequence): List<Terminal> = symbols.map { Terminal(it) }

        fun nonterminals(symbols: CharSequence): List<Nonterminal> = symbols.map { Nonterminal(it) }

        fun symbols(symbols: CharSequence): List<Symbol> = symbols.map { parse(it) }

        private fun parse(char: Char): Symbol = if (char.isUpperCase()) Nonterminal(char) else Terminal(char)
    }
}