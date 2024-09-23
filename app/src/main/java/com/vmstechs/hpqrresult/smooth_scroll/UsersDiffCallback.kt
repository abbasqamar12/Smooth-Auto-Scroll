package com.vmstechs.hpqrresult.smooth_scroll

import androidx.recyclerview.widget.DiffUtil
import com.vmstechs.hpqrresult.home.UserDetail

class UsersDiffCallback : DiffUtil.ItemCallback<UserDetail>() {
    override fun areItemsTheSame(oldItem: UserDetail, newItem: UserDetail): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: UserDetail, newItem: UserDetail): Boolean =
        oldItem == newItem
}