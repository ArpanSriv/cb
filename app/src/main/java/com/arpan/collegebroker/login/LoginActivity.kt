package com.arpan.collegebroker.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.arpan.collegebroker.MainActivity
import com.arpan.collegebroker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            handleLogin()
        }

        registerButton.setOnClickListener {
            handleRegister()
        }
    }

    private fun handleLogin() {
        if (login_user_autocomplete.text != null && login_pass_editText.text != null) {
            signIn(login_user_autocomplete.text.toString(), login_pass_editText.text.toString())
        } else Toast.makeText(this, "Please ensure none of the fields are empty.", Toast.LENGTH_SHORT).show()
    }

    private fun handleRegister() {
        if (register_user_autocomplete.text.toString() != "" && register_password.text.toString() != "" && register_conf_password.text.toString() != "") {
            if (register_password.text.toString() == register_conf_password.text.toString()) {
                signUp(register_user_autocomplete.text.toString(), register_password.text.toString())
            } else Toast.makeText(this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this, "Please ensure none of the fields are empty.", Toast.LENGTH_SHORT).show()
    }

    private fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            //TODO PROGRESS BUTTON
            signIn(email, password)
        }
    }

    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mAuth.currentUser
                        proceed(user!!)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }

                    // ...
                })
    }

    private fun proceed(user: FirebaseUser) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }

    var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            return
        }

        fabRevealLayout!!.revealMainView()
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}