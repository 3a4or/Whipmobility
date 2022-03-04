package com.ashour.whipmobilitytest.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ashour.whipmobilitytest.CoroutineTestRule
import com.ashour.whipmobilitytest.data.entitities.*
import com.ashour.whipmobilitytest.data.network.BaseRepository
import com.ashour.whipmobilitytest.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class HomeViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var baseRepository: BaseRepository

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
        homeViewModel = HomeViewModel(baseRepository)
    }

    @Test
    fun getChartsData_successWithData() {
        runBlocking {
            val item = Item("2022-02-02", listOf(Value("jobs", 25), Value("services", 28)))
            val lineCharts = LineChart("LINE", "Total jobs and Services", listOf(item), "Jobs and Services")
           val analytics = Analytics(listOf(listOf(lineCharts)))
            val data = Data(analytics)
            val response = BaseResponse(201, Response(data, "Mock dashboard retrieved."))
            Mockito.`when`(baseRepository.getChartsData("All")).thenReturn(Result.Successful(response))
        }
        homeViewModel.getChartsData("All")
        Assert.assertEquals(homeViewModel.result.value!!.first.size, 1)
        Assert.assertEquals(homeViewModel.result.value!!.second.size, 1)
        Assert.assertEquals(homeViewModel.result.value!!.third.size, 1)
    }

    @Test
    fun getChartsData_successWithNoData() {
        runBlocking {
            val item = Item("2022-02-02", listOf(Value("jobs", 25), Value("services", 28)))
            val lineCharts = LineChart("LINE", "Total jobs and Services", listOf<Item>(), "Jobs and Services")
            val analytics = Analytics(listOf(listOf(lineCharts)))
            val data = Data(analytics)
            val response = BaseResponse(201, Response(data, "Mock dashboard retrieved."))
            Mockito.`when`(baseRepository.getChartsData("All")).thenReturn(Result.Successful(response))
        }
        homeViewModel.getChartsData("All")
        Assert.assertEquals(homeViewModel.result.value!!.first.size, 0)
        Assert.assertEquals(homeViewModel.result.value!!.second.size, 0)
        Assert.assertEquals(homeViewModel.result.value!!.third.size, 0)
    }

    @Test
    fun getChartsData_failBecauseOfAuthenticationError() {
        runBlocking {
            Mockito.`when`(baseRepository.getChartsData("All")).thenReturn(Result.AuthenticationError())
        }
        homeViewModel.getChartsData("All")
        val errorValue = homeViewModel.error.getOrAwaitValue()
        Assert.assertTrue(errorValue is Result.AuthenticationError)
    }

    @Test
    fun getChartsData_failBecauseOfServerError() {
        runBlocking {
            Mockito.`when`(baseRepository.getChartsData("All")).thenReturn(Result.ServerError())
        }
        homeViewModel.getChartsData("All")
        val errorValue = homeViewModel.error.getOrAwaitValue()
        Assert.assertTrue(errorValue is Result.ServerError)
    }
}