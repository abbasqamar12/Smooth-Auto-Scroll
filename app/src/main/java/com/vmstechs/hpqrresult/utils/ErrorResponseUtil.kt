package com.vmstechs.hpqrresult.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vmstechs.hpqrresult.utils.ErrorResponse
import okhttp3.ResponseBody

object ErrorResponseUtil {

    fun getError( errorBody: ResponseBody): String {
        val gson = Gson()
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse? = gson.fromJson(errorBody.charStream(), type)
        if (errorResponse != null) {
            return errorResponse.error
        }
        return "Something went wrong"
    }
}