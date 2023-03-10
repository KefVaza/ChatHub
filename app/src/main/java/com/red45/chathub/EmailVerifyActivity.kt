package com.red45.chathub

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.red45.chathub.databinding.ActivityEmailVerifyBinding

class EmailVerifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailVerifyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEmailVerifyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.logBtn.setOnClickListener{
            val proDialog = ProgressDialog(this)
            proDialog.setMessage("Logging in...")
            proDialog.show()
            startActivity(Intent(this,LoginActivity::class.java))
            proDialog.dismiss()
            finish()
        }
    }
}