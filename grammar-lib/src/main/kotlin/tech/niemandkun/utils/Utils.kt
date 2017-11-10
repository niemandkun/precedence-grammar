package tech.niemandkun.utils

fun <T> Iterable<T>.bigrams(): List<Pair<T, T>> {
    return this.zip(this.drop(1))
}

fun <T> Iterable<T>.cartesianSquare(): List<Pair<T, T>> {
    return this.zip(this)
}

fun <T> List<T>.isSuffixOf(other: List<T>): Boolean {
    return this.reversed() == other.reversed().take(this.size)
}

fun <T> Iterable<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
    val result = mutableListOf<T>()
    for (item in this) {
        result.add(item)
        if (!predicate(item)) break
    }
    return result
}
