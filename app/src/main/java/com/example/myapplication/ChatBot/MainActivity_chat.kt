package com.example.myapplication.ChatBot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.AdapterChatBot
import com.example.myapplication.LoginFragment
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



 class MainActivity_chat : AppCompatActivity() {
    private val adapterChatBot = AdapterChatBot()

    private lateinit var database: DatabaseReference
    var firebaseUser: FirebaseUser? = null
    var i=0
    var value =0
    var kint_value = null
     var backButtonCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_chat)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "E-NUFF"
        firebaseUser = FirebaseAuth.getInstance().currentUser
        val database = Firebase.database


        val myRef = database.getReference("ChatList").child(firebaseUser!!.uid)

        txtResponse_yes.setOnClickListener {

                Toast.makeText(this@MainActivity_chat, "Yes", Toast.LENGTH_LONG).show()
            etChat.setText("Yes")
        }
        txtResponse_No.setOnClickListener {

                    Toast.makeText(this@MainActivity_chat, "No", Toast.LENGTH_LONG).show()
            etChat.setText("No")
        }


        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.20:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(APIService::class.java)

        rvChatList.layoutManager = LinearLayoutManager(this)
        rvChatList.adapter = adapterChatBot

        btnSend.setOnClickListener {
            if(etChat.text.isNullOrEmpty()){
                Toast.makeText(this@MainActivity_chat, "Please enter a text", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            myRef.child("UserResponse").push().setValue(etChat.text.toString())
            adapterChatBot.addChatToList(ChatModel(etChat.text.toString()))
            apiService.chatWithTheBit(etChat.text.toString()).enqueue(callBack)
            etChat.text.clear()

        }

        i++
    }
     override fun onBackPressed() {


     startActivity(Intent(this@MainActivity_chat, MainActivity::class.java))

     }

    private val callBack = object  : Callback<ChatResponse>{
        override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
            var j=0
            val database = Firebase.database
            val myRef = database.getReference("ChatList").child(firebaseUser!!.uid)
            if(response.isSuccessful &&  response.body()!= null){
                Toast.makeText(this@MainActivity_chat, response.body()!!.chatBotReply, Toast.LENGTH_LONG).show()
                myRef.child("BotResponse").push().setValue(response.body()!!.chatBotReply)
                Log.d("Errror",response.body()!!.chatBotReply)
                adapterChatBot.addChatToList(ChatModel(response.body()!!.chatBotReply, true))
            }else{
                Toast.makeText(this@MainActivity_chat, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
            Log.d("Errror","error message => $t")

            Toast.makeText(this@MainActivity_chat, "Something went wrong", Toast.LENGTH_LONG).show()
        }

    }
}