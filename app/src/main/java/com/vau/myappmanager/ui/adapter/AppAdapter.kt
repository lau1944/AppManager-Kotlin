package com.vau.myappmanager.ui.adapter

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vau.myappmanager.R
import com.vau.myappmanager.databinding.EachAppBinding
import com.vau.myappmanager.objects.EachApp
import com.vau.myappmanager.objects.ExtractApp
import com.vau.myappmanager.ui.pages.AppInfo
import com.vau.myappmanager.ui.viewmodels.FileViewModel
import com.vau.myappmanager.utils.AlertDialogHelper
import com.vau.myappmanager.utils.InjectUtil
import kotlinx.android.synthetic.main.each_app.view.*

class AppAdapter : ListAdapter<EachApp, RecyclerView.ViewHolder>(AppDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppViewholder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.each_app, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AppViewholder).bind(getItem(position)!!)
    }

    class AppViewholder(
        private val binding: EachAppBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun share(viewModel: FileViewModel) = with(itemView){
            viewModel.uriData.observe(context as AppCompatActivity, Observer {
                if (it != null) {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "*/*"
                    intent.putExtra(Intent.EXTRA_STREAM, it)
                    context.startActivity(Intent.createChooser(intent, context.resources.getString(R.string.share_via)))
                }
            })
        }
        @SuppressLint("SetTextI18n")
        fun bind(item: EachApp) = with(itemView) {
            val animationSet = AnimationSet(true)
            val transitionAnimation = TranslateAnimation(0f,0f,100f,0f)
            val alphaAnimation = AlphaAnimation(0.7f, 1f)
            animationSet.addAnimation(transitionAnimation)
            animationSet.addAnimation(alphaAnimation)
            animationSet.duration = 900
            binding.appLayout.startAnimation(animationSet)

            Glide.with(this).load(item.icon).into(binding.myAppsImage)
            binding.myAppsName.text = item.name
            binding.myAppsText.text = item.packageName
            binding.myAppSize.text = context.resources.getString(R.string.size) + " " + item.size

            val viewModel = ViewModelProvider(context as AppCompatActivity, InjectUtil.provideFileViewModelFactory(context))
                .get(FileViewModel::class.java)

            app_layout.setOnClickListener {
                context.startActivity(Intent(context, AppInfo::class.java)
                    .putExtra("appId", item.packageName))
            }

            my_app_pop_up.setOnClickListener {
                val popupDialog = AlertDialogHelper.getInstance().buildDialog(context,
                    context.resources.getString(R.string.apk_file),
                    context.resources.getString(R.string.more_app_options))
                popupDialog.setPositiveButton(context.resources.getString(R.string.extract_apk),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        viewModel.getSource(item.source!!, item.name!!, false,
                            ExtractApp(item.packageName.toString(), item.size,
                                item.name, ""), context)
                    })
                popupDialog.setNegativeButton(context.resources.getString(R.string.share_apk),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        viewModel.getSource(item.source!!, item.name!!, true,
                            ExtractApp(item.packageName.toString(), item.size,
                                item.name, ""), context)
                        share(viewModel)
                    })
                popupDialog.create().show()
            }

        }
    }
}

class AppDiffCallback : DiffUtil.ItemCallback<EachApp>() {
    override fun areItemsTheSame(oldItem: EachApp, newItem: EachApp): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: EachApp, newItem: EachApp): Boolean {
        return oldItem.source == newItem.source
    }
}