package com.example.recyclerview1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.recyclerview1.adapter.UserActionListener
import com.example.recyclerview1.adapter.UsersAdapter
import com.example.recyclerview1.databinding.ActivityMainBinding
import com.example.recyclerview1.fragments.UserDetailsFragment
import com.example.recyclerview1.model.User
import com.example.recyclerview1.model.UsersListener
import com.example.recyclerview1.model.UsersService

class MainActivity : AppCompatActivity(), Navigator{

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var currentFragment: Fragment? = null
    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks(){
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is NavHostFragment) return
            currentFragment = f
            updateUi()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.toolbar)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHost.navController
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun onSupportNavigateUp() = navController.navigateUp() || super.onSupportNavigateUp()

    private fun updateUi(){

        if (navController.currentDestination?.id == navController.graph.startDestination){
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else{
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun showDetails(user: User) {
        navController.navigate(R.id.userDetailsFragment, UserDetailsFragment.createArgs(user.id))
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun toast(messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }

}