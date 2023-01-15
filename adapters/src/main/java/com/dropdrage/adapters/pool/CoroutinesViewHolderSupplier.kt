package com.dropdrage.adapters.pool

import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.coroutines.CoroutineContext

internal class CoroutinesViewHolderSupplier(
    context: Context,
    viewHolderProducer: ViewHolderProducer,
) : ViewHolderSupplier(context, viewHolderProducer), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Default

    private val channelFlow = Channel<Int>()


    override fun start() {
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
    }

}
