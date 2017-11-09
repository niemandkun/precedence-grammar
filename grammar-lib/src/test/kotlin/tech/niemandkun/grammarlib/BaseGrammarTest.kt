package tech.niemandkun.grammarlib

open class BaseGrammarTest {
    fun grammar(vararg rules: String) = Grammar(rules.map { GrammarRule.parse(it) })

    companion object {
        val S = Nonterminal('S')
        val A = Nonterminal('A')
        val b = Terminal('b')
        val c = Terminal('c')
        val d = Terminal('d')
        val e = Terminal('e')
    }
}