package com.vau.myappmanager

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.vau.myappmanager.databinding.ActivityMainBinding
import com.vau.myappmanager.ui.adapter.PagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.vau.myappmanager.objects.AppbarState
import com.vau.myappmanager.ui.pages.AppFragment
import com.vau.myappmanager.ui.pages.LikeAppFragment
import com.vau.myappmanager.ui.pages.SystemAppFragment
import com.vau.myappmanager.ui.pages.SettingPage
import com.vau.myappmanager.ui.viewmodels.AppsViewModel
import com.vau.myappmanager.ui.viewmodels.MainActivityModel
import com.vau.myappmanager.utils.InjectUtil
import com.vau.myappmanager.utils.SharedPreferenceHelper

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var model : MainActivityModel
    val listIcon = intArrayOf(R.drawable.apk, R.drawable.android,
        R.drawable.like)
    lateinit var appViewModel : AppsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(SharedPreferenceHelper.getDarkSetting(this))

        model = ViewModelProvider(this).get(MainActivityModel::class.java)
        appViewModel = ViewModelProvider(this, InjectUtil.provideAppsViewModelFactory(application))
            .get(AppsViewModel::class.java)

        checkPermission(
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        //binding.appsAdview.visibility = View.VISIBLE
        binding.appsAdview.loadAd(adRequest)

        binding.appBarNormalState.menu.setOnClickListener {
            startActivity(Intent(this, SettingPage::class.java))
        }

        //Log.d("myAppCrash", appViewModel.hashCode().toString())
        //search app
        binding.appBarSearchState.appSearch.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.d("myAppCrash", p0.toString())
                    appViewModel.getFilteredApps(p0.toString())

            }
        })


        model.state.observe(this, Observer {
            if (it != null) {
                when (it) {
                    is AppbarState.normal -> {
                        appBarNormal()
                    }
                    is AppbarState.search -> {
                        appbarSearch()
                    }
                }
            }
        })

        setUpAdapter()

        binding.appBarNormalState.searchApp.setOnClickListener {
            model.stateMutable.postValue(AppbarState.search())
        }

        binding.appBarSearchState.cancelSearch.setOnClickListener {
            binding.appBarSearchState.appSearch.text.clear()
            model.stateMutable.postValue(AppbarState.normal())
        }
    }


    private fun setUpAdapter() {
        val fm = supportFragmentManager

        val adapter = PagerAdapter(fm)
        adapter.addFragment(AppFragment())
        adapter.addFragment(SystemAppFragment())
        adapter.addFragment(LikeAppFragment())

        binding.selectViewpager.adapter = adapter
        binding.selectTabs.setupWithViewPager(binding.selectViewpager)

        binding.selectViewpager.currentItem = 0
        binding.selectTabs.setupWithViewPager(binding.selectViewpager)
        binding.selectTabs.tabGravity = TabLayout.GRAVITY_FILL

        setUpIcon()
    }

    private fun setUpIcon() {
        for (i in 0 until binding.selectTabs.tabCount)
            binding.selectTabs.getTabAt(i)!!.setIcon(listIcon[i])
    }

    private fun appBarNormal() {
        binding.appBarNormalState.appBarNormalState.visibility = View.VISIBLE
        binding.appBarSearchState.appBarSearchState.visibility = View.GONE
    }

    private fun appbarSearch() {
        binding.appBarNormalState.appBarNormalState.visibility = View.GONE
        binding.appBarSearchState.appBarSearchState.visibility = View.VISIBLE
    }

    private fun checkPermission(permission: Array<out String>, requestCode: Int) {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   if (checkSelfPermission(
                           android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                       PackageManager.PERMISSION_GRANTED
                   ) {
                       //do thing
                   } else {
                       requestPermissions(permission, requestCode)
                   }

            } else {
               startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                   data = Uri.fromParts("package", packageName, null)
               })
                Toast.makeText(this, R.string.turn_on_permission, Toast.LENGTH_LONG).show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                 //granted
            }  else {
                //not granted
            }
        }
    }

    companion object {
        val STORAGE_REQUEST_CODE = 1234
    }

}
