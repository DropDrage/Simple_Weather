package com.dropdrage.adapters.differ

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class DefaultDifferCallbackTest {

    private val differ = DefaultDifferCallback<FakeSameEquatable>()


    @Nested
    inner class areItemsTheSame {

        @Test
        fun same() {
            val a = FakeSameEquatable(0, 1)
            val b = FakeSameEquatable(0, 2)

            assertTrue(differ.areItemsTheSame(a, b))
        }

        @Test
        fun `not same`() {
            val a = FakeSameEquatable(0, 1)
            val b = FakeSameEquatable(1, 1)

            assertFalse(differ.areItemsTheSame(a, b))
        }

    }

    @Nested
    inner class areContentsTheSame {

        @Test
        fun same() {
            val a = FakeSameEquatable(0, 1)
            val b = FakeSameEquatable(0, 1)

            assertTrue(differ.areContentsTheSame(a, b))
        }

        @Test
        fun `id different`() {
            val a = FakeSameEquatable(0, 1)
            val b = FakeSameEquatable(1, 1)

            assertFalse(differ.areContentsTheSame(a, b))
        }

        @Test
        fun `content different`() {
            val a = FakeSameEquatable(0, 1)
            val b = FakeSameEquatable(0, 2)

            assertFalse(differ.areContentsTheSame(a, b))
        }

    }


    private data class FakeSameEquatable(val id: Int, val content: Int) : SameEquatable<FakeSameEquatable> {
        override fun isSame(other: FakeSameEquatable): Boolean = id == other.id
    }

}
