package com.example.smarthomeappv3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.example.smarthomeappv3.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val buttonClick = findViewById<Button>(R.id.backToLogin)
        buttonClick.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        val registerButton = findViewById<Button>(R.id.signUpButton)
        registerButton.setOnClickListener {
            val email = binding.registerEmail.text.toString()
            val password1 = binding.registerPassword.text.toString()
            val password2 = binding.registerRePassword.text.toString()

            if(email.isNotEmpty() && password1.isNotEmpty() && password2.isNotEmpty()){
                if(password1 == password2){
                    firebaseAuth.createUserWithEmailAndPassword(email , password1).addOnCompleteListener{
                        if(it.isSuccessful){
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


