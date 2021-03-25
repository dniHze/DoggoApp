package dev.kaitei.feature.list.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BreedListSegmentTest {

    @Test
    fun `compare to same on segment capital letter`() {
        val first = BreedListSegment("A", emptyList())
        val second = BreedListSegment("A", emptyList())
        assertEquals(0, first.compareTo(second))
    }

    @Test
    fun `compare to larger on segment capital letter`() {
        val first = BreedListSegment("B", emptyList())
        val second = BreedListSegment("A", emptyList())
        assertTrue(first > second)
    }

    @Test
    fun `compare to smaller on segment capital letter`() {
        val first = BreedListSegment("A", emptyList())
        val second = BreedListSegment("B", emptyList())
        assertTrue(first < second)
    }
}