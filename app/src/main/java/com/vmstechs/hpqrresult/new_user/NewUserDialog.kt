package com.vmstechs.hpqrresult.new_user

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import com.squareup.picasso.Picasso
import com.vmstechs.hpqrresult.R
import com.vmstechs.hpqrresult.databinding.DialogNewUserBinding
import com.vmstechs.hpqrresult.home.UserDetail


class NewUserDialog(
    private val context: Context,
    private val newUserDetails: UserDetail,
) : Dialog(context) {

    private lateinit var dialogNewUserBinding: DialogNewUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogNewUserBinding = DialogNewUserBinding.inflate(LayoutInflater.from(context))
        setContentView(dialogNewUserBinding.root)

        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window!!.setBackgroundDrawableResource(R.drawable.bg_2)//R.color.red
        setCanceledOnTouchOutside(true)
        setDialogData()

        Handler(Looper.getMainLooper()).postDelayed({
            dismiss()

        }, 5000)
    }

    private fun setDialogData() {
        if (!newUserDetails.filename1.isNullOrBlank()) {
            if (!newUserDetails.filename1.isNullOrEmpty()) {
                Picasso.get().load(newUserDetails.filename1)
                    .into(dialogNewUserBinding.imgUserProfile)
            } else {
                Picasso.get().load(R.drawable.placeholder_profile)
                    .into(dialogNewUserBinding.imgUserProfile)
            }

        } else {
            Picasso.get().load(R.drawable.placeholder_profile)
                .into(dialogNewUserBinding.imgUserProfile)
        }
        if (!newUserDetails.filename2.isNullOrBlank()) {
            Picasso.get().load(newUserDetails.filename2).into(dialogNewUserBinding.imgCompanyLogo)
        } else {
            Picasso.get().load(R.drawable.transparent_bg).into(dialogNewUserBinding.imgCompanyLogo)
        }
        dialogNewUserBinding.txtUserName.text = newUserDetails.Full_Name
        dialogNewUserBinding.txtDesignation.text = newUserDetails.Designation
        dialogNewUserBinding.txtCompanyName.text = newUserDetails.Company_Name
    }
}