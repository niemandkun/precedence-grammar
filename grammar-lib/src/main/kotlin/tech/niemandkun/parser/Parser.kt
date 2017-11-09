package tech.niemandkun.parser

class Parser<TState : ParserState>(private val stateMachine: StateMachine<TState>) {
    fun parse(input: String, callback: Callback) {
        Runner(input)
                .forEach { when(it) {
                    is ParserState -> { callback.onStep(it) }
                    is Throwable -> { callback.onError(it) }
                } }
    }

    inner class Runner(input: String) : Iterator<Any> {
        private var currentState = stateMachine.init(input)

        override fun hasNext() = !currentState.isTerminating

        override fun next() = try { stateMachine.step(currentState) } catch (error: Throwable) { error }
    }

    interface Callback {
        fun onStep(state: ParserState)
        fun onError(error: Throwable)
    }
}
