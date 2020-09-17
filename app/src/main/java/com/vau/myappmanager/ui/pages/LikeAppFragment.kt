package com.vau.myappmanager.ui.pages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vau.myappmanager.R
import com.vau.myappmanager.databinding.AppListPageBinding
import com.vau.myappmanager.ui.adapter.LikeAppAdapter
import com.vau.myappmanager.ui.viewmodels.AppsViewModel
import com.vau.myappmanager.ui.viewmodels.LikeAppViewModel
import com.vau.myappmanager.utils.InjectUtil

class LikeAppFragment : Fragment(R.layout.app_list_page) {
    lateinit var binding : AppListPageBinding
    lateinit var model : LikeAppViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AppListPageBinding.inflate(layoutInflater)

        model = ViewModelProvider(this, InjectUtil.provideLikeAppViewModelFactory(requireContext()))
            .get(LikeAppViewModel::class.java)


        binding.appListProgress.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.colorBackGround))
        binding.appListProgress.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        binding.appListProgress.isRefreshing = true

        binding.appListProgress.setOnRefreshListener {
            if (binding.appListProgress.isRefreshing) {
                binding.appListProgress.isRefreshing = false
                model.getLikeApp()
            }
        }

        getLike()

        return binding.root
    }

    private fun getLike() {
        val adapter = LikeAppAdapter()
        model.LikedAppList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d("myAppCrash", it.size.toString())
                adapter.submitList(it)
                binding.appRecycler.adapter = adapter
                binding.appListProgress.isRefreshing = false
            }
        })
    }


    override fun onResume() {
        super.onResume()
        model.getLikeApp()
    }

}