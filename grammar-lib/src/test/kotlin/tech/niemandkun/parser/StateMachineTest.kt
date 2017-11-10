package tech.niemandkun.parser

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Test
import tech.niemandkun.grammarlib.BaseGrammarTest
import tech.niemandkun.grammarlib.Symbol
import tech.niemandkun.parser.precedence.Precedence
import tech.niemandkun.parser.precedence.PrecedenceParserState
import tech.niemandkun.parser.precedence.PrecedenceStateMachine
import kotlin.test.assertFailsWith

class StateMachineTest : BaseGrammarTest() {
    @Test
    fun testParsingSuccess() {
        val grammar = grammar("S eASb", "S d", "A Ac", "A c")
        val stateMachine = PrecedenceStateMachine(grammar)

        val table = stateMachine.table

        val firstState = stateMachine.init("ecdb")
        firstState.isTerminating shouldBe false

        assert(firstState,
                emptyList(),
                listOf(Symbol.START_OF_LINE),
                "ecdb$")

        val secondState = stateMachine.step(firstState)
        secondState.isTerminating shouldBe false

        assert(secondState,
                listOf(table[Symbol.START_OF_LINE, e]),
                listOf(Symbol.START_OF_LINE, e),
                "cdb$"
        )

        val thirdState = stateMachine.step(secondState)
        thirdState.isTerminating shouldBe false

        assert(thirdState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, c]),
                listOf(Symbol.START_OF_LINE, e, c),
                "db$"
        )

        val fourthState = stateMachine.step(thirdState)
        fourthState.isTerminating shouldBe false

        assert(fourthState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, A]),
                listOf(Symbol.START_OF_LINE, e, A),
                "db$"
        )

        val fifthState = stateMachine.step(fourthState)
        fifthState.isTerminating shouldBe false

        assert(fifthState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, A], table[A, d]),
                listOf(Symbol.START_OF_LINE, e, A, d),
                "b$"
        )

        val sixthState = stateMachine.step(fifthState)
        sixthState.isTerminating shouldBe false

        assert(sixthState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, A], table[A, S]),
                listOf(Symbol.START_OF_LINE, e, A, S),
                "b$"
        )

        val sevenState = stateMachine.step(sixthState)
        sevenState.isTerminating shouldBe false

        assert(sevenState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, A], table[A, S], table[S, b]),
                listOf(Symbol.START_OF_LINE, e, A, S, b),
                "$"
        )

        val eighthState = stateMachine.step(sevenState)
        eighthState.isTerminating shouldBe false

        assert(eighthState,
                listOf(table[Symbol.START_OF_LINE, S]),
                listOf(Symbol.START_OF_LINE, S),
                "$"
        )

        val ninthState = stateMachine.step(eighthState)
        ninthState.isTerminating shouldBe true

        assert(ninthState,
                listOf(table[Symbol.START_OF_LINE, S], table[S, Symbol.END_OF_LINE]),
                listOf(Symbol.START_OF_LINE, S, Symbol.END_OF_LINE),
                ""
        )
    }

    @Test
    fun testParsingError() {
        val grammar = grammar("S eASb", "S d", "A Ac", "A c")
        val stateMachine = PrecedenceStateMachine(grammar)

        val table = stateMachine.table

        val firstState = stateMachine.init("ecdc")
        firstState.isTerminating shouldBe false

        assert(firstState,
                emptyList(),
                listOf(Symbol.START_OF_LINE),
                "ecdc$")

        val secondState = stateMachine.step(firstState)
        secondState.isTerminating shouldBe false

        assert(secondState,
                listOf(table[Symbol.START_OF_LINE, e]),
                listOf(Symbol.START_OF_LINE, e),
                "cdc$")

        val thirdState = stateMachine.step(secondState)
        thirdState.isTerminating shouldBe false

        assert(thirdState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, c]),
                listOf(Symbol.START_OF_LINE, e, c),
                "dc$")

        val fourthState = stateMachine.step(thirdState)
        fourthState.isTerminating shouldBe false

        assert(fourthState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, A]),
                listOf(Symbol.START_OF_LINE, e, A),
                "dc$")

        val fifthState = stateMachine.step(fourthState)
        fifthState.isTerminating shouldBe false

        assert(fifthState,
                listOf(table[Symbol.START_OF_LINE, e], table[e, A], table[A, d]),
                listOf(Symbol.START_OF_LINE, e, A, d),
                "c$")

        assertFailsWith(IllegalStateException::class, {
            stateMachine.step(fifthState)
        })
    }

    private fun assert(state: PrecedenceParserState, precedence: List<Set<Precedence>>, stack: List<Symbol>, input: String) {
        state.precedence shouldEqual precedence
        state.stack shouldEqual stack
        state.input shouldEqual input
    }
}