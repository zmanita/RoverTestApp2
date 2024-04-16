package com.example.rovertestapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    var dataFragment = DataFragment()
    var controlFragment = ControlFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        supportFragmentManager.beginTransaction().replace(R.id.container, dataFragment).commit()
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.data -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, dataFragment).commit()
                    true
                }
                R.id.control -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, controlFragment).commit()
                    true
                }
                else -> false
            }
        }
        val disconnectbtn = findViewById<Button>(R.id.disconnect_btn)
        disconnectbtn.setOnClickListener {
            val intent2 = Intent(this, ActivityCover::class.java)
            startActivity(intent2)
            finish()
        }
    }
}