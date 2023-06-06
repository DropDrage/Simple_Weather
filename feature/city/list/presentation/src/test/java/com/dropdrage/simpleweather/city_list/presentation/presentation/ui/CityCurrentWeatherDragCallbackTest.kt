package com.dropdrage.simpleweather.city_list.presentation.presentation.ui

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dropdrage.simpleweather.city_list.presentation.presentation.utils.DragListener
import com.dropdrage.simpleweather.city_list.presentation.presentation.utils.ItemsMovable
import com.dropdrage.test.util.verifyNever
import com.dropdrage.test.util.verifyOnce
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyOrder
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class CityCurrentWeatherDragCallbackTest {

    @MockK(relaxed = true)
    lateinit var dragEnd: () -> Unit

    private lateinit var callback: CityCurrentWeatherDragCallback


    @BeforeEach
    fun setUp() {
        callback = CityCurrentWeatherDragCallback(dragEnd)
    }


    @Test
    fun `onMove moveItem invoked`() {
        val viewHolderPosition = 10
        val viewHolder = mockk<RecyclerView.ViewHolder> {
            every { adapterPosition } returns viewHolderPosition
        }
        val targetPosition = 5
        val target = mockk<RecyclerView.ViewHolder> {
            every { adapterPosition } returns targetPosition
        }
        val adapter = mockk<RecyclerView.Adapter<*>>(moreInterfaces = arrayOf(ItemsMovable::class)) {
            justRun { (this@mockk as ItemsMovable).moveItem(eq(viewHolderPosition), eq(targetPosition)) }
        }
        val recyclerView = mockk<RecyclerView> {
            every { this@mockk.adapter } returns adapter
        }

        val isMoved = callback.onMove(recyclerView, viewHolder, target)

        verifyOnce { (adapter as ItemsMovable).moveItem(eq(viewHolderPosition), eq(targetPosition)) }
        assertTrue(isMoved)
    }

    @Test
    fun `onSwipe then nothing`() {
        assertDoesNotThrow { callback.onSwiped(mockk(), ItemTouchHelper.DOWN) }
    }

    @Nested
    inner class onSelectedChanged {

        @Test
        fun `state not ACTION_STATE_DRAG then nothing`() {
            assertDoesNotThrow { callback.onSelectedChanged(null, ItemTouchHelper.ACTION_STATE_IDLE) }
        }

        @Test
        fun `state ACTION_STATE_DRAG but viewHolder null then nothing`() {
            assertDoesNotThrow { callback.onSelectedChanged(null, ItemTouchHelper.ACTION_STATE_DRAG) }
        }

        @Test
        fun `state ACTION_STATE_DRAG then nothing`() {
            val viewHolder = mockk<RecyclerView.ViewHolder>(moreInterfaces = arrayOf(DragListener::class)) {
                justRun { (this@mockk as DragListener).onDragStart() }
            }

            assertDoesNotThrow { callback.onSelectedChanged(viewHolder, ItemTouchHelper.ACTION_STATE_DRAG) }

            val viewHolderDragListener = viewHolder as DragListener
            verifyNever { viewHolderDragListener.onDragEnd() }
            verifyOnce { viewHolderDragListener.onDragStart() }
        }

    }

    @Nested
    inner class clearView {

        @Test
        fun `but viewHolder not DragListener then just dragEnd`() {
            val viewHolder = FakeViewHolder(mockk(relaxed = true))

            assertDoesNotThrow { callback.clearView(mockk(), viewHolder) }

            verifyOnce { dragEnd() }
        }

        @Test
        fun `viewHolder DragListener then onDragEnd and dragEnd`() {
            val viewHolder = spyk(FakeViewHolder(mockk(relaxed = true)), moreInterfaces = arrayOf(DragListener::class))

            assertDoesNotThrow { callback.clearView(mockk(), viewHolder) }

            verifyOrder {
                (viewHolder as DragListener).onDragEnd()
                dragEnd()
            }
        }

    }

    @Test
    fun `isLongPressDragEnabled returns true`() {
        val isLongPressDragEnabled = callback.isLongPressDragEnabled

        assertFalse(isLongPressDragEnabled)
    }


    private open class FakeViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
