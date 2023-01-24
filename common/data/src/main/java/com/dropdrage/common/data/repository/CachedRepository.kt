package com.dropdrage.common.data.repository

import android.util.Log
import com.dropdrage.common.data.LocalResource
import com.dropdrage.common.domain.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.FlowCollector
import java.io.IOException

abstract class CachedRepository(protected val tag: String) {
    protected suspend inline fun <T> FlowCollector<Resource<T>>.tryProcessRemoteResourceOrEmitError(
        localResourceResult: LocalResource<*>,
        remoteResourceAction: () -> Unit,
    ) {
        try {
            remoteResourceAction()
        } catch (e: IOException) {
            Log.e(tag, e.message, e)
            if (localResourceResult !is LocalResource.Success) {
                emit(Resource.CantObtainResource())
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(tag, e.message, e)
            if (localResourceResult !is LocalResource.Success) {
                emit(Resource.Error(e))
            }
        }
    }
}
