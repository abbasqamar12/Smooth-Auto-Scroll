package com.vmstechs.hpqrresult.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vmstechs.hpqrresult.databinding.ActivityMainBinding
import com.vmstechs.hpqrresult.new_user.NewUserDialog
import com.vmstechs.hpqrresult.smooth_scroll.FeaturesAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var newUserDialog: NewUserDialog
    private var previousUserId: String = "0"

    private var currentIndex = 0

    private var userList = mutableListOf<UserDetail>()
    private lateinit var userAdapter: UsersAdapter // = UsersAdapter(userList)
    private lateinit var userFeatureAdapter: FeaturesAdapter // = UsersAdapter(userList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        // Prevent the TV from going to standby or dimming
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        observeViewModel()

        /*mainBinding.rvUserList.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        userAdapter = UsersAdapter(userList)
        mainBinding.rvUserList.adapter = userAdapter*/
        //Feature Adapter
        userFeatureAdapter = FeaturesAdapter()



        mainViewModel.refreshAllUsers()


    }


    private fun observeViewModel() {
        mainViewModel.newJoinerResponse.observe(this) {
            Log.d("MVVM", "New Join Response: $it")
            manageNewUserDialog(it.first_user_details)
        }
        mainViewModel.allJoinedUsersResponse.observe(this) {
            Log.d("MVVM", "All User Response: $it")

            /*if (it.AllJoined_user_details.isNotEmpty()) {
                userAdapter.updateUserList(it.AllJoined_user_details)
            }*/
            if (it.AllJoined_user_details.isNotEmpty()) {
                userList = it.AllJoined_user_details.toMutableList()
                setupFeatureTiles(userList)
            }
            refreshNewUser()
        }
        mainViewModel.resultError.observe(this) {
            Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
        }
        mainViewModel.loading.observe(this) {

        }
    }

    private fun refreshNewUser() {
        var counter = 0
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                mainViewModel.refreshNewJoiner()
                scrollToNextPosition()
                counter += 10
                Log.d("TIMER", "Timer : $counter")
                mainHandler.postDelayed(this, 10000)
            }
        })
    }

    private fun manageNewUserDialog(newUserDetails: UserDetail) { //newUserDetails: UserDetail
        if (previousUserId != newUserDetails.id.toString()) {
            // userAdapter.addUser(newUserDetails)
            updateList(newUserDetails)
           // userFeatureAdapter.notifyItemInserted(userList.size)
            setupFeatureTiles(userList)
            newUserDialog = NewUserDialog(this, newUserDetails)
            newUserDialog.show()
            previousUserId = newUserDetails.id.toString()

        }
    }

    private fun updateList(newUserDetails: UserDetail) {
        userList.add(newUserDetails)
        userList = userList.distinctBy { it.id }.toMutableList()
    }

    fun scrollToNextPosition() {
        if (userList.isNotEmpty()) {
            if (userList.size > 4) {
                Log.d("MVVM", "Current Index: $currentIndex List Size: ${userList.size}")
                if (currentIndex > userList.size - 1) {
                    currentIndex = 0
                }

                mainBinding.rvUserList.smoothScrollToPosition(currentIndex)
                currentIndex += 1
            }
        }
    }


    /******
     * New Featured Adapter
     * ******/

    private fun setupFeatureTiles(featuresList: List<UserDetail>) {
        with(mainBinding.rvUserList) {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = userFeatureAdapter
        }
        userFeatureAdapter.submitList(featuresList)

        lifecycleScope.launch { autoScrollFeaturesList() }
    }

    private val DELAY_BETWEEN_SCROLL_MS = 50L  //25L
    private val SCROLL_DX = 15 //5
    private val DIRECTION_RIGHT = 1

    private tailrec suspend fun autoScrollFeaturesList() {
        if (mainBinding.rvUserList.canScrollHorizontally(DIRECTION_RIGHT)) {
            mainBinding.rvUserList.smoothScrollBy(SCROLL_DX, 0)
        } else {
            val firstPosition =
                (mainBinding.rvUserList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            if (firstPosition != RecyclerView.NO_POSITION) {
                val currentList = userFeatureAdapter.currentList
                val secondPart = currentList.subList(0, firstPosition)
                val firstPart = currentList.subList(firstPosition, currentList.size)
                userFeatureAdapter.submitList(firstPart + secondPart)
            }
        }
        delay(DELAY_BETWEEN_SCROLL_MS)
        autoScrollFeaturesList()
    }

}