package com.dev.reef.evote

import android.app.Dialog
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_vote.*
import java.lang.Exception
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import kotlinx.android.synthetic.main.pop_up_detail_calon.*
import android.widget.Toast
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*


class VoteActivity : AppCompatActivity(), View.OnClickListener {

    val dbRoot = FirebaseDatabase.getInstance().reference
    var option : String? = null
    var nim : String? = null

    var myDialog: Dialog? = null

    var listCalon : ArrayList<Calon> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)

        myDialog = Dialog(this);

        try {
            option = intent.getStringExtra("voteOption").toString()
            nim = intent.getStringExtra("nim").toString()
        } catch (e : Exception) {
            option = ""
            nim = ""
        }

        isAlreadyVote(nim!!)

        if (option.equals("calonPresma", true)) {
            getPresmaData()
        } else if (option.equals("calonSenat", true)) {
            getSenatData()
        }

        calon_1.setOnClickListener(this)
        calon_2.setOnClickListener(this)
        calon_3.setOnClickListener(this)
        vote_1.setOnClickListener(this)
        vote_2.setOnClickListener(this)
        vote_3.setOnClickListener(this)
        vote_finish.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.calon_1 -> {
                showPopup(0)
            }

            R.id.calon_2 -> {
                showPopup(1)
            }

            R.id.calon_3 -> {
                showPopup(2)
            }

            R.id.vote_1 -> {
                voteConfirmation(1)
            }

            R.id.vote_2 -> {
                voteConfirmation(2)
            }

            R.id.vote_3 -> {
                voteConfirmation(3)
            }

            R.id.vote_finish -> {
                finish()
            }
        }
    }

    fun isAlreadyVote(nim : String) {
        val dbMahasiswa = dbRoot.child("vote").child(option!!)
        dbMahasiswa.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChild(nim)) {
                    vote_limit.visibility = View.VISIBLE
                    mainView.visibility = View.GONE
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    fun voteConfirmation(i : Int) {
        AlertDialog.Builder(this)
            .setTitle("Ingin melakukan vote?")
            .setMessage("Kamu tidak akan bisa melakukan vote lagi setelah menekan 'ya'")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Ya",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    dbRoot.child("vote").child(option!!).child(nim!!).setValue(i)
                    vote_confirmation.visibility = View.VISIBLE
                    mainView.visibility = View.GONE
                })
            .setNegativeButton("Tidak", null).show()
    }

    fun showPopup(i: Int) {
        myDialog!!.setContentView(R.layout.pop_up_detail_calon)

        myDialog!!.name_pop_up.setText(listCalon.get(i).name)
        myDialog!!.visi_pop_up.setText(listCalon.get(i).visi)
        myDialog!!.misi_pop_up.setText(listCalon.get(i).misi)
        myDialog!!.organisasi_pop_up.setText(listCalon.get(i).organization)

        Glide
            .with(applicationContext)
            .load(listCalon[i].imgUrl)
            .into(myDialog!!.photo_profile_pop_up);

        myDialog!!.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog!!.show()
    }

    fun getPresmaData() {
        listCalon.clear()
        val dbPresma = dbRoot.child("calonPresma")
        dbPresma.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                var size = 1
                p0.children.mapNotNull {
                    if (size == 1) {
                        var calon = Calon(it.child("name").value.toString(), it.child("visi").value.toString(), it.child("misi").value.toString(), it.child("urlToImage").value.toString(), it.child("organizationalExperiences").value.toString())
                        listCalon.add(calon)
                        setToLayout1(it.child("name").value.toString(), it.child("visi").value.toString(), it.child("misi").value.toString(), it.child("urlToImage").value.toString())
                    } else if (size == 2) {
                        var calon = Calon(it.child("name").value.toString(), it.child("visi").value.toString(), it.child("misi").value.toString(), it.child("urlToImage").value.toString(), it.child("organizationalExperiences").value.toString())
                        listCalon.add(calon)
                        setToLayout2(it.child("name").value.toString(), it.child("visi").value.toString(), it.child("misi").value.toString(), it.child("urlToImage").value.toString())
                    } else if (size == 3) {
                        calon_3.visibility = View.VISIBLE
                        var calon = Calon(it.child("name").value.toString(), it.child("visi").value.toString(), it.child("misi").value.toString(), it.child("urlToImage").value.toString(), it.child("organizationalExperiences").value.toString())
                        listCalon.add(calon)
                        setToLayout3(it.child("name").value.toString(), it.child("visi").value.toString(), it.child("misi").value.toString(), it.child("urlToImage").value.toString())
                    }
                    size += 1
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    fun getSenatData() {
        listCalon.clear()
        val dbSenat = dbRoot.child("calonSenat")
        dbSenat.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                var size = 1
                p0.children.mapNotNull {
                    if (size == 1) {
                        var calon = Calon(it.child("name").value.toString(), it.child("visi").value.toString(), it.child("misi").value.toString(), it.child("urlToImage").value.toString(), it.child("organizationalExperiences").value.toString())
                        listCalon.add(calon)
                        setToLayout1(it.child("name").value.toString(), it.child("visi").value.toString(), it.child("misi").value.toString(), it.child("urlToImage").value.toString())
                    } else if (size == 2) {
                        var calon = Calon(it.child("name").value.toString(), it.child("visi").value.toString(), it.child("misi").value.toString(), it.child("urlToImage").value.toString(), it.child("organizationalExperiences").value.toString())
                        listCalon.add(calon)
                        setToLayout2(it.child("name").value.toString(), it.child("visi").value.toString(), it.child("misi").value.toString(), it.child("urlToImage").value.toString())
                    } else if (size == 3) {
                        var calon = Calon(it.child("name").value.toString(), it.child("visi").value.toString(), it.child("misi").value.toString(), it.child("urlToImage").value.toString(), it.child("organizationalExperiences").value.toString())
                        listCalon.add(calon)
                        calon_3.visibility = View.VISIBLE
                        setToLayout3(it.child("name").value.toString(), it.child("visi").value.toString(), it.child("misi").value.toString(), it.child("urlToImage").value.toString())
                    }
                    size += 1
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    fun setToLayout1(name : String, visi : String, misi : String, imgUrl : String) {
        name_1.setText(name)
        visi_1.setText(visi)
        misi_1.setText(misi)

        Glide
            .with(applicationContext)
            .load(imgUrl)
            .into(photo_profile_1);
    }

    fun setToLayout2(name : String, visi : String, misi : String, imgUrl : String) {
        name_2.setText(name)
        visi_2.setText(visi)
        misi_2.setText(misi)

        Glide
            .with(applicationContext)
            .load(imgUrl)
            .into(photo_profile_2);
    }

    fun setToLayout3(name : String, visi : String, misi : String, imgUrl : String) {
        name_3.setText(name)
        visi_3.setText(visi)
        misi_3.setText(misi)

        Glide
            .with(applicationContext)
            .load(imgUrl)
            .into(photo_profile_3);
    }
}
