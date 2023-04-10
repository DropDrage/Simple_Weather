package com.dropdrage.adapters.differ

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DefaultDifferCallbackTest {

    private val differ = DefaultDifferCallback<FakeSameEquatable>()


    @Test
    fun `areItemsTheSame true`() {
        val a = FakeSameEquatable(0, 1)
        val b = FakeSameEquatable(0, 2)

        assertTrue(differ.areItemsTheSame(a, b))
    }

    @Test
    fun `areItemsTheSame false`() {
        val a = FakeSameEquatable(0, 1)
        val b = FakeSameEquatable(1, 1)

        assertFalse(differ.areItemsTheSame(a, b))
    }

    @Test
    fun `areContentsTheSame true`() {
        val a = FakeSameEquatable(0, 1)
        val b = FakeSameEquatable(0, 1)

        assertTrue(differ.areContentsTheSame(a, b))
    }

    @Test
    fun `areContentsTheSame id different`() {
        val a = FakeSameEquatable(0, 1)
        val b = FakeSameEquatable(1, 1)

        assertFalse(differ.areContentsTheSame(a, b))
    }

    @Test
    fun `areContentsTheSame content different`() {
        val a = FakeSameEquatable(0, 1)
        val b = FakeSameEquatable(0, 2)

        assertFalse(differ.areContentsTheSame(a, b))
    }

}

private data class FakeSameEquatable(val id: Int, val content: Int) : SameEquatable<FakeSameEquatable> {
    override fun isSame(other: FakeSameEquatable): Boolean = id == other.id
}
