package com.example.getfit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.getfit.data.User
import com.example.getfit.databinding.ActivityHomeBinding
import com.example.getfit.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var binding: ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val historyFragment = HistoryFragment()
    private val leaderBoardFragment = LeaderboardFragment()
    private val goalsFragment = GoalsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        replaceFragment(homeFragment)

        val bottomNavigation: BottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        var frame = findViewById<FrameLayout>(R.id.fragment_container)

        val bundle = Bundle()
        val user:User = intent.extras?.get("loggedUser") as User
        bundle.putSerializable("user", user)
        homeFragment.arguments = bundle
        historyFragment.arguments = bundle
        goalsFragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        

        bottomNavigation.setOnItemSelectedListener{
                item->
            when(item.itemId){
                R.id.action_home ->{
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.action_goals ->{
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, goalsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }


                R.id.action_history ->{
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, historyFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }


                R.id.action_leaderboard ->{
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, leaderBoardFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
            true
        }

    }
    private fun replaceFragment(fragment: Fragment){
        if(fragment!=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}


