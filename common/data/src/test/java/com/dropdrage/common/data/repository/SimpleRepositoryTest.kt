package com.dropdrage.common.data.repository

import com.dropdrage.common.domain.Resource
import com.dropdrage.test.util.mockLogE
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.coroutines.cancellation.CancellationException

internal class SimpleRepositoryTest {

    private lateinit var repository: FakeSimpleRepository<Any>


    @BeforeEach
    fun setUp() {
        repository = FakeSimpleRepository("tag")
    }


    @Test
    fun `simplyResourceWrap success`() {
        val data = Any()
        val result = repository.simplyResourceWrapFake { data }

        assertTrue(result is Resource.Success)
        assertEquals(data, (result as Resource.Success<Any>).data)
    }

    @Test
    fun `simplyResourceWrap throws CancellationException`() {
        assertThrows<CancellationException> { repository.simplyResourceWrapFake { throw CancellationException() } }
    }

    @Test
    fun `simplyResourceWrap throws Exception`() = mockLogE {
        val exception = Exception()

        val result = repository.simplyResourceWrapFake { throw exception }

        assertTrue(result is Resource.Error)
        assertEquals(exception, (result as Resource.Error).exception)
    }

}

private class FakeSimpleRepository<T>(tag: String) : SimpleRepository<T>(tag) {
    fun simplyResourceWrapFake(tryGetData: () -> T): Resource<T> = simplyResourceWrap(tryGetData)
}
