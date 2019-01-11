package com.dev.reef.evote

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_nim_enter.*

class NimEnterActivity : AppCompatActivity(), View.OnClickListener {

    val dbRoot = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nim_enter)

        button.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.button -> {
                if (login_nim.text.toString().isEmpty() || login_admin.text.toString().isEmpty()) {
                    showSnackbar("Masukkan data", p0)
                    return
                }

                isValidNim(login_nim.text.toString(), p0)
            }
        }
    }

    fun isValidNim(nim : String, v : View) {
        val dbNim = dbRoot.child("mahasiswa")
        dbNim.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChild(nim)) {
                    login_nim.text.clear()
                    login_admin.text.clear()

                    val intent = Intent(applicationContext, VoteOptionsActivity::class.java)
                    intent.putExtra("nim", nim)
                    startActivity(intent)
                } else {
                    showSnackbar("Masukkan data dengan benar", v)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                showSnackbar("Masukkan data dengan benar", v)
            }

        })
    }

    fun showSnackbar(message : String, v : View) {
        Snackbar.make(
            v, // Parent view
            message, // Message to show
            Snackbar.LENGTH_SHORT // How long to display the message.
        ).show()
    }
}
