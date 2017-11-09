package tech.niemandkun.parser

import tech.niemandkun.grammarlib.GrammarRule
import tech.niemandkun.grammarlib.Symbol
import tech.niemandkun.grammarlib.Terminal

abstract class ParserState(private val stack: List<Symbol>, private val input: String) {
    fun hasMoreInput() = !input.isEmpty()

    fun peekInput() = Terminal(input[0])

    fun peekStack() = stack.last()

    fun tailInput() = input.substring(1)

    abstract fun shift(): ParserState

    abstract fun reduce(rule: GrammarRule): ParserState

    abstract val isTerminating: Boolean
}