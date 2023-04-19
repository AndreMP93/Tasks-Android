package com.example.tasksandroid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tasksandroid.databinding.ActivityRegisterBinding
import com.example.tasksandroid.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var builder: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        builder = ActivityRegisterBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        setContentView(builder.root)

        builder.registerButton.setOnClickListener {
            viewModel.register(
                builder.editName.text.toString(),
                builder.editEmail.text.toString(),
                builder.editPassword.text.toString()
            )
        }

        observes()

    }

    private fun observes(){
        viewModel.user.observe(this){
            if (it.status()){
                finish()
            }else{
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_LONG).show()
            }
        }
    }
}