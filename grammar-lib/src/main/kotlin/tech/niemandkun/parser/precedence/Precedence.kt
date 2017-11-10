package tech.niemandkun.parser.precedence

enum class Precedence {
    LESS,
    GREATER,
    EQUAL;

    override fun toString(): String {
        return when (this) {
            LESS -> "<"
            GREATER -> ">"
            EQUAL -> "="
        }
    }
}
