package com.red45.chathub

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.util.Patterns.EMAIL_ADDRESS
import android.view.View
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.red45.chathub.databinding.ActivityRegisterBinding
import java.io.IOException
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private var proImgUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        setContentView(binding.root)

        binding.selectImgBtn.setOnClickListener{
            selectImg()
        }

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
            //registerUser()
        }
    }
    private fun  selectImg(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null){
            proImgUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, proImgUri)
                binding.ciPickProImg.setImageBitmap(bitmap)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }
}
