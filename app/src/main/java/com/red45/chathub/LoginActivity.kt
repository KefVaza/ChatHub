package com.red45.chathub

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.red45.chathub.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.logBtn.setOnClickListener{
            val email = binding.etLogEmail.text.toString().trim()
            val pass = binding.etLogPass.text.toString().trim()

            if (email.isEmpty()){
                binding.etLogEmail.error = "E-mail is required"
                binding.etLogEmail.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etLogEmail.error = "Enter valid E-mail"
                binding.etLogEmail.requestFocus()
                return@setOnClickListener
            }
            if (pass.isEmpty()){
                binding.etLogPass.error = "Password is required"
                binding.etLogPass.requestFocus()
                return@setOnClickListener
            }

            val pro = ProgressDialog(this)
            pro.setMessage("Logging in...")
            pro.show()

            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    task ->
                    if (task.isSuccessful){
                        val user = auth.currentUser

                        if (user != null && user.isEmailVerified){
                            pro.dismiss()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        else{
                            pro.dismiss()
                            Toast.makeText(this, "Please verify your E-mail", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, EmailVerifyActivity::class.java))
                            auth.signOut()
                            finish()

                        }
                    }
                    else{
                        pro.dismiss()
                        Toast.makeText(this, "Login failed"+task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }


        }
        binding.tvForgotPass.setOnClickListener{
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }

    }
}