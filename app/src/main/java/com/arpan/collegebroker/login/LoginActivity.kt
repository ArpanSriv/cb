package com.arpan.collegebroker.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import io.codetail.animation.RevealViewGroup
import io.codetail.animation.ViewAnimationUtils
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import carbon.widget.FrameLayout
import com.arpan.collegebroker.MainActivity
import com.arpan.collegebroker.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.codetail.widget.RevealFrameLayout
import kotlinx.android.synthetic.main.activity_login.*
import com.arpan.collegebroker.R.id.target
import android.view.animation.AccelerateDecelerateInterpolator
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation


class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        registerRevealButton.setOnClickListener {

            //            val myView = target
//
//            val cx = (registerRevealButton.left + registerRevealButton.right) / 2
//            val cy = (registerRevealButton.top + registerRevealButton.bottom) / 2
//
//            // get the final radius for the clipping circle
//            val dx = Math.max(cx, myView.width - cx)
//            val dy = Math.max(cy, myView.height - cy)
//            val finalRadius = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()
//
//            // Android native animator
//            val valueAnimator = ValueAnimator.ofFloat(0f, -((target.left + target.right) / 2).toFloat())
//            valueAnimator.addUpdateListener {
//                val value = it.animatedValue as Float
//                registerRevealButton.translationY = value
//            }
//
//            // 2 - Here set your favorite interpolator
//            valueAnimator.interpolator = LinearInterpolator()
//            valueAnimator.duration = 1000
//
//            // 3
//            valueAnimator.start()
//            registerRevealButton.hide()
//
////            registerRevealButton.animate().translationY(((target.left + target.right) / 2).toFloat()).setDuration(100).start()
//
//            val animator = ViewAnimationUtils.createCircularReveal(myView, myView.x.toInt(), myView.y.toInt(), 0f, finalRadius)
//            animator.interpolator = AccelerateDecelerateInterpolator()
//            animator.duration = 500
//            animator.startDelay = 1000
            source.visibility = View.INVISIBLE
            target.visibility = View.VISIBLE
            registerRevealButton.rotation = -90f
            registerRevealButton.setImageDrawable(resources.getDrawable(R.drawable.ic_arrow_forward_white_24dp))
//            animator.start()
        }

        backToLoginButton.setOnClickListener {
            source.visibility = View.VISIBLE
            target.visibility = View.INVISIBLE
            registerRevealButton.rotation = -90f
            registerRevealButton.setImageDrawable(resources.getDrawable(R.drawable.ic_add_white_24dp))
        }

        loginButton.setOnClickListener {
            signIn(login_user_autocomplete.text.toString(), login_pass_editText.text.toString())
        }

    }

    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
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


}