package com.red45.chathub

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.red45.chathub.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        db = FirebaseDatabase.getInstance()

        setContentView(binding.root)


        binding.regBtn.setOnClickListener{
            val name = binding.etRegName.text.toString().trim()
            val email = binding.etRegEmail.text.toString().trim()
            val pass = binding.etRegPass.text.toString().trim()

            if (name.isEmpty()){
                binding.etRegName.error = "Name is required"
                binding.etRegName.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()){
                binding.etRegEmail.error = "E-mail is required"
                binding.etRegEmail.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etRegEmail.error = "Enter valid E-mail"
                binding.etRegEmail.requestFocus()
                return@setOnClickListener
            }
            if (pass.isEmpty()){
                binding.etRegPass.error = "Password is required"
                binding.etRegPass.requestFocus()
                return@setOnClickListener
            }
            if (pass.length < 8){
                binding.etRegPass.error = "Password should be at least 8 characters"
                binding.etRegPass.requestFocus()
                return@setOnClickListener
            }

            val proDialog = ProgressDialog(this)
            proDialog.setMessage("Registering user...")
            proDialog.show()

           auth.createUserWithEmailAndPassword(email, pass)
               .addOnCompleteListener(this){
                   task->
                   if (task.isSuccessful){
                       val user = auth.currentUser
                       user?.sendEmailVerification()?.addOnCompleteListener {
                           task->
                           if (!task.isSuccessful){
                               Toast.makeText(this, "Failed to send Verification E-mail", Toast.LENGTH_SHORT).show()
                           }
                       }
                       val ref = db.getReference("users")
                       val uId = ref.push().key
                       val uObj = User(uId,name,email)

                       if (uId != null){
                           ref.child(uId).setValue(uObj).addOnCompleteListener {
                               Task->
                               if (Task.isSuccessful){
                                   proDialog.dismiss()
                                   startActivity(Intent(this, EmailVerifyActivity::class.java))
                                   finish()
                               }
                               else{
                                   proDialog.dismiss()
                                   Toast.makeText(this, "Failed to register user", Toast.LENGTH_SHORT).show()
                                   finish()
                               }
                           }
                       }
                   }
                   else{
                       proDialog.dismiss()
                       Toast.makeText(this,"E-mail is already exist", Toast.LENGTH_SHORT).show()
                   }
               }
        }
    }
}

