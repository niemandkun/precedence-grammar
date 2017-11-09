package tech.niemandkun.parser

interface StateMachine<TState : ParserState> {
    fun init(input: String): TState
    fun step(state: TState): TState
}
