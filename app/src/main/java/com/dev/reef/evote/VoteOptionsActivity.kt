package com.dev.reef.evote

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_vote_options.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class VoteOptionsActivity : AppCompatActivity(), View.OnClickListener {

    var nim : String? = null
    val dbRoot = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote_options)

        try {
            nim = intent.getStringExtra("nim").toString()
        } catch (e : Exception) {
            nim = ""
        }

        vote_presma.setOnClickListener(this)
        vote_senat.setOnClickListener(this)

        checkTime()
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.vote_presma -> {
                val intent = Intent(applicationContext, VoteActivity::class.java)
                intent.putExtra("nim", nim)
                intent.putExtra("voteOption", "calonPresma")
                startActivity(intent)
            }

            R.id.vote_senat -> {
                val intent = Intent(applicationContext, VoteActivity::class.java)
                intent.putExtra("nim", nim)
                intent.putExtra("voteOption", "calonSenat")
                startActivity(intent)
            }
        }
    }

    fun checkTime() {
        val timeNow = SimpleDateFormat("HHmm").format(Calendar.getInstance().getTime()).toInt()

        val timeDb = dbRoot.child("waktu")
        timeDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(timeNow < p0.child("mulai").value.toString().toInt() || timeNow > p0.child("selesai").value.toString().toInt()) {
                    vote_time_limit.visibility = View.VISIBLE
                    mainView.visibility = View.GONE
                    vote_time_limit_text.text = "Vote saat ini sedang ditutup"
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
}
