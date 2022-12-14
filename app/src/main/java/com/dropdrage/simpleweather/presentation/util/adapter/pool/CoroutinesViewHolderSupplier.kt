package com.dropdrage.simpleweather.presentation.util.adapter.pool

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CoroutinesViewHolderSupplier(
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
