package com.dropdrage.adapters.pool

import android.os.Handler
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.dropdrage.common.test.util.verifyNever
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.isActive
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

private const val VIEW_TYPE = 0

@ExtendWith(MockKExtension::class)
internal class CoroutinesViewHolderSupplierTest {

    @SpyK
    private var testUtilsClass: Utils = Utils()

    @RelaxedMockK
    lateinit var viewHolderConsumer: ViewHolderConsumer


    @Test
    fun start() {
        val supplier = createSupplier()

        assertDoesNotThrow { supplier.start() }
    }

    @Test
    fun stop() {
        val supplier = createSupplier()

        supplier.start()
        assertTrue(supplier.isActive)
        supplier.stop()
        assertFalse(supplier.isActive)
        supplier.prefetch(VIEW_TYPE, 1)

        verify(inverse = true, timeout = 500) { testUtilsClass.viewHolderProducer(any(), eq(VIEW_TYPE)) }
        verifyNever { viewHolderConsumer(any()) }

        supplier.stop()
    }

    @Test
    fun `start cannot be called after stop`() {
        val supplier = createSupplier()

        supplier.start()
        supplier.stop()

        assertThrows<IllegalStateException> { supplier.start() }
    }


    @Nested
    inner class prefetch {

        @Test
        fun `0 count`() {
            val supplier = createInitializedSupplier()

            supplier.prefetch(VIEW_TYPE, 0)

            coVerify(exactly = 0, timeout = 500) { testUtilsClass.viewHolderProducer(any(), eq(VIEW_TYPE)) }
            verifyNever { viewHolderConsumer(any()) }

            supplier.stop()
        }

        @ParameterizedTest
        @ValueSource(ints = [1, 10])
        fun `positive count`(count: Int) = mockkConstructor(FrameLayout::class, Handler::class) {
            every { anyConstructed<Handler>().postAtFrontOfQueue(any()) } answers {
                firstArg<Runnable>().run()
                true
            }

            val supplier = createInitializedSupplier()

            supplier.prefetch(VIEW_TYPE, count)

            verify(exactly = count, timeout = 3000) { testUtilsClass.viewHolderProducer(any(), eq(VIEW_TYPE)) }
            verify(exactly = count, timeout = 3000) { viewHolderConsumer(any()) }

            supplier.stop()
        }


        @ParameterizedTest
        @ValueSource(ints = [-1, -10])
        fun `negative count`(count: Int) {
            val supplier = createInitializedSupplier()

            supplier.prefetch(VIEW_TYPE, count)

            coVerify(inverse = true, timeout = 500) { testUtilsClass.viewHolderProducer(any(), eq(VIEW_TYPE)) }
            verifyNever { viewHolderConsumer(any()) }

            supplier.stop()
        }

    }

    @Test
    fun onItemCreatedOutside() {
        val supplier = createSupplier()

        assertDoesNotThrow { supplier.onItemCreatedOutside(VIEW_TYPE) }
        //does nothing noticeable outside so nothing to check
    }


    private fun createSupplier(recordPrivateCalls: Boolean = false): CoroutinesViewHolderSupplier = spyk(
        CoroutinesViewHolderSupplier(mockk(), testUtilsClass::viewHolderProducer),
        recordPrivateCalls = recordPrivateCalls,
    )

    private fun createInitializedSupplier(recordPrivateCalls: Boolean = false) =
        createSupplier(recordPrivateCalls).apply {
            viewHolderConsumer = this@CoroutinesViewHolderSupplierTest.viewHolderConsumer
            start()
        }


    @Suppress("UNUSED_PARAMETER")
    private class Utils {
        fun viewHolderProducer(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = mockk(relaxed = true) {
            every { itemViewType } returns viewType
        }
    }

}
