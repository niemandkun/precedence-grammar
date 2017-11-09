package tech.niemandkun.parser

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Test
import tech.niemandkun.grammarlib.BaseGrammarTest
import tech.niemandkun.grammarlib.Symbol
import tech.niemandkun.parser.precedence.Precedence
import tech.niemandkun.parser.precedence.PrecedenceParserState
import tech.niemandkun.parser.precedence.PrecedenceStateMachine

class StateMachineTest : BaseGrammarTest() {
    @Test
    fun testParsingSuccess() {
        val grammar = grammar("S eASb", "S d", "A Ac", "A c")
        val stateMachine = PrecedenceStateMachine(grammar)

        val table = stateMachine.table

        val firstState = stateMachine.init("ecdb")

        assert(firstState,
                emptyList(),
                listOf(Symbol.START_OF_LINE),
                "ecdb$")

        val secondState = stateMachine.step(firstState)

        assert(secondState,
                listOf(table[Symbol.START_OF_LINE, e]),
                listOf(Symbol.START_OF_LINE, e),
                "cdb$"
        )

        val thirdState = stateMachine.step(secondState)

        assert(thirdState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, c]),
                listOf(Symbol.START_OF_LINE, e, c),
                "db$"
        )

        val fourthState = stateMachine.step(thirdState)

        assert(fourthState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, A]),
                listOf(Symbol.START_OF_LINE, e, A),
                "db$"
        )

        val fifthState = stateMachine.step(fourthState)

        assert(fifthState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, A], table[A, d]),
                listOf(Symbol.START_OF_LINE, e, A, d),
                "b$"
        )

        val sixthState = stateMachine.step(fifthState)

        assert(sixthState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, A], table[A, S]),
                listOf(Symbol.START_OF_LINE, e, A, S),
                "b$"
        )

        val sevenState = stateMachine.step(sixthState)

        assert(sevenState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, A], table[A, S], table[S, b]),
                listOf(Symbol.START_OF_LINE, e, A, S, b),
                "$"
        )

        sevenState.isTerminating shouldBe false

        val eighthState = stateMachine.step(sevenState)

        assert(eighthState,
                listOf(table[Symbol.START_OF_LINE, S]),
                listOf(Symbol.START_OF_LINE, S),
                "$"
        )

        eighthState.isTerminating shouldBe false

        val nineState = stateMachine.step(eighthState)

        assert(nineState,
                listOf(table[Symbol.START_OF_LINE, S], table[S, Symbol.END_OF_LINE]),
                listOf(Symbol.START_OF_LINE, S, Symbol.END_OF_LINE),
                ""
        )

        nineState.isTerminating shouldBe true
    }

    private fun assert(state: PrecedenceParserState, precedence: List<Set<Precedence>>, stack: List<Symbol>, input: String) {
        state.precedence shouldEqual precedence
        state.stack shouldEqual stack
        state.input shouldEqual input
    }
}