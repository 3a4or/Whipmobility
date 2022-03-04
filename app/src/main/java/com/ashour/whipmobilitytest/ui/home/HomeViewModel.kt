package com.ashour.whipmobilitytest.ui.home

import androidx.lifecycle.viewModelScope
import com.ashour.whipmobilitytest.base.BaseViewModel
import com.ashour.whipmobilitytest.data.entitities.LineChart
import com.ashour.whipmobilitytest.data.network.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.ashour.whipmobilitytest.data.entitities.Result
import com.ashour.whipmobilitytest.utils.SingleLiveEvent

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: BaseRepository) : BaseViewModel() {

    val result = SingleLiveEvent<Triple<ArrayList<String>, ArrayList<Int>, ArrayList<Int>>>()

    fun getChartsData(scope: String) {
        viewModelScope.launch {
            dataLoading.value = true
            when (val response = repo.getChartsData(scope)) {
                is Result.Successful -> {
                    val lineChartsObject: LineChart = response.data.response.data.analytics.lineCharts[0][0]
                    val keys = ArrayList<String>()
                    val jobs = ArrayList<Int>()
                    val services = ArrayList<Int>()
                    for (item in lineChartsObject.items) {
                        keys.add(item.key)
                        jobs.add(item.value[0].value)
                        services.add(item.value[1].value)
                    }
                    result.value = Triple(keys, jobs, services)
                }
                is Result.BaseError -> error.value = response
            }
            dataLoading.value = false
        }
    }
}