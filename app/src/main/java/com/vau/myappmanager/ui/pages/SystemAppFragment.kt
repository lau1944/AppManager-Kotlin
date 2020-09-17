package com.vau.myappmanager.ui.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vau.myappmanager.R
import com.vau.myappmanager.databinding.AppListPageBinding
import com.vau.myappmanager.ui.adapter.AppAdapter
import com.vau.myappmanager.ui.viewmodels.AppsViewModel
import com.vau.myappmanager.utils.InjectUtil

class SystemAppFragment : Fragment(R.layout.app_list_page) {
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

        binding.appListProgress.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.colorBackGround))
        binding.appListProgress.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        binding.appListProgress.isEnabled = false
        binding.appListProgress.isRefreshing = true

        model.getSystemAppList().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.submitList(it)
                binding.appRecycler.adapter = adapter
                binding.appListProgress.isRefreshing = false
            }
        })


        return binding.root
    }
}