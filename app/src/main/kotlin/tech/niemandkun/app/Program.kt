package tech.niemandkun.app

import tech.niemandkun.grammarlib.Grammar
import tech.niemandkun.grammarlib.GrammarRule
import tech.niemandkun.parser.Parser
import tech.niemandkun.parser.ParserState
import tech.niemandkun.parser.precedence.PrecedenceStateMachine
import java.io.File

fun main(args: Array<String>) {
    Thread.setDefaultUncaughtExceptionHandler({ _, throwable -> println("Error: " + throwable.message) })

    if ("-h" in args || "--help" in args) {
        printHelp()
        return
    }

    if (args.size != 2) {
        printUsage()
        return
    }

    File(args[0]).useLines { input ->
        File(args[1]).printWriter().use { output ->
            solve(input.toList(), { s -> output.println(s) })
        }
    }
}

private fun solve(input: List<String>, output: (Any) -> Unit) {
    val rules = input.takeWhile { !it.isEmpty() }.map { GrammarRule.parse(it) }
    val grammar = Grammar(rules)
    val stateMachine = PrecedenceStateMachine(grammar)

    output(stateMachine.table)

    when {
        stateMachine.table.isSimple() -> output("S")
        stateMachine.table.isWeak() -> output("W")
        else -> { output("N"); return }
    }

    val parser = Parser(PrecedenceStateMachine(grammar))
    val sequences = input.dropWhile { !it.isEmpty() }.drop(1)

    sequences.forEach { output(""); parser.parse(it, object : Parser.Callback {
        override fun onStep(state: ParserState) = output(state)
        override fun onError(error: Throwable) = output("error")
    })}
}

private  fun printHelp() {
    printUsage()
    println()
    println("Parse sequences derived by simple or weak precedence grammar.")
    println()
    println("Args:")
    println("    -h, --help - show this help message and exit")
    println("    INPUT - input file")
    println("    OUTPUT - output file")
}

private fun printUsage() {
    println("Usage: parser.exe [-h] INPUT OUTPUT")
}