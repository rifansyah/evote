package com.dev.reef.evote

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var passwordAdmin : String

    lateinit var dbRoot : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbRoot = FirebaseDatabase.getInstance().reference

        getPasswordData()

        button.setOnClickListener(this)
    }

    fun getPasswordData() {
        var dbAdmin = dbRoot.child("admin")
        dbAdmin.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                passwordAdmin = p0.child("password").value.toString()

                if (passwordAdmin.equals("")) {
                    passwordAdmin = "admin"
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.button -> {
                if (login_username.text.toString().equals("") || login_password.text.toString().equals("") || !login_username.text.toString().equals("admin", true) || !login_password.text.toString().equals(passwordAdmin)) {
                    showSnackbar("Masukkan data dengan benar", p0)
                    return
                }

                var intent = Intent(applicationContext, NimEnterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun showSnackbar(message : String, v : View) {
        Snackbar.make(
            v, // Parent view
            message, // Message to show
            Snackbar.LENGTH_SHORT // How long to display the message.
        ).show()
    }
}
