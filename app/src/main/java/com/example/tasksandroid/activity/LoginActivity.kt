package com.example.tasksandroid.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tasksandroid.MainActivity
import com.example.tasksandroid.databinding.ActivityLoginBinding
import com.example.tasksandroid.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBuilder: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBuilder = ActivityLoginBinding.inflate(layoutInflater)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setContentView(loginBuilder.root)

        loginBuilder.loginButton.setOnClickListener {
            loginViewModel.doLogin(
                loginBuilder.editTextEmailAddress.text.toString(),
                loginBuilder.editTextPassword.text.toString()
            )
        }

        loginBuilder.cadastraSe.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }

        loginViewModel.verifyLoggedUser()
        observes()
    }

    override fun onStart() {
        super.onStart()
        loginViewModel.verifyLoggedUser()
    }

    private fun observes(){
        loginViewModel.login.observe(this){
            if (it.status()){
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_LONG).show()
            }
        }

        loginViewModel.loggedUser.observe(this){
            if(it){
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }
    }
}