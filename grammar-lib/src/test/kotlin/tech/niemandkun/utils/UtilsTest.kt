package tech.niemandkun.utils

import org.amshove.kluent.shouldEqual
import org.junit.Test

class UtilsTest {
    @Test
    fun suffixesTest() {
        listOf(1, 2, 3, 4).suffixes() shouldEqual listOf(
                listOf(4),
                listOf(3, 4),
                listOf(2, 3, 4),
                listOf(1, 2, 3, 4)
        )
    }

    @Test
    fun suffixesTest_ifEmpty() {
        emptyList<Any>().suffixes() shouldEqual emptyList()
    }

    @Test
    fun suffixesTest_ifSingleElement() {
        listOf(1).suffixes() shouldEqual listOf(listOf(1))
    }
}