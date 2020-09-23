package com.example.flatz

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.loginscreen.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setup()
    }

    private fun setup()
    {
        signUpButton.setOnClickListener{
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }

        logInButton.setOnClickListener{
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                ).addOnCompleteListener{
                    if(it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.EmailPassword)
                        txtError.visibility = View.INVISIBLE
                        emailEditText.setText("")
                        passwordEditText.setText("")
                    }else{
                        txtError.visibility = View.VISIBLE
                        txtError.text = " ERROR! Email y/o contraseña invalido"
                        emailEditText.setText("")
                        passwordEditText.setText("")
                    }
                }
            }
            else
            {
                txtError.visibility = View.VISIBLE
                txtError.text = " ERROR! Campos vacíos"
            }
        }

        buttonAdmin.setOnClickListener{
            emailEditText.setText("admin@admin.com")
            passwordEditText.setText("111111")
        }

        buttonInvitado.setOnClickListener{
            emailEditText.setText("invitado@invitado.com")
            passwordEditText.setText("222222")
        }

        buttonUsuario.setOnClickListener{
            emailEditText.setText("usuario@usuario.com")
            passwordEditText.setText("333333")
        }

        buttonAnonimo.setOnClickListener{
            emailEditText.setText("anonimo@anonimo.com")
            passwordEditText.setText("444444")
        }

        buttonTester.setOnClickListener{
            emailEditText.setText("tester@tester.com")
            passwordEditText.setText("555555")
        }

        emailEditText

    }

    private fun showAlert()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("OK", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent: Intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}