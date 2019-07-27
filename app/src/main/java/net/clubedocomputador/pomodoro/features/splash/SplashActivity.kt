package net.clubedocomputador.pomodoro.features.splash

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import net.clubedocomputador.pomodoro.features.base.BaseActivity
import net.clubedocomputador.pomodoro.features.principal.PrincipalActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }



}