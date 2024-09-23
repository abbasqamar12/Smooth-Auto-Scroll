package com.vmstechs.hpqrresult.home

data class NewUserResponse(
    val first_user_details: UserDetail,
    val message: String,
    val status: Boolean
)