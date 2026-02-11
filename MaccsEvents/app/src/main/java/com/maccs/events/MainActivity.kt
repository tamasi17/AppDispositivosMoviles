package com.maccs.events

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.maccs.events.ui.auth.LoginActivity
import com.maccs.events.ui.home.HomeActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val currentUser = FirebaseAuth.getInstance().currentUser

        val intent = if (currentUser != null) {

            Intent(this, HomeActivity::class.java)
        } else {

            Intent(this, LoginActivity::class.java)
        }


        startActivity(intent)

        finish()
    }
}