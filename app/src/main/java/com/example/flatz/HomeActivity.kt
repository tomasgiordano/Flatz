package com.example.flatz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginscreen.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.concurrent.thread

enum class ProviderType {
    EmailPassword
}

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        setup(email ?: "")
    }

    private fun setup(email: String)
    {
        title = "Home"
        emailTextView.text = email

        logOutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        imageViewLindas.setOnClickListener{
            val intent = Intent(this, CosasLindasActivity::class.java)
            startActivity(intent)
        }
    }
}