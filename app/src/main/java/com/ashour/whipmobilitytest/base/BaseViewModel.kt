package com.ashour.whipmobilitytest.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ashour.whipmobilitytest.data.entitities.Result

open class BaseViewModel : ViewModel()  {

    val dataLoading = MutableLiveData(false)

    var error = MutableLiveData<Result.BaseError>()
}