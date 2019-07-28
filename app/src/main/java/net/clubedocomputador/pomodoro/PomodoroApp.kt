package net.clubedocomputador.pomodoro

import android.app.Application
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.realm.Realm
import io.realm.RealmConfiguration
import net.clubedocomputador.pomodoro.persistence.PersistenceProvider
import net.clubedocomputador.pomodoro.services.timer.ITimerProvider
import net.clubedocomputador.pomodoro.services.timer.TimerService
import net.clubedocomputador.pomodoro.util.Analytics


class PomodoroApp : Application() {

    companion object {
        lateinit var instance: PomodoroApp
        lateinit var timer: ITimerProvider
        lateinit var persistence: PersistenceProvider
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        timer = TimerService

        configCrashReport()
        configFonts()
        configPersistence()
    }

    private fun configFonts() {
        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/SpaceMono-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build())
    }

    private fun configPersistence() {
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder().apply {
            name("pomodoro")
            schemaVersion(1)
            if (BuildConfig.DEBUG) {
                deleteRealmIfMigrationNeeded()
            }
        }.build()
        Realm.setDefaultConfiguration(realmConfig)

        persistence = PersistenceProvider()
    }

    private fun configCrashReport() {
        Analytics.configCrashReport(this)
    }

}