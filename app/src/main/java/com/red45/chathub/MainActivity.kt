package com.red45.chathub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request.Method.POST
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.red45.chathub.Classes.MessageRVAdapter
import com.red45.chathub.Classes.MessageRVModel
import org.json.JSONObject
import java.lang.ref.ReferenceQueue

class MainActivity : AppCompatActivity() {

    lateinit var queryEdt : TextInputEditText
    lateinit var msgRv : RecyclerView
    lateinit var msgRVAdapter : MessageRVAdapter
    lateinit var msgList : ArrayList<MessageRVModel>
//    "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"
    var url = "https://api.openai.com/v1/completions"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queryEdt= findViewById(R.id.tiSearch)
        msgRv =findViewById(R.id.rvMessages)
        msgList = ArrayList()
        msgRVAdapter= MessageRVAdapter(msgList)

        val layoutManager = LinearLayoutManager(applicationContext)
        msgRv.adapter = msgRVAdapter

        queryEdt.setOnEditorActionListener(TextView.OnEditorActionListener { textview, i, keyEvent ->
            if(i==EditorInfo.IME_ACTION_SEND){
                if(queryEdt.text.toString().length > 0){
                    msgList.add(MessageRVModel(queryEdt.text.toString(),"user"))
                    msgRVAdapter.notifyDataSetChanged()
                    getResponce(queryEdt.text.toString())
                }else{
                    Toast.makeText(this@MainActivity,"Pleas enter your query !",Toast.LENGTH_SHORT).show()
                }
                return@OnEditorActionListener true

            }
            false
        })



    }

    private fun getResponce(query: String) {
        queryEdt.setText("")
        val queue : RequestQueue = Volley.newRequestQueue(applicationContext)
        val jsonObject : JSONObject? = JSONObject()
        jsonObject?.put("model","text-davinci-003")
        jsonObject?.put("prompt",query)
        jsonObject?.put("temperature",0)
        jsonObject?.put("max_tokens",100)
        jsonObject?.put("frequency_penalty",0.0)
        jsonObject?.put("presence_penalty",0.0)
        jsonObject?.put("prompt",query)
        jsonObject?.put("prompt",query)

        val postRequest : JsonObjectRequest = object : JsonObjectRequest(Method.POST,url,jsonObject,Response.Listener { response ->
                val responceMsg : String =response.getJSONArray("choices").getJSONObject(0).getString("text")
            msgList.add(MessageRVModel(responceMsg,"bot"))
            msgRVAdapter.notifyDataSetChanged()

        }, Response.ErrorListener {
            Toast.makeText(this@MainActivity,"Somthing Wentt Wrong !!!",Toast.LENGTH_SHORT).show()
        }){

            override fun getHeaders(): MutableMap<String, String> {
                val params : MutableMap<String,String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer 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"
                return params
            }
        }
        postRequest.setRetryPolicy(object  : RetryPolicy{
            override fun getCurrentTimeout(): Int {
                return 5000
            }

            override fun getCurrentRetryCount(): Int {
                return 5000
            }

            override fun retry(error: VolleyError?) {

            }
        })
            queue.add(postRequest)


    }
}