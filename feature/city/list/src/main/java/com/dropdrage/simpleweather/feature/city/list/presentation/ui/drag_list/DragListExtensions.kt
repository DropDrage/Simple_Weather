package com.dropdrage.simpleweatherfeature.city_list.presentation.ui.drag_list

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState

/*
    LazyListItemInfo.index is the item's absolute index in the list
    Based on the item's "relative position" with the "currently top" visible item,
    this returns LazyListItemInfo corresponding to it
*/
internal fun LazyListState.getVisibleItemInfoFor(absoluteIndex: Int): LazyListItemInfo? {
    return layoutInfo.visibleItemsInfo.getOrNull(absoluteIndex - layoutInfo.visibleItemsInfo.first().index)
}

/*
  Bottom offset of the element in Vertical list
*/
internal val LazyListItemInfo.offsetEnd: Int
    get() = offset + size