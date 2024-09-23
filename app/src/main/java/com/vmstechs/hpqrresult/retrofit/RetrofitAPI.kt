package com.vmstechs.hpqrresult.retrofit

import com.vmstechs.hpqrresult.home.AllJoinedUserResponse
import com.vmstechs.hpqrresult.home.NewUserResponse
import com.vmstechs.hpqrresult.utils.NetworkConst
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitAPI {
     @GET(NetworkConst.NEW_JOINED_USER)
      suspend fun requestNewJoiner(): Response<NewUserResponse>

    @GET(NetworkConst.ALL_JOINED_USERS)
    suspend fun requestAllJoinedUsers(): Response<AllJoinedUserResponse>

}