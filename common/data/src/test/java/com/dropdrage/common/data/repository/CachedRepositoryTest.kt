package com.dropdrage.common.data.repository

import android.util.Log
import com.dropdrage.common.data.LocalResource
import com.dropdrage.common.domain.Resource
import io.mockk.every
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
internal class CachedRepositoryTest {

    private lateinit var repository: FakeCachedRepository<Any>


    @BeforeEach
    fun setUp() {
        repository = FakeCachedRepository("tag")
    }


    @Test
    fun `processOrEmit success`() = runTest {
        val data = Any()
        val result = repository.tryProcessRemoteResourceOrEmitErrorFake(LocalResource.NotFound<Any>()) {
            emit(Resource.Success(data))
        }.first()

        assertTrue(result is Resource.Success)
        assertEquals(data, (result as Resource.Success<Any>).data)
    }

    @Test
    fun `processOrEmit error Exception`() = runTest {
        val exception = Exception()
        val result = repository.tryProcessRemoteResourceOrEmitErrorFake(LocalResource.NotFound<Any>()) {
            emit(Resource.Error(exception))
        }.first()

        assertTrue(result is Resource.Error)
        assertEquals(exception, (result as Resource.Error).exception)
    }

    @Test
    fun `processOrEmit error CancellationException`() = runTest {
        val exception = CancellationException()
        val result = repository.tryProcessRemoteResourceOrEmitErrorFake(LocalResource.NotFound<Any>()) {
            emit(Resource.Error(exception))
        }.first()

        assertTrue(result is Resource.Error)
        assertEquals(exception, (result as Resource.Error).exception)
    }


    @Test
    fun `processOrEmit throw IOException with LocalResource Success`() = runTest {
        mockkStatic(Log::class) {
            every { Log.e(any(), any(), any()) } returns 0

            val exception = IOException()
            val result = repository.tryProcessRemoteResourceOrEmitErrorFake(LocalResource.Success(Any())) {
                throw exception
            }.firstOrNull()

            assertNull(result)
        }
    }

    @Test
    fun `processOrEmit throw IOException with LocalResource NotFound`() = runTest {
        mockkStatic(Log::class) {
            every { Log.e(any(), any(), any()) } returns 0

            val exception = IOException()
            val result = repository.tryProcessRemoteResourceOrEmitErrorFake(LocalResource.NotFound<Any>()) {
                throw exception
            }.first()

            assertTrue(result is Resource.CantObtainResource)
        }
    }

    @Test
    fun `processOrEmit throw CancellationException`() = runTest {
        val exception = CancellationException()

        assertThrows<CancellationException> {
            repository.tryProcessRemoteResourceOrEmitErrorFake(LocalResource.NotFound<Any>()) {
                throw exception
            }.first()
        }
    }

    @Test
    fun `processOrEmit throw Exception with LocalResource NotFound`() = runTest {
        mockkStatic(Log::class) {
            every { Log.e(any(), any(), any()) } returns 0

            val exception = Exception()
            val result = repository.tryProcessRemoteResourceOrEmitErrorFake(LocalResource.Success(Any())) {
                throw exception
            }.firstOrNull()

            assertNull(result)
        }
    }

    @Test
    fun `processOrEmit throw Exception with LocalResource not Success`() = runTest {
        mockkStatic(Log::class) {
            every { Log.e(any(), any(), any()) } returns 0

            val exception = Exception()
            val result = repository.tryProcessRemoteResourceOrEmitErrorFake(LocalResource.NotFound<Any>()) {
                throw exception
            }.first()

            assertTrue(result is Resource.Error)
            assertEquals(exception, (result as Resource.Error).exception)
        }
    }

}

private class FakeCachedRepository<T>(tag: String) : CachedRepository<T>(tag) {
    suspend fun tryProcessRemoteResourceOrEmitErrorFake(
        localResourceResult: LocalResource<*>,
        remoteResourceAction: suspend FlowCollector<Resource<T>>.() -> Unit,
    ) = flow { tryProcessRemoteResourceOrEmitError<T>(localResourceResult, remoteResourceAction) }
}
