package com.dropdrage.simpleweather.city_list.presentation.presentation.ui

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.dropdrage.simpleweather.city_list.presentation.presentation.ui.drag_list.SameEquatable
import com.dropdrage.simpleweather.city_list.presentation.presentation.ui.drag_list.rememberDragDropListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val ON_DRAG_ALPHA = 0.5f
private const val IDLE_ALPHA = 1f

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun <T : SameEquatable<T>> DragDropList(
    items: List<T>,
    onMove: (from: Int, to: Int) -> Unit,
    onDragEnd: () -> Unit,
    keyProducer: (item: T) -> Any,
    modifier: Modifier = Modifier,
    item: @Composable LazyItemScope.(
        item: T,
        isDragging: Boolean,
        modifier: Modifier, draggableModifier: Modifier,
    ) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var overscrollJob by remember { mutableStateOf<Job?>(null) }
    val dragDropListState = rememberDragDropListState(onMove = onMove, onDragEnd = onDragEnd)

    LazyColumn(
        state = dragDropListState.lazyListState,
        modifier = modifier
    ) {
        items(items = items, key = keyProducer) { item ->
            var isDragging by remember {
                mutableStateOf(false)
            }

            val offsetAnimation by animateFloatAsState(
                if (isDragging) dragDropListState.elementDisplacement!! else 0f
            )

            item(
                item = item,
                isDragging = isDragging,
                modifier = Modifier.composed {
                    graphicsLayer {
                        translationY = if (isDragging) dragDropListState.elementDisplacement!! else offsetAnimation
                        alpha = if (isDragging) ON_DRAG_ALPHA else IDLE_ALPHA
                    }
                },
                draggableModifier = Modifier
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragStart = {
                                dragDropListState.onDragStart(items.indexOfFirst { it.isSame(item) })
                                isDragging = true
                            },
                            onVerticalDrag = { change, offset ->
                                change.consume()
                                dragDropListState.onDrag(offset)

                                if (overscrollJob?.isActive == true) return@detectVerticalDragGestures

                                dragDropListState
                                    .checkForOverScroll()
                                    .takeIf { it != 0f }
                                    ?.let {
                                        overscrollJob = scope.launch {
                                            dragDropListState.lazyListState.scrollBy(it)
                                        }
                                    }
                                    ?: run { overscrollJob?.cancel() }
                            },
                            onDragEnd = {
                                dragDropListState.onDragInterrupted()
                                isDragging = false
                            },
                            onDragCancel = {
                                dragDropListState.onDragInterrupted()
                                isDragging = false
                            }
                        )
                    }
                    .pointerInteropFilter { it.action == MotionEvent.ACTION_DOWN },
            )
        }
    }
}
