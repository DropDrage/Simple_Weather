package com.dropdrage.simpleweather.presentation.util.adapter.pool

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicInteger

typealias ViewHolderProducer = (parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder
typealias ViewHolderConsumer = (viewHolder: RecyclerView.ViewHolder) -> Unit

private const val TAG = "ViewHolderSupplier"

abstract class ViewHolderSupplier(context: Context, private val viewHolderProducer: ViewHolderProducer) {

    private val itemsCreated = AtomicInteger(0)
    private val itemsQueued = AtomicInteger(0)

    internal lateinit var viewHolderConsumer: ViewHolderConsumer

    private val fakeParent: ViewGroup by lazy { FrameLayout(context) }
    private val mainHandler = Handler(Looper.getMainLooper())


    abstract fun start()

    abstract fun enqueueItemCreation(viewType: Int)

    abstract fun stop()


    protected fun createItem(viewType: Int) {
        val created = itemsCreated.getAndAdd(1)
        val queued = itemsQueued.get()
        if (created > queued) return

        val holder: RecyclerView.ViewHolder

        try {
            holder = viewHolderProducer(fakeParent, viewType)
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
            return
        }

        itemsCreated.incrementAndGet()

        mainHandler.postAtFrontOfQueue { viewHolderConsumer(holder) }
    }

    internal fun prefetch(viewType: Int, count: Int) {
        if (itemsQueued.get() >= count) return
        itemsQueued.set(count)

        val created = itemsCreated.get()
        if (created >= count) return

        repeat(count - created) { enqueueItemCreation(viewType) }
    }

    internal fun onItemCreatedOutside() {
        Log.d(TAG, "Item Created Outside: ${itemsCreated.incrementAndGet()}")
    }

}
