package com.ict.project.ewpiot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ict.project.ewpiot.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponent()
    }

    private fun initComponent() {

        binding.btnSubmit.setOnClickListener {
            btnSubmitOnClickListener()
        }
    }

    private fun btnSubmitOnClickListener() {
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            if (username.isEmpty()) {
                binding.username.error = "Please enter username."
            }
            if (password.isEmpty()) {
                binding.passwordLayout.isPasswordVisibilityToggleEnabled = false
                binding.password.error = "Please enter a password."
            }
            return
        }
        if(username.compareTo("admin") == 0 && password.compareTo("admin") == 0){
            Toast.makeText(this, "Welcome to EWT-IoT Project!", Toast.LENGTH_SHORT).show()
            startActivity((Intent(this, DashboardActivity::class.java)))
            finish()
        }else{
            Toast.makeText(this, "Either your Username or Password is Invalid!", Toast.LENGTH_SHORT).show()
        }
    }
}