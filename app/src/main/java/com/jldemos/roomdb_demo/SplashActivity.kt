package com.jldemos.roomdb_demo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Handler().postDelayed({
            launchApp()
        },1500)
    }

    private fun launchApp() {
        Timber.d("Launching App ...")
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainIntent)
        overridePendingTransition(
            R.anim.fade_in,
            R.anim.fade_out
        )
        finish()
    }
}