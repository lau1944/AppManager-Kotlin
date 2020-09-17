package com.vau.myappmanager.ui.pages

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.vau.myappmanager.R
import com.vau.myappmanager.databinding.ActivityAppInfoBinding
import com.vau.myappmanager.objects.LikedApp
import com.vau.myappmanager.objects.EachApp
import com.vau.myappmanager.objects.ExtractApp
import com.vau.myappmanager.ui.viewmodels.AppInfoViewModel
import com.vau.myappmanager.ui.viewmodels.FileViewModel
import com.vau.myappmanager.utils.AlertDialogHelper
import com.vau.myappmanager.utils.InjectUtil
import java.lang.Exception

class AppInfo : AppCompatActivity() {
    lateinit var binding : ActivityAppInfoBinding
    private var appId : String ?= ""
    lateinit var appInfo : EachApp
    private val model : AppInfoViewModel by viewModels {
        InjectUtil.provideAppInfoViewModelFactory(this)
    }
    private val fileModel : FileViewModel by viewModels {
        InjectUtil.provideFileViewModelFactory(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = model
        binding.lifecycleOwner = this


        //observe isLike
        model.isLike.observe(this, Observer {
            if (it != null) {
                binding.likeApp.isChecked = it
            }
        })

        val bundle = intent.extras
        appId = bundle!!.getString("appId")

        //press like or unlike
        binding.likeApp.setOnCheckStateChangeListener { view, checked ->
            if (checked) {
                model.likeApp(LikedApp(appInfo.packageName!!,
                    appInfo.size!!, appInfo.name!!, appInfo.source!!))
                Toast.makeText(this, R.string.like_app, Toast.LENGTH_LONG).show()
            } else {
                model.dislikeApp(LikedApp(appInfo.packageName!!,
                    appInfo.size!!, appInfo.name!!, appInfo.source!!))
            }
        }

        binding.back.setOnClickListener { finish() }

        binding.delete.setOnClickListener {
            AlertDialogHelper.getInstance()
                .buildDialog(this, resources.getString(R.string.uninstall)
                ,resources.getString(R.string.uninstall_dialog))
                .setPositiveButton(resources.getString(R.string.confirm), DialogInterface.OnClickListener{DialogInterface, it ->
                    uninstallApp()
                })
                .setNegativeButton(resources.getString(R.string.no), DialogInterface.OnClickListener {DialogInterface, it ->

                }).create().show()
        }

        binding.openSetting.setOnClickListener{
            try {
                startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", appInfo.packageName, null)
                })
            } catch (e: Exception) {}
        }

        model.getAppInfo(appId!!)

        binding.appLink.setOnClickListener {
            openlink()
        }

        fileModel.uriData.observe(this, Observer {
            if (it != null) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "*/*"
                intent.putExtra(Intent.EXTRA_STREAM, it)
                startActivity(Intent.createChooser(intent, resources.getString(R.string.share_via)))
            }
        })

        binding.open.setOnClickListener {
            try {
                startActivity(Intent(packageManager.getLaunchIntentForPackage(appInfo.packageName.toString()))
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) )
            } catch (e: Exception) {
                Log.d("myAppCrash", e.toString())
            }
        }

        binding.share.setOnClickListener {
            try {
                fileModel.getSource(appInfo.source.toString(), appInfo.name.toString(), true,
                    ExtractApp(appInfo.packageName!!, appInfo.size,
                        appInfo.name, ""),this)
            } catch (e: Exception) {
                Log.d("myAppCrash", e.toString())
            }
        }

        binding.extract.setOnClickListener {
           try {
               fileModel.getSource(appInfo.source.toString(), appInfo.name.toString(), false,
                   ExtractApp(appInfo.packageName!!, appInfo.size,
                       appInfo.name, ""),this)
           } catch (e: Exception) {
               Log.d("myAppCrash", e.toString())
           }
        }

        model.appInfo.observe(this, Observer {
            if (it != null) {
                appInfo = it
                model.isLike(it.packageName.toString())
                Glide.with(this).load(it.icon).into(binding.appIcon)
                binding.lastUpdate.text = model.transformToDate(it.lastUpdate!!)
                binding.permission.text = model.permissionBuilder(it.permission!!)
            } else {
                finish()
                Toast.makeText(this, R.string.cannot_find_app, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun uninstallApp() {
        try {
            startActivity(Intent(Intent.ACTION_DELETE).apply {
                data = Uri.parse("package:${appInfo.packageName}")
            })
            this.finish()
        } catch (e: Exception) {}
    }

    fun openlink() {
        try {
            if (appInfo.packageName != "") {
                val uri =
                    Uri.parse("https://play.google.com/store/apps/details?id=${appInfo.packageName}")
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
            }
        } catch (e: Exception) {}
    }

}