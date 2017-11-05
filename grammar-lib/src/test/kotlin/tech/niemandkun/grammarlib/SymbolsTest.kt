package tech.niemandkun.grammarlib

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.junit.Test
import tech.niemandkun.grammarlib.Nonterminal
import tech.niemandkun.grammarlib.Terminal

class SymbolsTest {
    @Test
    fun terminals_areEquals_forSameSymbols() {
        val a = Terminal('a')
        val b = Terminal('a')
        a shouldEqual b
    }

    @Test
    fun terminals_areNotEquals_forDifferentSymbols() {
        val a = Terminal('a')
        val b = Terminal('b')
        a shouldNotEqual b
    }

    @Test
    fun nonterminals_areEquals_forSameSymbols() {
        val a = Terminal('A')
        val b = Terminal('A')
        a shouldEqual b
    }

    @Test
    fun nonterminals_areNotEquals_forDifferentSymbols() {
        val a = Nonterminal('A')
        val b = Nonterminal('B')
        a shouldNotEqual b
    }

    @Test
    fun terminalsAndNonterminals_areNotEquals_forSameSymbols() {
        val a = Nonterminal('A')
        val b = Terminal('A')
        a shouldNotEqual b
    }

    @Test
    fun terminalsAndNonterminals_areNotEquals_forDifferentSymbols() {
        val a = Nonterminal('A')
        val b = Terminal('b')
        a shouldNotEqual b
    }
}