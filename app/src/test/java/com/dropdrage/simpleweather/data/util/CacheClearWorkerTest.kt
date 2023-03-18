package com.dropdrage.simpleweather.data.util

import androidx.work.ListenableWorker.*
import androidx.work.WorkManager
import com.dropdrage.simpleweather.data.weather.repository.CacheRepository
import com.dropdrage.test.util.runTestWithMockLogE
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
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

    @Test
    fun `doWork with cache`() = runTest {
        coEvery { cacheRepository.hasCache() } returns true

        val result = worker.doWork()

        assertEquals(Result.success(), result)
        coVerify { cacheRepository.clearOutdated() }
    }

    @Test
    fun `doWork no cache`() = runTest {
        coEvery { cacheRepository.hasCache() } returns false
        mockkStatic(WorkManager::class)
        val mockWorkManager = mockk<WorkManager>(relaxed = true)
        every { WorkManager.getInstance(eq(worker.applicationContext)) } returns mockWorkManager

        val result = worker.doWork()

        assertEquals(Result.success(), result)
        coVerify { mockWorkManager.cancelWorkById(eq(worker.id)) }
        coVerify { cacheRepository.clearOutdated() }

        unmockkStatic(WorkManager::class)
    }

    @Test
    fun `doWork throws exception`() = runTestWithMockLogE {
        coEvery { cacheRepository.clearOutdated() } throws Exception()

        val result = worker.doWork()

        assertEquals(Result.failure(), result)
    }

}