package com.dropdrage.common.data.repository

import com.dropdrage.common.data.LocalResource
import com.dropdrage.common.domain.Resource
import com.dropdrage.test.util.assertInstanceOf
import com.dropdrage.test.util.runTestWithMockLogE
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
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


    @Nested
    inner class processOrEmit {

        @Test
        fun success() = runTest {
            val data = Any()
            val result = repository.tryProcessRemoteResourceOrEmitErrorExposed(LocalResource.NotFound<Any>()) {
                emit(Resource.Success(data))
            }.first()

            assertInstanceOf<Resource.Success<Any>>(result)
            assertEquals(data, result.data)
        }

        @Test
        fun `error Exception`() = runTest {
            val exception = Exception()
            val result = repository.tryProcessRemoteResourceOrEmitErrorExposed(LocalResource.NotFound<Any>()) {
                emit(Resource.Error(exception))
            }.first()

            assertInstanceOf<Resource.Error<Any>>(result)
            assertEquals(exception, result.exception)
        }

        @Test
        fun `error CancellationException`() = runTest {
            val exception = CancellationException()
            val result = repository.tryProcessRemoteResourceOrEmitErrorExposed(LocalResource.NotFound<Any>()) {
                emit(Resource.Error(exception))
            }.first()

            assertInstanceOf<Resource.Error<Any>>(result)
            assertEquals(exception, result.exception)
        }


        @Test
        fun `throw IOException with LocalResource Success`() = runTestWithMockLogE {
            val exception = IOException()
            val result = repository.tryProcessRemoteResourceOrEmitErrorExposed(LocalResource.Success(Any())) {
                throw exception
            }.firstOrNull()

            assertNull(result)
        }

        @Test
        fun `throw IOException with LocalResource NotFound`() = runTestWithMockLogE {
            val exception = IOException()
            val result = repository.tryProcessRemoteResourceOrEmitErrorExposed(LocalResource.NotFound<Any>()) {
                throw exception
            }.first()

            assertInstanceOf<Resource.CantObtainResource<Any>>(result)
        }

        @Test
        fun `throw CancellationException`() = runTest {
            val exception = CancellationException()

            assertThrows<CancellationException> {
                repository.tryProcessRemoteResourceOrEmitErrorExposed(LocalResource.NotFound<Any>()) {
                    throw exception
                }.first()
            }
        }

        @Test
        fun `throw Exception with LocalResource NotFound`() = runTestWithMockLogE {
            val exception = Exception()
            val result = repository.tryProcessRemoteResourceOrEmitErrorExposed(LocalResource.Success(Any())) {
                throw exception
            }.firstOrNull()

            assertNull(result)
        }

        @Test
        fun `throw Exception with LocalResource not Success`() = runTestWithMockLogE {
            val exception = Exception()
            val result = repository.tryProcessRemoteResourceOrEmitErrorExposed(LocalResource.NotFound<Any>()) {
                throw exception
            }.first()

            assertInstanceOf<Resource.Error<Any>>(result)
            assertEquals(exception, result.exception)
        }

    }


    private class FakeCachedRepository<T>(tag: String) : CachedRepository<T>(tag) {
        suspend fun tryProcessRemoteResourceOrEmitErrorExposed(
            localResourceResult: LocalResource<*>,
            remoteResourceAction: suspend FlowCollector<Resource<T>>.() -> Unit,
        ) = flow { tryProcessRemoteResourceOrEmitError(localResourceResult, remoteResourceAction) }
    }

}
