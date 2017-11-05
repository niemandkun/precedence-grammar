package tech.niemandkun.utils

fun <T> Iterable<T>.bigrams(): List<Pair<T, T>> {
    return this.zip(this.drop(1))
}