package com.red45.chathub

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.red45.chathub.databinding.ActivityEmailVerifyBinding
import com.red45.chathub.databinding.ActivityLoginBinding

class EmailVerifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailVerifyBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEmailVerifyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.resendEmailBtn.setOnClickListener{
            emailVerification()
        }
        binding.logBtn.setOnClickListener{
            val proDialog = ProgressDialog(this)
            proDialog.setMessage("Logging in...")
            proDialog.show()
            startActivity(Intent(this,LoginActivity::class.java))
            proDialog.dismiss()
            finish()
        }
    }
    private fun emailVerification(){
        val user = FirebaseAuth.getInstance().currentUser
        user?.sendEmailVerification()?.addOnCompleteListener{
            task ->
            if (task.isSuccessful){
                Toast.makeText(this, "Verification email sent to ${user.email}", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Failed to send email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}