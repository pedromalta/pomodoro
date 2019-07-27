package net.clubedocomputador.pomodoro.extensions

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity


fun AppCompatActivity.getString(@StringRes resId: Int): String = resources.getString(resId)

fun AppCompatActivity.getString(@StringRes resId: Int, vararg args: Any): String = resources.getString(resId, *args)

