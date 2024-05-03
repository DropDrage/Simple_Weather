package com.dropdrage.simpleweather.data.util

import androidx.work.ListenableWorker.Result
import androidx.work.WorkManager
import com.dropdrage.common.test.util.coVerifyOnce
import com.dropdrage.common.test.util.justMock
import com.dropdrage.common.test.util.runTestWithMockLogE
import com.dropdrage.simpleweather.data.weather.repository.CacheRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
internal class CacheClearWorkerTest {

    @MockK(relaxed = true)
    lateinit var cacheRepository: CacheRepository

    private lateinit var worker: CacheClearWorker


    @BeforeEach
    fun setUp() {
        worker = CacheClearWorker(mockk(relaxed = true), mockk(relaxed = true), cacheRepository)
    }


    @Test
    fun create() {
    }

    @Nested
    inner class doWork {

        @Test
        fun `with cache`() = runTest {
            coEvery { cacheRepository.hasCache() } returns true

            val result = worker.doWork()

            coVerifyOnce {
                cacheRepository.clearOutdated()
                cacheRepository.hasCache()
            }
            assertEquals(Result.success(), result)
        }

        @Test
        fun `no cache`() = runTest {
            mockkStatic(WorkManager::class) {
                coEvery { cacheRepository.hasCache() } returns false
                val mockWorkManager = mockk<WorkManager> {
                    justMock { cancelWorkById(eq(worker.id)) }
                }
                every { WorkManager.getInstance(eq(worker.applicationContext)) } returns mockWorkManager

                val result = worker.doWork()

                coVerifyOnce {
                    cacheRepository.clearOutdated()
                    cacheRepository.hasCache()
                    mockWorkManager.cancelWorkById(eq(worker.id))
                }
                assertEquals(Result.success(), result)
            }
        }

        @Test
        fun `throws exception`() = runTestWithMockLogE {
            coEvery { cacheRepository.clearOutdated() } throws Exception()

            val result = worker.doWork()

            coVerifyOnce { cacheRepository.clearOutdated() }
            assertEquals(Result.failure(), result)
        }

    }

}