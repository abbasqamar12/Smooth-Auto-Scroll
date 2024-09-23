package com.vmstechs.hpqrresult.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vmstechs.hpqrresult.retrofit.ApiService
import com.vmstechs.hpqrresult.utils.ErrorResponseUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainViewModel:ViewModel() {
    private val apiService = ApiService.getNetworkService()
    private var job: Job? = null

    val allJoinedUsersResponse = MutableLiveData<AllJoinedUserResponse>()
    val newJoinerResponse = MutableLiveData<NewUserResponse>()
    val resultError = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is IOException -> {
                // Handle network error
                println("Network Error: ${exception.message}")
                Log.e("ERROR", "Network Error: ${exception.message}")
            }

            is HttpException -> {
                // Handle HTTP error
                println("HTTP Error: ${exception.message}")
                Log.e("ERROR", "HTTP Error: ${exception.message}")
            }

            else -> {
                // Handle other exceptions
                println("Unknown Error: ${exception.message}")
                Log.e("ERROR", "Unknown Error: ${exception.message}")
            }
        }
    }

    fun refreshNewJoiner() {
            requestNewJoiner()
    }

    private fun requestNewJoiner() {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            supervisorScope {
                try {
                    val response = apiService.requestNewJoiner()
                    withContext(Dispatchers.Main) {
                        Log.d(
                            "MVVM",
                            "New User Success : ${response.isSuccessful} response : ${response.body()}"
                        )

                        if (response.code() == 200) {
                            if (response.body()?.status!!) {
                                newJoinerResponse.value = response.body()
                                loading.value = false
                            } else {
                                Log.e("ERROR", "New User Error: ${response.body()?.message}")
                                onError(response.body()?.message!!)
                            }
                        } else if (response.code() == 400) {
                            val errorMessage = ErrorResponseUtil.getError(response.errorBody()!!)
                            Log.e("ERROR", "New User Error: $errorMessage")
                            onError(errorMessage)

                        } else if (response.code() == 401) {
                            val errorMessage = ErrorResponseUtil.getError(response.errorBody()!!)
                            Log.e("ERROR", "New User Error: $errorMessage")
                            onError(errorMessage)
                        } else {
                            val errorMessage = ErrorResponseUtil.getError(response.errorBody()!!)
                            Log.e("ERROR", "New User Error: $errorMessage")
                            onError(errorMessage)
                        }
                    }

                } catch (e: IOException) {
                    Log.e("ERROR", "Network Error: ${e.message}")
                    onError("Network Error: ${e.message}")
                } catch (e: HttpException) {
                    Log.e("ERROR", "HTTP Error: ${e.message}")
                    onError("HTTP Error: ${e.message}")
                } catch (e: Exception) {
                    Log.e("ERROR", "Unknown Error: ${e.message}")
                    onError("Unknown Error: ${e.message}")
                }
            }
        }
    }


    fun refreshAllUsers() {
        Log.d("MVVM", "refreshAllUsers() Clicked")
            requestAllUsers()
    }

    private fun requestAllUsers() {
        Log.d("MVVM", "requestAllUsers() Clicked")
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            supervisorScope {
                try {
                    val response = apiService.requestAllJoinedUsers()
                    withContext(Dispatchers.Main) {
                        Log.d(
                            "MVVM",
                            "User List Success : ${response.isSuccessful} response : ${response.body()}"
                        )

                        if (response.code() == 200) {
                            if (response.body()?.status!!) {
                                allJoinedUsersResponse.value = response.body()
                                loading.value = false
                            } else {
                                Log.e("ERROR", "User List Error: ${response.body()?.message}")
                                onError(response.body()?.message!!)
                            }

                        } else if (response.code() == 400) {
                            val errorMessage = ErrorResponseUtil.getError(response.errorBody()!!)
                            Log.e("ERROR", "User List Error: $errorMessage")
                            onError(errorMessage)

                        } else if (response.code() == 401) {
                            val errorMessage = ErrorResponseUtil.getError(response.errorBody()!!)
                            Log.e("ERROR", "User List Error: $errorMessage")
                            onError(errorMessage)
                        } else {
                            val errorMessage = ErrorResponseUtil.getError(response.errorBody()!!)
                            Log.e("ERROR", "User List Error: $errorMessage")
                            onError(errorMessage)
                        }
                    }

                } catch (e: IOException) {
                    Log.e("ERROR", "Network Error: ${e.message}")
                    onError("Network Error: ${e.message}")
                } catch (e: HttpException) {
                    Log.e("ERROR", "HTTP Error: ${e.message}")
                    onError("HTTP Error: ${e.message}")
                } catch (e: Exception) {
                    Log.e("ERROR", "Unknown Error: ${e.message}")
                    onError("Unknown Error: ${e.message}")
                }
            }
        }
    }


    private fun onError(message: String) {
        Log.d("MVVM", "User Error Response $message")
        resultError.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}