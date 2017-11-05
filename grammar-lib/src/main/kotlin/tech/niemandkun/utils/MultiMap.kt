package tech.niemandkun.utils

class MultiMap<TKey, TValue> : HashMap<TKey, Set<TValue>>() {
    fun put(key: TKey, value: TValue) {
        put(key, (get(key) ?: emptySet()) + value)
    }

    operator fun plus(other: Map<TKey, TValue>): MultiMap<TKey, TValue> =
        emptyMultiMap<TKey, TValue>().apply { addAll(this@MultiMap); addAll(other) }

    fun addAll(other: MultiMap<TKey, TValue>) {
        other.forEach { put(it.key, it.value) }
    }

    fun addAll(other: Map<TKey, TValue>) {
        other.forEach { put(it.key, it.value) }
    }
}

fun <TKey, TValue> emptyMultiMap(): MultiMap<TKey, TValue> = MultiMap()

fun <TKey, TValue> Map<TKey, TValue>.toMultiMap(): MultiMap<TKey, TValue> =
        emptyMultiMap<TKey, TValue>().apply { addAll(this@toMultiMap) }