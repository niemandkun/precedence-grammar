package tech.niemandkun.parser

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Test
import tech.niemandkun.grammarlib.BaseGrammarTest
import tech.niemandkun.grammarlib.Symbol
import tech.niemandkun.parser.precedence.PrecedenceParserState
import tech.niemandkun.parser.precedence.PrecedenceStateMachine

class BugFixTest : BaseGrammarTest() {

    @Test
    fun testBugFix_withSimplePrecedenceGrammar() {

        val stateMachine = PrecedenceStateMachine(grammar(
                "S SeSb",
                "S c"
        ))

        val errorState = PrecedenceParserState(stateMachine.table, S,
                listOf(LESS, NONE, LESS + EQUAL, NONE, LESS + EQUAL, EQUAL),
                listOf(Symbol.START_OF_LINE, S, e, S, e, S, b),
                "ecbb$"
        )

        println(errorState.peekStack())
        println(errorState.peekInput())
        println(stateMachine.table[errorState.peekStack(), errorState.peekInput()])

        errorState.isReducible shouldBe true
        errorState.isTerminating shouldBe false
        errorState.isPreFinal shouldBe false

        val nextState = stateMachine.step(errorState)

        nextState.stack shouldEqual listOf(Symbol.START_OF_LINE, S, e, S)
        nextState.input shouldEqual "ecbb$"
        nextState.precedence shouldEqual listOf(LESS, NONE, LESS + EQUAL)
    }
}