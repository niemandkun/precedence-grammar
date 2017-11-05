package tech.niemandkun.grammarlib

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Test

class GrammarTest : BaseGrammarTest() {
    @Test
    fun grammar_parsing_shouldWork() {
        val input = listOf(
                "S aAb",
                "A bSa\n",
                "\nA b",
                "A\ta",
                "A    c",
                "A \n",
                "A"
        )

        val rules = input.map { Rule.parse(it) }

        val grammar = Grammar(rules)

        grammar.rules shouldEqual listOf(
                Rule(Nonterminal('S'), listOf(Terminal('a'), Nonterminal('A'), Terminal('b'))),
                Rule(Nonterminal('A'), listOf(Terminal('b'), Nonterminal('S'), Terminal('a'))),
                Rule(Nonterminal('A'), listOf(Terminal('b'))),
                Rule(Nonterminal('A'), listOf(Terminal('a'))),
                Rule(Nonterminal('A'), listOf(Terminal('c'))),
                Rule(Nonterminal('A'), listOf()),
                Rule(Nonterminal('A'), listOf())
        )

        grammar.terminals shouldEqual Symbol.terminals("abc")

        grammar.nonterminals shouldEqual Symbol.nonterminals("SA")

        grammar.axiom shouldEqual Nonterminal('S')
    }

    @Test
    fun grammar_parsing_shouldWork2() {
        with(grammar("S SdAb", "S c", "A SS", "A b")) {
            rules shouldEqual listOf(
                    Rule(S, listOf(S, d, A, b)),
                    Rule(S, listOf(c)),
                    Rule(A, listOf(S, S)),
                    Rule(A, listOf(b))
                )
        }
    }

    @Test
    fun isUniquelyInversible_ifHasNoSimilarProductins() {
        grammar("S abc", "A cba", "A aab", "S abb").isUniquelyInversible shouldBe true
    }

    @Test
    fun isNotUniquelyInversible_ifHasSimilarProductins() {
        grammar("S abc", "A bba", "A abc", "S abb").isUniquelyInversible shouldBe false
    }

    @Test
    fun first_shouldWork_withoutErasingRules() {
        with (grammar("S aASb", "S d", "A Ac", "A c")) {
            first(Nonterminal('S')) shouldEqual Symbol.symbols("ad").toSet()
            first(Nonterminal('A')) shouldEqual Symbol.symbols("Ac").toSet()
            first(Terminal('a')) shouldEqual emptySet()
            first(Terminal('b')) shouldEqual emptySet()
            first(Terminal('d')) shouldEqual emptySet()
            first(Terminal('c')) shouldEqual emptySet()
        }
    }

    @Test
    fun firstAndLast_shouldWork_withFirstPrecedenceSample() {
        with (grammar("S dSAb", "S c", "A Ab", "A b")) {
            first(S) shouldEqual setOf(c, d)
            last(S) shouldEqual setOf(b, c)

            first(A) shouldEqual setOf(A, b)
            last(A) shouldEqual setOf(b)

            first(b) shouldEqual emptySet()
            last(b) shouldEqual emptySet()

            first(c) shouldEqual emptySet()
            last(c) shouldEqual emptySet()

            first(d) shouldEqual emptySet()
            last(d) shouldEqual emptySet()
        }
    }

    @Test
    fun firstAndLast_shouldWork_withSecondPrecedenceSample() {
        with(grammar("S dSSb", "S c")) {
            first(S) shouldEqual setOf(d, c)
            last(S) shouldEqual setOf(b, c)

            first(d) shouldEqual emptySet()
            last(d) shouldEqual emptySet()

            first(b) shouldEqual emptySet()
            last(b) shouldEqual emptySet()

            first(c) shouldEqual emptySet()
            last(c) shouldEqual emptySet()
        }
    }

    @Test
    fun firstAndLast_shouldWork_withThirdPrecedenceSample() {
        with(grammar("S SdAb", "S c", "A SS", "A b")) {
            first(S) shouldEqual setOf(S, c)
            last(S) shouldEqual setOf(b, c)

            first(A) shouldEqual setOf(S, b, c)
            last(A) shouldEqual setOf(S, b, c)
        }
    }

    @Test
    fun last_shouldWork_withoutErasingRules() {
        with (grammar("S aASb", "S d", "A Ac", "A c")) {
            last(Nonterminal('S')) shouldEqual Symbol.symbols("bd").toSet()
            last(Nonterminal('A')) shouldEqual Symbol.symbols("c").toSet()
            last(Terminal('a')) shouldEqual emptySet()
            last(Terminal('b')) shouldEqual emptySet()
            last(Terminal('d')) shouldEqual emptySet()
            last(Terminal('c')) shouldEqual emptySet()
        }
    }

    @Test
    fun first_shouldNotCrash_withErasingRules() {
        with (grammar("S aASb", "A c", "S", "A")) {
            first(Nonterminal('S')) shouldEqual setOf(Terminal('a'))
            first(Nonterminal('A')) shouldEqual setOf(Terminal('c'))
        }
    }

    @Test
    fun last_shouldNotCrash_withErasingRules() {
        with (grammar("S aASb", "A c", "S", "A")) {
            last(Nonterminal('S')) shouldEqual setOf(Terminal('b'))
            last(Nonterminal('A')) shouldEqual setOf(Terminal('c'))
        }
    }
}