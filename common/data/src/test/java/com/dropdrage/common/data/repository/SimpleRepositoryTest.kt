package com.dropdrage.common.data.repository

import com.dropdrage.common.domain.Resource
import com.dropdrage.test.util.assertInstanceOf
import com.dropdrage.test.util.mockLogE
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.coroutines.cancellation.CancellationException

internal class SimpleRepositoryTest {

    private lateinit var repository: FakeSimpleRepository<Any>


    @BeforeEach
    fun setUp() {
        repository = FakeSimpleRepository("tag")
    }


    @Nested
    inner class simplyResourceWrap {

        @Test
        fun success() {
            val data = Any()
            val result = repository.simplyResourceWrapFake { data }

            assertInstanceOf<Resource.Success<Any>>(result)
            assertEquals(data, result.data)
        }

        @Test
        fun `throws CancellationException`() {
            assertThrows<CancellationException> { repository.simplyResourceWrapFake { throw CancellationException() } }
        }

        @Test
        fun `throws Exception`() = mockLogE {
            val exception = Exception()

            val result = repository.simplyResourceWrapFake { throw exception }

            assertInstanceOf<Resource.Error<Any>>(result)
            assertEquals(exception, result.exception)
        }

    }


    private class FakeSimpleRepository<T>(tag: String) : SimpleRepository<T>(tag) {
        fun simplyResourceWrapFake(tryGetData: () -> T): Resource<T> = simplyResourceWrap(tryGetData)
    }

}
