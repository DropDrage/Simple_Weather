package com.dropdrage.adapters.pool

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import java.lang.Integer.max

private const val DEFAULT_VIEW_TYPE = 0

class PrefetchPlainViewPool internal constructor(
    private val defaultMaxRecycledViews: Int,
    private val viewHolderSupplier: ViewHolderSupplier,
) : RecyclerView.RecycledViewPool() {

    private var maxRecycledViews: Int = defaultMaxRecycledViews


    init {
        viewHolderSupplier.viewHolderConsumer = ::putViewFromSupplier
        viewHolderSupplier.start()
    }

    fun prefetch(count: Int) {
        maxRecycledViews = max(defaultMaxRecycledViews, count)
        viewHolderSupplier.prefetch(DEFAULT_VIEW_TYPE, count)
    }

    override fun putRecycledView(scrap: RecyclerView.ViewHolder) {
        val viewType = scrap.itemViewType
        val maxRecycledViews = maxRecycledViews
        setMaxRecycledViews(viewType, maxRecycledViews)
        super.putRecycledView(scrap)
    }

    override fun getRecycledView(viewType: Int): RecyclerView.ViewHolder? {
        val holder = super.getRecycledView(viewType)
        if (holder == null) viewHolderSupplier.onItemCreatedOutside(viewType)
        return holder
    }

    override fun clear() {
        super.clear()
        viewHolderSupplier.stop()
    }

    private fun putViewFromSupplier(scrap: RecyclerView.ViewHolder) {
        putRecycledView(scrap)
    }


    companion object {
        const val DEFAULT_MAX_RECYCLED_VIEWS = 5


        fun createPrefetchedWithCoroutineSupplier(
            context: Context,
            viewHolderProducer: ViewHolderProducer,
            defaultMaxRecycledViews: Int = DEFAULT_MAX_RECYCLED_VIEWS,
            prefetchedCount: Int = defaultMaxRecycledViews,
        ): PrefetchPlainViewPool {
            val viewHolderSupplier = CoroutinesViewHolderSupplier(context, viewHolderProducer)
            return PrefetchPlainViewPool(defaultMaxRecycledViews, viewHolderSupplier).apply {
                prefetch(prefetchedCount)
            }
        }
    }

}
