package com.example.recyclersample.favePage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclersample.data.PersonFave
import com.example.recyclersample.data.PersonFaveSource
import com.example.recyclersample.databinding.ItemFaveBinding

class PersonFaveAdapter(private val faveList: List<PersonFave>, private val faveRepository: PersonFaveSource) : RecyclerView.Adapter<PersonFaveAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: PersonFave)
    }

    class ListViewHolder(var binding: ItemFaveBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = faveList.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemFaveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val fave = faveList[position]
        Glide.with(holder.itemView.context)
            .load(fave.avatarUrl)
            .into(holder.binding.photoAvatarFave)
        holder.apply {
            binding.apply {
                tvUsername.text = fave.login
                btnDelete.setOnClickListener { faveRepository.delete(fave) }
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(faveList[holder.adapterPosition])
                }
            }
        }
    }
}
