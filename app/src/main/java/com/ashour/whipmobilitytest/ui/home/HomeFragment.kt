package com.ashour.whipmobilitytest.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ashour.whipmobilitytest.R
import com.ashour.whipmobilitytest.base.BaseFragment
import com.ashour.whipmobilitytest.databinding.FragmentHomeBinding
import com.ashour.whipmobilitytest.utils.IdManager
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setBaseViewModel(viewModel)
        mBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = viewModel
        initObserving()
        viewModel.getChartsData(IdManager.ALL)
        return mBinding.root
    }

    private fun initObserving() {
        viewModel.result.observe(viewLifecycleOwner) {
            if (it != null && it.first.isNotEmpty() && it.second.isNotEmpty() && it.third.isNotEmpty()) {
                val data = BarData( getChartsDataSet())
                drawCharts(data)
            }
        }
    }

    private fun drawCharts(barData: BarData) {
        mBinding.barchart.data = barData
        mBinding.barchart.animateXY(50, 50)
        mBinding.barchart.invalidate()
    }

    private fun getChartsDataSet() : ArrayList<IBarDataSet> {
        val dataSets = ArrayList<IBarDataSet>()
        // set first charts
        val valueSet1 = ArrayList<BarEntry>()
        for (job in viewModel.result.value!!.second) {
            val jobValue = BarEntry(job.toFloat(), viewModel.result.value!!.second.indexOf(job).toFloat())
            valueSet1.add(jobValue)
        }
        // set second charts
        val valueSet2 = ArrayList<BarEntry>()
        for (service in viewModel.result.value!!.third) {
            val serviceValue = BarEntry(service.toFloat(), viewModel.result.value!!.third.indexOf(service).toFloat())
            valueSet2.add(serviceValue)
        }
        // setting style of the charts
        val barDataSet1 = BarDataSet(valueSet1, getString(R.string.label_jobs))
        barDataSet1.color = Color.rgb(0, 155, 0)
        val barDataSet2 = BarDataSet(valueSet2, getString(R.string.label_services))
        barDataSet2.colors = ColorTemplate.COLORFUL_COLORS.toList()
        // add both charts to the main charts list
        dataSets.add(barDataSet1)
        dataSets.add(barDataSet2)
        return dataSets
    }
}