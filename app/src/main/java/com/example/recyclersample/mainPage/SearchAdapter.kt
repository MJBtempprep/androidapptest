package com.example.recyclersample.mainPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclersample.api.UserDetail
import com.example.recyclersample.databinding.ItemPersonBinding


class SearchAdapter: RecyclerView.Adapter<SearchAdapter.UserViewHolder>() {

    private val list = ArrayList<UserDetail>()
    private var onItemClickCallback: OnItemClickCallback? = null
    interface OnItemClickCallback { fun onItemCLicked(data: UserDetail) }





    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }





    inner class UserViewHolder(private val binding: ItemPersonBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(user: UserDetail){
            binding.root.setOnClickListener{
                onItemClickCallback?.onItemCLicked(user)
            }
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .centerCrop()
                    .into(personImage)
                personText.text = user.login
            }
        }
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }





    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }





    override fun getItemCount(): Int = list.size





    fun setList(users: List<UserDetail>){
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

}