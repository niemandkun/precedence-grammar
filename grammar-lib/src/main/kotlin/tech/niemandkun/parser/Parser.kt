package tech.niemandkun.parser

class Parser<TState : ParserState>(private val stateMachine: StateMachine<TState>) {
    fun parse(input: String, callback: Callback) {
        Runner(stateMachine.init(input))
                .forEach { when(it) {
                    is ParserState -> callback.onStep(it)
                    is Throwable -> callback.onError(it)
                } }
    }

    inner class Runner(initialState: TState) : Iterator<Any> {
        private var currentState: TState = initialState
        private var isRunning = false
        private var isError = false

        override fun hasNext() = !currentState.isTerminating && !isError

        override fun next() = try {
            if (!isRunning) {
                isRunning = true
            } else {
                currentState = stateMachine.step(currentState)
            }
            currentState
        } catch (error: Throwable) {
            isError = true
            error
        }
    }

    interface Callback {
        fun onStep(state: ParserState)
        fun onError(error: Throwable)
    }
}
