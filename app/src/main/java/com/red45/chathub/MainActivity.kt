package com.red45.chathub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.red45.chathub.Classes.MessageRvAdapter
import com.red45.chathub.Classes.MessageRvModel
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var searchEdit: TextInputEditText
    lateinit var messageRv: RecyclerView
    lateinit var messageRvAdapter: MessageRvAdapter
    lateinit var messageList: ArrayList<MessageRvModel>
    val url = "https://api.openai.com/v1/completions"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchEdit = findViewById(R.id.etSearch)
        messageRv = findViewById(R.id.rvMsg)
        messageList = ArrayList()
        messageRvAdapter = MessageRvAdapter(messageList)
        val layoutManager = LinearLayoutManager(applicationContext)
        messageRv.layoutManager = layoutManager
        messageRv.adapter = messageRvAdapter


        searchEdit.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEND){
                if (searchEdit.toString().length > 0){
                    messageList.add(MessageRvModel(searchEdit.text.toString(), "user"))
                    messageRvAdapter.notifyDataSetChanged()
                    getResponce(searchEdit.text.toString())
                }
                else{
                    searchEdit.error = "Please search here"
                    searchEdit.requestFocus()
                }
                return@OnEditorActionListener true
            }
            false
        })

    }
    private fun getResponce(search: String){
        searchEdit.setText("")
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val jsonObject: JSONObject? = JSONObject()
        jsonObject?.put("model", "text-davinci-003")
        jsonObject?.put("prompt", search)
        jsonObject?.put("temperature", 0)
        jsonObject?.put("max_tokens", 1000)
        jsonObject?.put("top_p", 1)
        jsonObject?.put("frequency_penalty", 0.0)
        jsonObject?.put("presence_penalty", 0.0)

        val postRQ: JsonObjectRequest = object: JsonObjectRequest(Method.POST, url, jsonObject, Response.Listener { response ->
            val responseMsg: String = response.getJSONArray("choices").getJSONObject(0).getString("text")
            messageList.add(MessageRvModel(responseMsg, "bot"))
            messageRvAdapter.notifyDataSetChanged()
        }, Response.ErrorListener {
            Toast.makeText(this,"Failed to get response", Toast.LENGTH_SHORT).show()
        }){
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer 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"
                return params
            }
        }

        postRQ.setRetryPolicy(object : RetryPolicy{
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            override fun retry(error: VolleyError?) {

            }
        })
        queue.add(postRQ)
    }

}