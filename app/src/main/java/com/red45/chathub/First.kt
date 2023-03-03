package com.red45.chathub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.red45.chathub.databinding.ActivityFirstBinding

class First : AppCompatActivity() {

    lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }
}