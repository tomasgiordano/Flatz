package com.example.flatz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.flatz.Feas.VerGaleriaFeaActivity
import com.example.flatz.Lindas.VerGaleriaLindaActivity
import com.example.loginscreen.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType {
    EmailPassword
}
var email = ""

public class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Setup
        val bundle = intent.extras
        email = bundle?.getString("email").toString()
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
            val intent = Intent(this, VerGaleriaLindaActivity::class.java).apply{
                putExtra("email", email)
            }
            startActivity(intent)
        }

        imageViewFeas.setOnClickListener{
            val intent = Intent(this, VerGaleriaFeaActivity::class.java).apply{
                putExtra("email", email)
            }
            startActivity(intent)
        }
    }
}