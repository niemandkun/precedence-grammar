package tech.niemandkun.app

import tech.niemandkun.grammarlib.Grammar
import tech.niemandkun.grammarlib.Rule

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

    val rules = input.takeWhile { !it.isEmpty() }.map { Rule.parse(it) }
    val grammar = Grammar(rules)

    println(grammar)
}