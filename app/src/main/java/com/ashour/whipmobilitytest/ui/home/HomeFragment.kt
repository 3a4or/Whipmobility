package com.ashour.whipmobilitytest.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.*
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
        setHasOptionsMenu(true)
        initObserving()
        viewModel.getChartsData(IdManager.LAST_7_DAYS)
        return mBinding.root
    }

    private fun initObserving() {
        viewModel.result.observe(viewLifecycleOwner) {
            if (it != null && it.first.isNotEmpty() && it.second.isNotEmpty() && it.third.isNotEmpty()) {
                val data = BarData( getChartsDataSet())
                drawCharts(data)
            } else {
                showError(getString(R.string.error_no_charts))
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_scopes_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_All -> {
                viewModel.getChartsData(IdManager.ALL)
                true
            }
            R.id.action_today -> {
                viewModel.getChartsData(IdManager.TODAY)
                true
            }
            R.id.action_last_week -> {
                viewModel.getChartsData(IdManager.LAST_7_DAYS)
                true
            }
            R.id.action_last_month -> {
                viewModel.getChartsData(IdManager.LAST_30_DAYS)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}