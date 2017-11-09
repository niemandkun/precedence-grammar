package tech.niemandkun.utils

fun <T> Iterable<T>.bigrams(): List<Pair<T, T>> {
    return this.zip(this.drop(1))
}

fun <T> Iterable<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
    val result = mutableListOf<T>()
    for (item in this) {
        result.add(item)
        if (!predicate(item)) break
    }
    return result
}
