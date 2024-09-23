package com.vmstechs.hpqrresult.smooth_scroll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vmstechs.hpqrresult.R
import com.vmstechs.hpqrresult.databinding.ItemUserDetailsBinding
import com.vmstechs.hpqrresult.home.UserDetail
import com.vmstechs.hpqrresult.home.UsersAdapter

class FeaturesAdapter:ListAdapter<UserDetail, RecyclerView.ViewHolder>(UsersDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val userDetailsBinding =
            ItemUserDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeatureItemViewHolder(userDetailsBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as FeatureItemViewHolder
        itemViewHolder.bind(getItem(position))
    }

    class FeatureItemViewHolder(private val itemUserBinding: ItemUserDetailsBinding) :
        RecyclerView.ViewHolder(itemUserBinding.root) {
        fun bind(userDetail: UserDetail) {
            if (userDetail != null) {
                if (!userDetail.filename1.isNullOrBlank()) {
                    if (!userDetail.filename1.isNullOrEmpty()) {
                        Picasso.get().load(userDetail.filename1)
                            .into(itemUserBinding.imgUserProfile)
                    } else {
                        Picasso.get().load(R.drawable.placeholder_profile)
                            .into(itemUserBinding.imgUserProfile)
                    }

                } else {
                    Picasso.get().load(R.drawable.placeholder_profile)
                        .into(itemUserBinding.imgUserProfile)
                }

                if (!userDetail.filename2.isNullOrBlank()) {
                    Picasso.get().load(userDetail.filename2).into(itemUserBinding.imgCompanyLogo)
                } else {
                    Picasso.get().load(R.drawable.transparent_bg)
                        .into(itemUserBinding.imgCompanyLogo)
                }

                itemUserBinding.txtUserName.text = userDetail.Full_Name
                itemUserBinding.txtDesignation.text = userDetail.Designation
                itemUserBinding.txtOrganisation.text = userDetail.Company_Name
            }
        }
    }
}