package com.red45.chathub

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.red45.chathub.databinding.ActivityForgotPassBinding
import com.red45.chathub.databinding.ActivityLoginBinding

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPassBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()

        binding.subBtn.setOnClickListener{
            val email = binding.etEmail.text.toString().trim()
            if (email.isEmpty()){
                binding.etEmail.error = "E-mail is required"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmail.error = "Enter valid E-mail"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }
            forgotPass()
        }
    }
    private fun forgotPass(){
        val email = binding.etEmail.text.toString().trim()
        val pro = ProgressDialog(this)
        pro.setMessage("Sending reset password E-mail...")
        pro.show()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                task->
                if (task.isSuccessful){
                    pro.dismiss()
                    Toast.makeText(this, "Password reset E-maul sent to /n $email", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                else{
                    pro.dismiss()
                    binding.etEmail.error = "Enter valid E-mail"
                    binding.etEmail.requestFocus()
                    finish()
                }
            }
    }
}