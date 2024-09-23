package com.vmstechs.hpqrresult.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import com.vmstechs.hpqrresult.R
import com.vmstechs.hpqrresult.databinding.ItemUserDetailsBinding

class UsersAdapter(private var userList: MutableList<UserDetail>) :
    Adapter<UsersAdapter.UsersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val userDetailsBinding =
            ItemUserDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(userDetailsBinding)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.userDetailsBinding(userList.get(position))
    }

    fun addUser(userDetail: UserDetail) {
        //  var index = -2
        Log.d("USER_ADAPTER", "List Size Before: ${userList.size}")
        /*  val user: UserDetail? = userList.find { it.id == userDetail.id }
         if (user!=null) {
              index = userList.indexOf(user)
             userList.removeAt(index)
         }
          Log.d("USER_ADAPTER", "Index : $index")*/
        userList.add(userDetail)
        Log.d("USER_ADAPTER", "List Size After: ${userList.size}")
        notifyItemInserted(userList.size - 1)
        removeDuplicate()
    }

    fun updateUserList(users: List<UserDetail>) {
        //userList.clear()
        Log.d("USER_ADAPTER", "All Users List Size Before: ${userList.size}")
        userList.addAll(users)
        Log.d("USER_ADAPTER", "All Users List Size After: ${userList.size}")
        notifyDataSetChanged()
    }

    fun removeDuplicate(){
        userList = userList.distinctBy { it.id }.toMutableList()
        notifyDataSetChanged()
    }

    class UsersViewHolder(private val itemUserBinding: ItemUserDetailsBinding) :
        ViewHolder(itemUserBinding.root) {
        fun userDetailsBinding(userDetail: UserDetail?) {
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