package com.dropdrage.simpleweather.city_list.presentation.presentation.ui.drag_list

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

@Composable
internal fun rememberDragDropListState(
    lazyListState: LazyListState = rememberLazyListState(),
    onMove: (Int, Int) -> Unit,
    onDragEnd: () -> Unit,
): DragDropListState = remember {
    DragDropListState(lazyListState = lazyListState, onMove = onMove, onDragEnd = onDragEnd)
}

internal class DragDropListState(
    val lazyListState: LazyListState,
    private val onMove: (Int, Int) -> Unit,
    private val onDragEnd: () -> Unit,
) {

    private var draggedDistance by mutableStateOf(0f)

    // used to obtain initial offsets on drag start
    private var initiallyDraggedElement by mutableStateOf<LazyListItemInfo?>(null)

    private val initialOffsets: Pair<Int, Int>?
        get() = initiallyDraggedElement?.let { Pair(it.offset, it.offsetEnd) }

    var currentIndexOfDraggedItem by mutableStateOf<Int?>(null)
        private set

    private val currentElement: LazyListItemInfo?
        get() = currentIndexOfDraggedItem?.let {
            lazyListState.getVisibleItemInfoFor(absoluteIndex = it)
        }

    val elementDisplacement: Float?
        get() = currentIndexOfDraggedItem
            ?.let { lazyListState.getVisibleItemInfoFor(absoluteIndex = it) }
            ?.let { item -> (initiallyDraggedElement?.offset ?: 0f).toFloat() + draggedDistance - item.offset }


    fun onDragStart(itemIndex: Int) {
        currentIndexOfDraggedItem = itemIndex
        initiallyDraggedElement = lazyListState.layoutInfo.visibleItemsInfo[itemIndex]
    }

    fun onDragStart(offset: Offset) {
        val offsetY = offset.y.toInt()
        lazyListState.layoutInfo.visibleItemsInfo
            .firstOrNull { item -> offsetY in item.offset..item.offsetEnd }
            ?.also {
                currentIndexOfDraggedItem = it.index
                initiallyDraggedElement = it
            }
    }

    fun onDrag(offset: Float) {
        draggedDistance += offset

        initialOffsets?.let { (topOffset, bottomOffset) ->
            val startOffset = topOffset + draggedDistance
            val endOffset = bottomOffset + draggedDistance

            currentElement?.let { hovered ->
                lazyListState.layoutInfo.visibleItemsInfo
                    .filterNot { item ->
                        item.offsetEnd < startOffset || item.offset > endOffset || hovered.index == item.index
                    }
                    .firstOrNull { item ->
                        val delta = startOffset - hovered.offset

                        if (delta > 0) endOffset > item.offsetEnd
                        else startOffset < item.offset
                    }
                    ?.also { item ->
                        currentIndexOfDraggedItem?.let { current -> onMove.invoke(current, item.index) }
                        currentIndexOfDraggedItem = item.index
                    }
            }
        }
    }

    fun checkForOverScroll(): Float = initiallyDraggedElement?.let {
        val startOffset = it.offset + draggedDistance
        val endOffset = it.offsetEnd + draggedDistance
        val layoutInfo = lazyListState.layoutInfo

        when {
            draggedDistance < 0 -> (startOffset - layoutInfo.viewportStartOffset).takeIf { diff -> diff < 0 }
            draggedDistance > 0 -> (endOffset - layoutInfo.viewportEndOffset).takeIf { diff -> diff > 0 }
            else -> null
        }
    } ?: 0f

    fun onDragInterrupted() {
        draggedDistance = 0f
        currentIndexOfDraggedItem = null
        initiallyDraggedElement = null
        onDragEnd()
    }

}
