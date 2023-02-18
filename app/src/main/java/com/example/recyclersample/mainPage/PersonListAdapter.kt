package com.example.recyclersample.mainPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclersample.api.UserDetail
import com.example.recyclersample.databinding.ItemPersonBinding

class PersonListAdapter(private val personList: List<UserDetail>) :
    RecyclerView.Adapter<PersonListAdapter.ListViewHolder>() {
    private var onItemClickCallBack: OnItemClickCallback? = null
    interface OnItemClickCallback { fun onItemClicked(data: UserDetail) }
    class ListViewHolder(var binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallBack = onItemClickCallback
    }

    override fun getItemCount(): Int = personList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val person = personList[position]
        Glide.with(holder.itemView.context)
            .load(person.avatarUrl)
            .into(holder.binding.personImage)
        holder.apply {
            binding.apply {
                personText.text = person.login
                itemView.setOnClickListener {
                    onItemClickCallBack?.onItemClicked(personList[holder.adapterPosition])
                }
            }
        }
    }
}