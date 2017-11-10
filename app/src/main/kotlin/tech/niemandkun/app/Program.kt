package tech.niemandkun.app

import tech.niemandkun.grammarlib.Grammar
import tech.niemandkun.grammarlib.GrammarRule
import tech.niemandkun.parser.Parser
import tech.niemandkun.parser.ParserState
import tech.niemandkun.parser.precedence.PrecedenceStateMachine

fun main(args: Array<String>) {
    val input = listOf(
            "S aASb",
            "S d",
            "A Ac",
            "A c",
            "",
            "acdb",
            "acdc"
    )

    val rules = input.takeWhile { !it.isEmpty() }.map { GrammarRule.parse(it) }
    val grammar = Grammar(rules)
    val stateMachine = PrecedenceStateMachine(grammar)

    println(stateMachine.table)

    when {
        stateMachine.table.isSimple() -> println("S\n")
        stateMachine.table.isWeak() -> println("W\n")
        else -> { println("N\n"); return }
    }

    val parser = Parser(PrecedenceStateMachine(grammar))

    val sequences = input.dropWhile { !it.isEmpty() }.drop(1)

    sequences.forEach { parser.parse(it, object : Parser.Callback {
        override fun onStep(state: ParserState) = println(state)

        override fun onError(error: Throwable) = println("error")

        override fun onEnd() = println()
    })}
}