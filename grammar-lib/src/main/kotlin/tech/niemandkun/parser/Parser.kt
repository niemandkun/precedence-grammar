package tech.niemandkun.parser

class Parser<TState : ParserState>(private val stateMachine: StateMachine<TState>) {
    fun parse(input: String, callback: Callback) {
        Runner(input)
                .forEach { when(it) {
                    is ParserState -> callback.onStep(it)
                    is Throwable -> callback.onError(it)
                } }
        callback.onEnd()
    }

    inner class Runner(input: String) : Iterator<Any> {
        private var currentState = stateMachine.init(input)
        private var isError = false

        override fun hasNext() = !currentState.isTerminating && !isError

        override fun next() = try {
            currentState = stateMachine.step(currentState)
            currentState
        } catch (error: Throwable) {
            isError = true
            error
        }
    }

    interface Callback {
        fun onStep(state: ParserState)
        fun onError(error: Throwable)
        fun onEnd()
    }
}
