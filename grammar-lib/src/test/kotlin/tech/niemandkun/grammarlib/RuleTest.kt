package tech.niemandkun.grammarlib

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.junit.Test
import tech.niemandkun.grammarlib.Nonterminal
import tech.niemandkun.grammarlib.Rule
import tech.niemandkun.grammarlib.Symbol

class RuleTest {
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
        Rule.parse(input) shouldEqual Rule(Nonterminal(nonterminal), Symbol.symbols(symbols))
    }

    @Test
    fun rules_shouldBeEqual_ifTerminalAndProductionAreTheSame() {
        Rule(Nonterminal('S'), Symbol.nonterminals("adsf")) shouldEqual
                Rule(Nonterminal('S'), Symbol.nonterminals("adsf"))
    }

    @Test
    fun rules_shouldBeNotEqual_ifTerminalsDiffer() {
        Rule(Nonterminal('S'), Symbol.nonterminals("adsf")) shouldNotEqual
                Rule(Nonterminal('A'), Symbol.nonterminals("adsf"))
    }

    @Test
    fun rules_shouldBeNotEqual_ifProductionDiffer() {
        Rule(Nonterminal('S'), Symbol.nonterminals("qwer")) shouldNotEqual
                Rule(Nonterminal('S'), Symbol.nonterminals("adsf"))
    }
}