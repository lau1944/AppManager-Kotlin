package com.vau.myappmanager.ui.pages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vau.myappmanager.R
import com.vau.myappmanager.databinding.AppListPageBinding
import com.vau.myappmanager.ui.adapter.AppAdapter
import com.vau.myappmanager.ui.viewmodels.AppsViewModel
import com.vau.myappmanager.utils.InjectUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppFragment : Fragment(R.layout.app_list_page) {
    private val model : AppsViewModel by viewModels {
        InjectUtil.provideAppsViewModelFactory(requireActivity().application)
    }
    val adapter =  AppAdapter()
    lateinit var binding : AppListPageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AppListPageBinding.inflate(layoutInflater)

        binding.appListProgress.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        binding.appListProgress.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.colorBackGround))
        binding.appListProgress.isEnabled = false
        binding.appListProgress.isRefreshing = true

        getAppList()

        //observe size
        model.applistSize.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val appcount = resources.getString(R.string.app_total) + " " + it
                Toast.makeText(context, appcount, Toast.LENGTH_LONG).show()
            }
        })


        return binding.root
    }

    private fun getAppList() {
        //Log.d("myAppCrash", model.hashCode().toString())
        model.getAppList().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.submitList(it)
                binding.appRecycler.adapter = adapter
                binding.appListProgress.isRefreshing = false
            }
        })
    }
}
