import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Test
import tech.niemandkun.grammarlib.*

class GrammarTest {
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
    fun isUniquelyInversible_ifHasNoSimilarProductins() {
        val grammar = makeGrammar(
                "S abc",
                "A cba",
                "A aab",
                "S abb"
        )

        grammar.isUniquelyInversible shouldBe true
    }

    @Test
    fun isNotUniquelyInversible_ifHasSimilarProductins() {
        val grammar = makeGrammar(
                "S abc",
                "A bba",
                "A abc",
                "S abb"
        )

        grammar.isUniquelyInversible shouldBe false
    }

    @Test
    fun first_shouldWork_withoutErasingRules() {
        val grammar = makeGrammar(
                "S aASb",
                "S d",
                "A Ac",
                "A c"
        )

        grammar.first[Nonterminal('S')] shouldEqual Symbol.symbols("ad").toSet()
        grammar.first[Nonterminal('A')] shouldEqual Symbol.symbols("Ac").toSet()
        grammar.first[Terminal('a')] shouldEqual setOf(Terminal('a'))
        grammar.first[Terminal('b')] shouldEqual setOf(Terminal('b'))
        grammar.first[Terminal('d')] shouldEqual setOf(Terminal('d'))
        grammar.first[Terminal('c')] shouldEqual setOf(Terminal('c'))
    }

    @Test
    fun last_shouldWork_withoutErasingRules() {
        val grammar = makeGrammar(
                "S aASb",
                "S d",
                "A Ac",
                "A c"
        )

        grammar.last[Nonterminal('S')] shouldEqual Symbol.symbols("bd").toSet()
        grammar.last[Nonterminal('A')] shouldEqual Symbol.symbols("c").toSet()
        grammar.last[Terminal('a')] shouldEqual setOf(Terminal('a'))
        grammar.last[Terminal('b')] shouldEqual setOf(Terminal('b'))
        grammar.last[Terminal('d')] shouldEqual setOf(Terminal('d'))
        grammar.last[Terminal('c')] shouldEqual setOf(Terminal('c'))
    }

    @Test
    fun first_shouldNotCrash_withErasingRules() {
        val grammar = makeGrammar(
                "S aASb",
                "A c",
                "S",
                "A"
        )

        grammar.first[Nonterminal('S')] shouldEqual setOf(Terminal('a'))
        grammar.first[Nonterminal('A')] shouldEqual setOf(Terminal('c'))
    }

    @Test
    fun last_shouldNotCrash_withErasingRules() {
        val grammar = makeGrammar(
                "S aASb",
                "A c",
                "S",
                "A"
        )

        grammar.last[Nonterminal('S')] shouldEqual setOf(Terminal('b'))
        grammar.last[Nonterminal('A')] shouldEqual setOf(Terminal('c'))
    }

    private fun makeGrammar(vararg rules: String) = Grammar(rules.map { Rule.parse(it) })
}