package tech.niemandkun.grammarlib

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.junit.Test

class GrammarRuleTest {
    @Test
    fun rules_parsing_shouldWork() {
        runParseTest(
                "S abcdABCD",
                'S', "abcdABCD"
        )
    }

    @Test
    fun rules_parsing_withLeadingSpaces() {
        runParseTest(
                "             S abcdABCD",
                'S', "abcdABCD"
        )
    }

    @Test
    fun rules_parsing_withTrailingSpaces() {
        runParseTest(
                "S abcdABCD                      ",
                'S', "abcdABCD"
        )
    }

    @Test
    fun rules_parsing_withLeadingNewline() {
        runParseTest(
                "\nS abcdABCD",
                'S', "abcdABCD"
        )
    }

    @Test
    fun rules_parsing_withTrailingNewline() {
        runParseTest(
                "S abcdABCD\n",
                'S', "abcdABCD"
        )
    }

    @Test
    fun rules_parsing_withInnerWhitespaces() {
        runParseTest(
                "S                      abcdABCD",
                'S', "abcdABCD"
        )
    }

    private fun runParseTest(input: String, nonterminal: Char, symbols: String) {
        GrammarRule.parse(input) shouldEqual GrammarRule(Nonterminal(nonterminal), Symbol.symbols(symbols))
    }

    @Test
    fun rules_shouldBeEqual_ifTerminalAndProductionAreTheSame() {
        GrammarRule(Nonterminal('S'), Symbol.nonterminals("adsf")) shouldEqual
                GrammarRule(Nonterminal('S'), Symbol.nonterminals("adsf"))
    }

    @Test
    fun rules_shouldBeNotEqual_ifTerminalsDiffer() {
        GrammarRule(Nonterminal('S'), Symbol.nonterminals("adsf")) shouldNotEqual
                GrammarRule(Nonterminal('A'), Symbol.nonterminals("adsf"))
    }

    @Test
    fun rules_shouldBeNotEqual_ifProductionDiffer() {
        GrammarRule(Nonterminal('S'), Symbol.nonterminals("qwer")) shouldNotEqual
                GrammarRule(Nonterminal('S'), Symbol.nonterminals("adsf"))
    }
}