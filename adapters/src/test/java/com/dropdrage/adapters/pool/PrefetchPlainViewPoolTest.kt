package com.dropdrage.adapters.pool

import androidx.recyclerview.widget.RecyclerView
import com.dropdrage.test.util.mockLooper
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExtendWith(MockKExtension::class)
internal class PrefetchPlainViewPoolTest {

    @MockK(relaxed = true)
    lateinit var viewHolderSupplier: ViewHolderSupplier


    @ParameterizedTest
    @ValueSource(ints = [-1, 0, 1, 2, 10])
    fun `create pool`(maxRecycledViews: Int) {
        val pool = createViewPool(maxRecycledViews)

        assertEquals(maxRecycledViews, pool.getMaxRecycledViews())
        verify { viewHolderSupplier.start() }
        assertEquals(0, pool.getRecycledViewCount(DEFAULT_VIEW_TYPE))
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, 1, 2, 10])
    fun `create pool with coroutines supplier with default prefetch`(maxRecycledViews: Int) = mockLooper {
        mockSupplierConstructor {
            val pool = spyk(
                PrefetchPlainViewPool.createPrefetchedWithCoroutineSupplier(mockk(), mockk(), maxRecycledViews),
                recordPrivateCalls = true
            )

            val viewHolderSupplier = pool.getViewHolderSupplier()
            assertEquals(maxRecycledViews, pool.getMaxRecycledViews())
            verify { viewHolderSupplier.start() }
            verify { viewHolderSupplier.prefetch(eq(DEFAULT_VIEW_TYPE), eq(maxRecycledViews)) }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 10])
    fun `create pool with coroutines supplier with custom prefetch`(prefetchCount: Int) = mockLooper {
        mockSupplierConstructor {
            val maxRecycledViews = 5
            val pool = PrefetchPlainViewPool.createPrefetchedWithCoroutineSupplier(
                mockk(),
                mockk(),
                maxRecycledViews,
                prefetchCount
            )

            val viewHolderSupplier = pool.getViewHolderSupplier()
            assertThat(pool.getMaxRecycledViews()).isAnyOf(prefetchCount, maxRecycledViews)
            verify { viewHolderSupplier.start() }
            verify { viewHolderSupplier.prefetch(eq(DEFAULT_VIEW_TYPE), eq(prefetchCount)) }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, 2, 10])
    fun prefetch(maxRecycledViews: Int) {
        val pool = createViewPool(maxRecycledViews)

        val prefetchCount = 1
        pool.prefetch(prefetchCount)
        assertThat(pool.getMaxRecycledViews()).isAtLeast(prefetchCount)
        verify { viewHolderSupplier.prefetch(eq(DEFAULT_VIEW_TYPE), eq(prefetchCount)) }
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 5, 10])
    fun putRecycledView(viewsToPut: Int) {
        val maxRecycledViews = 5
        val pool = createMockViewPool(5)

        assertEquals(0, pool.getRecycledViewCount(DEFAULT_VIEW_TYPE))

        repeat(viewsToPut) {
            val view = mockk<RecyclerView.ViewHolder>(relaxed = true) {
                every { itemViewType } returns DEFAULT_VIEW_TYPE
            }
            pool.putRecycledView(view)
        }

        val poolMaxRecycledViews = pool.getMaxRecycledViews()
        verify { pool.setMaxRecycledViews(eq(DEFAULT_VIEW_TYPE), eq(poolMaxRecycledViews)) }
        assertThat(pool.getRecycledViewCount(DEFAULT_VIEW_TYPE)).isAnyOf(viewsToPut, maxRecycledViews)
    }

    @Test
    fun `getRecycledView exist`() {
        val pool = createMockViewPool(1)

        val view = mockk<RecyclerView.ViewHolder>(relaxed = true) {
            every { itemViewType } returns DEFAULT_VIEW_TYPE
        }
        pool.putRecycledView(view)
        val returnedView = pool.getRecycledView(DEFAULT_VIEW_TYPE)

        assertNotNull(returnedView)
        verify(inverse = true) { viewHolderSupplier.onItemCreatedOutside(any()) }
    }

    @Test
    fun `getRecycledView not exist`() {
        val pool = createMockViewPool(0)

        val returnedView = pool.getRecycledView(DEFAULT_VIEW_TYPE)

        assertNull(returnedView)
        verify { viewHolderSupplier.onItemCreatedOutside(any()) }
    }

    @Test
    fun `clear empty`() {
        val pool = createMockViewPool(0, true)

        pool.clear()

        assertEquals(0, pool.getRecycledViewCount(DEFAULT_VIEW_TYPE))
        verify { viewHolderSupplier.stop() }
    }

    @Test
    fun `clear not empty`() {
        val viewsToPut = 5
        val pool = createMockViewPool(viewsToPut)

        repeat(viewsToPut) {
            val view = mockk<RecyclerView.ViewHolder>(relaxed = true) {
                every { itemViewType } returns DEFAULT_VIEW_TYPE
            }
            pool.putRecycledView(view)
        }

        assertEquals(viewsToPut, pool.getRecycledViewCount(DEFAULT_VIEW_TYPE))

        pool.clear()

        assertEquals(0, pool.getRecycledViewCount(DEFAULT_VIEW_TYPE))
        verify { viewHolderSupplier.stop() }
    }


    private fun createViewPool(maxRecycledViews: Int) = PrefetchPlainViewPool(maxRecycledViews, viewHolderSupplier)

    private fun createMockViewPool(maxRecycledViews: Int, recordPrivateCalls: Boolean = false) =
        spyk(createViewPool(maxRecycledViews), recordPrivateCalls = recordPrivateCalls)

    private inline fun mockSupplierConstructor(block: () -> Unit) =
        mockkConstructor(CoroutinesViewHolderSupplier::class) {
            justRun { anyConstructed<CoroutinesViewHolderSupplier>().start() }
            justRun { anyConstructed<CoroutinesViewHolderSupplier>().prefetch(any(), any()) }
            justRun { anyConstructed<CoroutinesViewHolderSupplier>()["createItem"](any<Int>()) }

            block()
        }

}
