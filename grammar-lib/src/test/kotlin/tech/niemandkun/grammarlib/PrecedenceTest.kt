package tech.niemandkun.grammarlib

import org.amshove.kluent.shouldEqual
import org.junit.Test

class PrecedenceTest : BaseGrammarTest() {
    @Test
    fun firstSample() {
        val table = PrecedenceTable(grammar(
                "S dSAb",
                "S c",
                "A Ab",
                "A b"
        ))

        println(table)

        table[S, S] shouldEqual NONE
        table[S, A] shouldEqual LESS + EQUAL
        table[S, b] shouldEqual LESS
        table[S, c] shouldEqual NONE
        table[S, d] shouldEqual NONE
        table[S, Symbol.END_OF_LINE] shouldEqual GREATER

        table[A, S] shouldEqual NONE
        table[A, A] shouldEqual NONE
        table[A, b] shouldEqual EQUAL
        table[A, c] shouldEqual NONE
        table[A, d] shouldEqual NONE
        table[A, Symbol.END_OF_LINE] shouldEqual NONE

        table[b, S] shouldEqual NONE
        table[b, A] shouldEqual NONE
        table[b, b] shouldEqual GREATER
        table[b, c] shouldEqual NONE
        table[b, d] shouldEqual NONE
        table[b, Symbol.END_OF_LINE] shouldEqual GREATER

        table[c, S] shouldEqual NONE
        table[c, A] shouldEqual NONE
        table[c, b] shouldEqual GREATER
        table[c, c] shouldEqual NONE
        table[c, d] shouldEqual NONE
        table[c, Symbol.END_OF_LINE] shouldEqual GREATER

        table[d, S] shouldEqual EQUAL
        table[d, A] shouldEqual NONE
        table[d, b] shouldEqual NONE
        table[d, c] shouldEqual LESS
        table[d, d] shouldEqual LESS
        table[d, Symbol.END_OF_LINE] shouldEqual NONE

        table[Symbol.START_OF_LINE, S] shouldEqual LESS
        table[Symbol.START_OF_LINE, A] shouldEqual NONE
        table[Symbol.START_OF_LINE, b] shouldEqual NONE
        table[Symbol.START_OF_LINE, c] shouldEqual LESS
        table[Symbol.START_OF_LINE, d] shouldEqual LESS
        table[Symbol.START_OF_LINE, Symbol.END_OF_LINE] shouldEqual LESS
    }

    @Test
    fun secondSample() {
        val table = PrecedenceTable(grammar("S dSSb", "S c"))

        println(table)

        table[S, S] shouldEqual EQUAL
        table[S, b] shouldEqual EQUAL
        table[S, c] shouldEqual LESS
        table[S, d] shouldEqual LESS
        table[S, Symbol.END_OF_LINE] shouldEqual GREATER

        table[b, S] shouldEqual NONE
        table[b, b] shouldEqual GREATER
        table[b, c] shouldEqual GREATER
        table[b, d] shouldEqual GREATER
        table[b, Symbol.END_OF_LINE] shouldEqual GREATER

        table[c, S] shouldEqual NONE
        table[c, b] shouldEqual GREATER
        table[c, c] shouldEqual GREATER
        table[c, d] shouldEqual GREATER
        table[c, Symbol.END_OF_LINE] shouldEqual GREATER

        table[d, S] shouldEqual EQUAL
        table[d, b] shouldEqual NONE
        table[d, c] shouldEqual LESS
        table[d, d] shouldEqual LESS
        table[d, Symbol.END_OF_LINE] shouldEqual NONE

        table[Symbol.START_OF_LINE, S] shouldEqual LESS
        table[Symbol.START_OF_LINE, b] shouldEqual NONE
        table[Symbol.START_OF_LINE, c] shouldEqual LESS
        table[Symbol.START_OF_LINE, d] shouldEqual LESS
        table[Symbol.START_OF_LINE, Symbol.END_OF_LINE] shouldEqual LESS
    }

    @Test
    fun thirdSample() {
        val table = PrecedenceTable(grammar("S SdAb", "S c", "A SS", "A b"))

        table[S, S] shouldEqual LESS + EQUAL
        table[S, A] shouldEqual NONE
        table[S, d] shouldEqual EQUAL
        table[S, b] shouldEqual GREATER
        table[S, c] shouldEqual LESS
        table[S, Symbol.END_OF_LINE] shouldEqual GREATER

        table[A, S] shouldEqual NONE
        table[A, A] shouldEqual NONE
        table[A, d] shouldEqual NONE
        table[A, b] shouldEqual EQUAL
        table[A, c] shouldEqual NONE
        table[A, Symbol.END_OF_LINE] shouldEqual NONE

        table[d, S] shouldEqual LESS
        table[d, A] shouldEqual EQUAL
        table[d, d] shouldEqual NONE
        table[d, b] shouldEqual LESS
        table[d, c] shouldEqual LESS
        table[d, Symbol.END_OF_LINE] shouldEqual NONE

        table[b, S] shouldEqual NONE
        table[b, A] shouldEqual NONE
        table[b, d] shouldEqual GREATER
        table[b, b] shouldEqual GREATER
        table[b, c] shouldEqual GREATER
        table[b, Symbol.END_OF_LINE] shouldEqual GREATER

        table[c, S] shouldEqual NONE
        table[c, A] shouldEqual NONE
        table[c, d] shouldEqual GREATER
        table[c, b] shouldEqual GREATER
        table[c, c] shouldEqual GREATER
        table[c, Symbol.END_OF_LINE] shouldEqual GREATER

        table[Symbol.START_OF_LINE, S] shouldEqual LESS
        table[Symbol.START_OF_LINE, A] shouldEqual NONE
        table[Symbol.START_OF_LINE, d] shouldEqual NONE
        table[Symbol.START_OF_LINE, b] shouldEqual NONE
        table[Symbol.START_OF_LINE, c] shouldEqual LESS
        table[Symbol.START_OF_LINE, Symbol.END_OF_LINE] shouldEqual LESS
    }

    companion object {
        val NONE = emptySet<Precedence>()
        val LESS = setOf(Precedence.LESS)
        val GREATER = setOf(Precedence.GREATER)
        val EQUAL = setOf(Precedence.EQUAL)
    }
}