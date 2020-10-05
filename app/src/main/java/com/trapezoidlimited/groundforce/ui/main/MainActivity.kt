package com.trapezoidlimited.groundforce.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.ActivityMainBinding
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}

