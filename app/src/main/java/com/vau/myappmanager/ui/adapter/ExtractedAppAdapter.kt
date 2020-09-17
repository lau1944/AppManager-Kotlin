package com.vau.myappmanager.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vau.myappmanager.databinding.EachExtractAppBinding
import com.vau.myappmanager.objects.ExtractApp
import com.vau.myappmanager.ui.pages.AppInfo
import com.vau.myappmanager.ui.viewmodels.FileViewModel
import com.vau.myappmanager.utils.InjectUtil


class ExtractedAppAdapter (
    private val list: ArrayList<ExtractApp>
): ListAdapter<ExtractApp, RecyclerView.ViewHolder>(ExtractedAppDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppViewholder(
            EachExtractAppBinding.inflate(LayoutInflater.from(parent.context),
                parent,  false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AppViewholder).bind(getItem(position)!!, list)
    }

    class AppViewholder(
        private val binding: EachExtractAppBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ExtractApp, list: ArrayList<ExtractApp>) = with(itemView) {
            binding.appName.text = item.name
            binding.appSize.text = item.size


            binding.extractAppLayout.setOnClickListener {
                context.startActivity(
                    Intent(context, AppInfo::class.java)
                        .putExtra("appId", item.packageName))
            }


            val viewModel = ViewModelProvider(context as AppCompatActivity, InjectUtil.provideFileViewModelFactory(context))
                .get(FileViewModel::class.java)

            binding.deleteApp.setOnClickListener {
                viewModel.deleteFile(item.dir.toString())
                viewModel.deleteExtract(item)
                list.removeAt(position)
                viewModel.updateExtractedList(list)
            }

            Glide.with(this).load(viewModel.getIcon(context,item.packageName)).into(binding.appIcon)


        }
    }
}

class ExtractedAppDiffCallback : DiffUtil.ItemCallback<ExtractApp>() {
    override fun areItemsTheSame(oldItem: ExtractApp, newItem: ExtractApp): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ExtractApp, newItem: ExtractApp): Boolean {
        return oldItem.packageName == newItem.packageName
    }
}