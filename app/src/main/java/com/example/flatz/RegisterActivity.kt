package com.example.flatz

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.loginscreen.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*


private lateinit var database: DatabaseReference
// ...

var maxId = 0

@IgnoreExtraProperties
data class User(
    var id: String = "",
    var email: String? = "",
    var sexo: String? = "",
    var perfil: String = ""
)

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        lastId()
        setup()
    }

    private fun setup() {

        var contador = 1
        var flag = false

        database = Firebase.database.reference

        var email=""
        var sexo=""
        var perfil=""

        imageButton.setOnClickListener {
            onBackPressed()
        }

        buttonPerfil.setOnClickListener {
            contador++
            if(contador>5)
            {
                contador=1
            }
            when (contador) {
                1 -> buttonPerfil.setText("USUARIO")
                2 -> buttonPerfil.setText("INVITADO")
                3 -> buttonPerfil.setText("ADMINISTRADOR")
                4 ->buttonPerfil.setText("ANONIMO")
                5 ->buttonPerfil.setText("TESTER")
            }
        }
        buttonRegister.setOnClickListener()
        {

            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){

                email=emailEditText.text.toString()

                if(!radioMasculino.isChecked&&!radioFemenino.isChecked){
                    spanSexo.visibility = View.VISIBLE
                }
                else if(radioMasculino.isChecked){
                    sexo="Masculino"
                    flag=true
                }
                else
                {
                    sexo="Femenino"
                    flag=true
                }

                if(flag)
                {
                    when (contador) {
                        1 -> perfil="USUARIO"
                        2 -> perfil="INVITADO"
                        3 -> perfil="ADMINISTRADOR"
                        4 -> perfil="ANONIMO"
                        5 -> perfil="TESTER"
                    }
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString()

                    ).addOnCompleteListener{
                        if(it.isSuccessful) {
                            writeNewUser((maxId+1).toString(),email,sexo,perfil)
                            showHome(it.result?.user?.email ?: "", ProviderType.EmailPassword)

                            spanEmail.visibility = View.INVISIBLE
                            spanPassword.visibility=View.INVISIBLE
                            spanSexo.visibility=View.INVISIBLE
                            emailEditText.setText("")
                            passwordEditText.setText("")
                        }else{
                            spanEmail.visibility = View.VISIBLE
                            spanPassword.visibility=View.VISIBLE
                            emailEditText.setText("")
                            passwordEditText.setText("")
                        }
                    }
                }
            }
            else
            {
                spanEmail.visibility = View.VISIBLE
                spanPassword.visibility=View.VISIBLE
            }
        }

    }

    // ...
    private fun writeNewUser(userId: String, email: String,sexo: String,perfil: String) {
        val user = User(userId,email,sexo,perfil)
        database.child("users").child(userId).setValue(user)
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent: Intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

    fun lastId(){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    maxId=(snapshot.children.count())
                }
            }
            override fun onCancelled(error: DatabaseError){
            }
        })
    }
}
