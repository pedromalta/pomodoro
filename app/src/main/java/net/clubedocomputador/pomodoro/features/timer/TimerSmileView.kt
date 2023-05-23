package net.clubedocomputador.pomodoro.features.timer

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import net.clubedocomputador.pomodoro.R
import org.jetbrains.anko.imageResource

class TimerSmileView : ImageView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        imageResource = R.drawable.smile
        showInactive()
    }
    fun hide() {
        visibility = View.INVISIBLE
        setColor(R.color.grey)
    }

    fun showActive() {
        visibility = View.VISIBLE
        setColor(R.color.red)
    }

    fun showInactive() {
        visibility = View.VISIBLE
        setColor(R.color.grey)
    }

    fun reactToTimer(timer: String, isRunning: Boolean) {
        if (timer.length == 5 && timer.substring(3, 5) == "00") {
            if (isRunning) {
                showActive()
            } else {
                showInactive()
            }
        } else {
            hide()
        }
    }

    private fun setColor(@ColorRes color: Int) {
        val colorResource = ContextCompat.getColor(context, color)
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(colorResource))
    }
}
