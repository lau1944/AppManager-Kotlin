package com.vau.myappmanager.ui.pages

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.daasuu.ei.BuildConfig
import com.vau.myappmanager.R
import com.vau.myappmanager.databinding.ActivitySettingPageBinding
import com.vau.myappmanager.databinding.ExtractAppBinding
import com.vau.myappmanager.databinding.SimpleEdittextBinding
import com.vau.myappmanager.objects.ExtractApp
import com.vau.myappmanager.ui.adapter.ExtractedAppAdapter
import com.vau.myappmanager.ui.viewmodels.FileViewModel
import com.vau.myappmanager.utils.InjectUtil
import com.vau.myappmanager.utils.SharedPreferenceHelper
import java.lang.Exception

class SettingPage : AppCompatActivity() {
    lateinit var binding : ActivitySettingPageBinding
    lateinit var model : FileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this, InjectUtil.provideFileViewModelFactory(this))
            .get(FileViewModel::class.java)


        binding.darkMode.setOnClickListener {
            binding.darkSwitch.performClick()
        }

        model.extractAppSize.observe(this, Observer {
            if (it != null) {
                binding.apkCount.text = it
            }
        })

        binding.fileLocation.setOnClickListener {
            folderDialog()
        }

        //detect ui mode
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.darkSwitch.isChecked = true
            } // Night mode is active, we're using dark theme
        }

        binding.darkSwitch.setOnCheckedChangeListener { p0, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            SharedPreferenceHelper.setDarkSetting(this, isChecked)
        }

        binding.back.setOnClickListener { finish() }

        binding.openAppMarket.setOnClickListener {
            openLink("com.vau.apphunt")
        }

        binding.sendFeedback.setOnClickListener {
            openLink(BuildConfig.APPLICATION_ID)
        }

        binding.fileApps.setOnClickListener { setExtractDialog() }

        binding.clearCache.setOnClickListener {
            model.clearCache(this)
            Toast.makeText(this, R.string.cache_be_clear, Toast.LENGTH_LONG).show()
        }
    }

    private fun setInfo() {
        model.getExtractApp()
        binding.foldLocation.text = model.getLocation(this)
        Log.d("myAppCrash", model.getLocation(this))
    }

    private fun openLink(link: String) {
        try {
            val uri =
                    Uri.parse("https://play.google.com/store/apps/details?id=${link}")
                val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
                try {
                    startActivity(myAppLinkToMarket)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        this,
                        "Impossible to open link",
                        Toast.LENGTH_LONG
                    ).show()
                }
        } catch (e: Exception) {}
    }

    private fun setExtractDialog() {
        val extractBinding = ExtractAppBinding.inflate(getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        val popupDialog = AlertDialog.Builder(this)
            .setView(extractBinding.root)
            .setPositiveButton(resources.getString(R.string.done),
            DialogInterface.OnClickListener { dialogInterface, i ->

            })

        model.extractApp.observe(this, Observer {
            if (it != null) {
                val adapter = ExtractedAppAdapter(it as ArrayList<ExtractApp>)
                adapter.submitList(it)
                extractBinding.extractAppRecycler.adapter = adapter
                //Log.d("myAppCrashSize", it.size.toString())
            }
        })

        popupDialog.create().show()
    }

    private fun folderDialog() {
        val folderBinding = SimpleEdittextBinding
            .inflate(getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        folderBinding.folderEdit.setText(binding.foldLocation.text.toString()
            .replace(Environment.getExternalStorageDirectory().toString()+"/", ""))

        val popupDialog = AlertDialog.Builder(this)
            .setView(folderBinding.root)
            .setTitle(resources.getString(R.string.folder_location))
            .setPositiveButton(resources.getString(R.string.done),
                DialogInterface.OnClickListener { dialogInterface, i ->
                        model.setFolderLocation(this, folderBinding.folderEdit.text.toString())
                        setInfo()
                        binding.foldLocation.text = folderBinding.folderEdit.text.toString()
                        dialogInterface.dismiss()
                        Toast.makeText(this, R.string.folder_path_updated, Toast.LENGTH_LONG).show()
                })
            .setNegativeButton(resources.getString(R.string.no),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })

        popupDialog.create().show()
    }

    override fun onResume() {
        super.onResume()
        setInfo()
    }
}