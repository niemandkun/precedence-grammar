package tech.niemandkun.parser.precedence

import tech.niemandkun.grammarlib.Grammar
import tech.niemandkun.parser.StateMachine

class PrecedenceStateMachine(private val grammar: Grammar) : StateMachine<PrecedenceParserState> {

    val table = PrecedenceTable(grammar)

    override fun init(input: String) =
            PrecedenceParserState.init(table, grammar.axiom, input)

    override fun step(state: PrecedenceParserState): PrecedenceParserState {
        return if (state.isReducible) {
            val production = state.getProduction(grammar.rules)
            val rule = grammar.findRuleBy(production)
            state.reduce(rule)
        } else {
            state.shift()
        }
    }
}