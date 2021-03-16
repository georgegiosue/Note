package com.nmrc.note

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class StartApp : AppCompatActivity() {

    private lateinit var button_next : ImageButton
    private lateinit var name : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_app)
        supportActionBar?.hide()

        val inpuntName : EditText = findViewById(R.id.etxt_name)
        button_next = findViewById(R.id.ibtn_next)

        button_next.setOnClickListener(View.OnClickListener {
            name = inpuntName.text.toString()

            if(name.isNotEmpty())
                nextMainActivity()
            else
                Snackbar.make(it, "Debe registrar su nombre", Snackbar.LENGTH_LONG).show()
        })


    }

    // CHANGE (OBJECT PERSON)
    private fun nextMainActivity(){
        val i_main : Intent = Intent(this,MainActivity::class.java).apply { putExtra("user",name) }
        startActivity(i_main)
    }
}