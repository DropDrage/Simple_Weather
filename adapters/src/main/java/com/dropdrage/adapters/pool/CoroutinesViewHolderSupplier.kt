package com.dropdrage.adapters.pool

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class CoroutinesViewHolderSupplier(
    context: Context,
    viewHolderProducer: ViewHolderProducer,
) : ViewHolderSupplier(context, viewHolderProducer), CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Default

    private val channelFlow = Channel<Int>()


    override fun start() {
        if (!coroutineContext.isActive) {
            throw IllegalStateException("Supplier is stopped and cannot be restarted. Create new one.")
        }

        launch {
            channelFlow.receiveAsFlow()
                .collect { viewType -> launch(Dispatchers.Default) { createItem(viewType) } }
        }
    }

    override fun enqueueItemCreation(viewType: Int) {
        launch { channelFlow.send(viewType) }
    }

    override fun stop() {
        cancel()
        channelFlow.cancel()
    }

}
