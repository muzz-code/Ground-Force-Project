package com.trapezoidlimited.groundforce.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.ActivityMainBinding
import com.trapezoidlimited.groundforce.utils.DataListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inflate layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        parseData()
    }


    private fun parseData() {
        val data = intent.data

        try {
            if (data?.getQueryParameter("token") != null) {
                DataListener.token  = data.getQueryParameter("token")!!

                val token = DataListener.token

                println(token)
            }

        } catch (ex: Exception) {
            finish()
        }

    }

}

