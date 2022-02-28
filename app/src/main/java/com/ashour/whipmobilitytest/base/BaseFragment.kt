package com.ashour.whipmobilitytest.base

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.ashour.whipmobilitytest.R
import com.google.android.material.snackbar.Snackbar
import com.ashour.whipmobilitytest.data.entitities.Result

abstract class BaseFragment : Fragment()  {

    val navController: NavController by lazy {
        return@lazy (requireActivity() as MainActivity).navController
    }
    private lateinit var mBaseViewModel: BaseViewModel

    protected fun setBaseViewModel(baseViewModel: BaseViewModel) {
        mBaseViewModel = baseViewModel
        baseViewModel.dataLoading.observe(this) {
            showLoading(it)
        }

        baseViewModel.error.observe(this) {
            when (it) {
                is Result.NetworkError -> showError(getString(R.string.error_network))
                is Result.ServerError -> showError(getString(R.string.error_connection_failed))
                is Result.ClientError -> showError("Message that should return from webservice response")
                is Result.AuthenticationError -> showError(getString(R.string.error_unauthorized_user))
                else -> showError(getString(R.string.error_connection_failed))
            }
        }
    }

    private fun showLoading(show: Boolean) {
        val base = requireActivity()
        if (base is MainActivity) {
            base.showHideProgress(show)
        }
    }

    protected open fun showError(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.color_filter4))
            view.background =
                ResourcesCompat.getDrawable(resources, R.drawable.round_corner_bg, null)
            setActionTextColor(Color.WHITE)
            setAction(R.string.label_dismiss) { dismiss() }
        }.show()
    }

}