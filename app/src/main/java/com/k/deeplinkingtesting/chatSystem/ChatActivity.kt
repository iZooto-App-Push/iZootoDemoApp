package com.k.deeplinkingtesting.chatSystem

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.k.deeplinkingtesting.R

class ChatActivity : AppCompatActivity() {

    private lateinit var adapter: ChatAdapter
    private val db = FirebaseFirestore.getInstance()

    private val senderId = "userA"
    private val receiverId = "userB"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_system_ui)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSend = findViewById<Button>(R.id.btnSend)

        adapter = ChatAdapter(mutableListOf())
        recyclerView.adapter = adapter

        // 🔥 Listen for messages (Realtime)
        listenMessages()

        btnSend.setOnClickListener {
            val text = etMessage.text.toString()
            if (text.isNotEmpty()) {
                sendMessage(text)
                Log.e("ChatSystem", "btnSend: $text")
                etMessage.setText("")
            }
        }
    }

    // 📩 Send Message to Firestore
    private fun sendMessage(text: String) {

        val message = Message(
            senderId = senderId,
            receiverId = receiverId,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        Log.d("ChatSystem", "📤 Sending message...")

        db.collection("chats")
            .add(message)
            .addOnSuccessListener {
                Log.d("ChatSystem", "✅ Message sent successfully")
            }
            .addOnFailureListener {
                Log.e("ChatSystem", "❌ Error: ${it.message}")
            }
    }

    // 🔄 Realtime Listener
    private fun listenMessages() {

        db.collection("chats")
            .orderBy("timestamp")
            .addSnapshotListener { value, _ ->

                if (value != null) {
                    for (doc in value.documentChanges) {

                        if (doc.type == DocumentChange.Type.ADDED) {
                            val msg = doc.document.toObject(Message::class.java)
                            adapter.addMessage(msg)
                        }
                    }
                }
            }
    }

    // 🔔 Push Notification (simplified)
    private fun sendPushNotification(text: String) {
        println("📤 Send push via backend (not directly from app)")
    }
}