package com.red45.chathub

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.red45.chathub.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var mImgUri: Uri? = null
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectImgBtn.setOnClickListener{
            selectImage()
        }
    }
    private fun selectImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.data != null){
            mImgUri = data.data
            Glide.with(this).load(mImgUri).into(binding.ciPickProImg)
        }
    }
}