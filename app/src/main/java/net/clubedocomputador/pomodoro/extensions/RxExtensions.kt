package net.clubedocomputador.pomodoro.extensions

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import net.clubedocomputador.pomodoro.util.Analytics
import java.util.concurrent.TimeUnit

/**
 * Faremos Retry a quantidade de vezes definida e o intervalo de tempo irá aumentando em passos definidos
 */
fun <T> Single<T>.retryWithExponentialDelay(times: Int, exponentialDelay: Int): Single<T> {
    return this.retryWhen { errors: Flowable<Throwable> ->
        errors.zipWith(
                Flowable.range(0, times),
                BiFunction<Throwable, Int, Int> { error: Throwable, retryCount: Int ->
                    //ComputedErrors.compute(ComputeError(error))
                    if (retryCount >= times - 1) {
                        throw error
                    } else {
                        retryCount
                    }
                }
        ).concatMap { retryCount: Int ->
            Flowable.timer(
                    exponentialDelay * retryCount.toLong(),
                    TimeUnit.MILLISECONDS
            )
        }
    }
}

fun <T> Single<T>.logErrorAndRetryWithDelay(logMessage: String, delay: Long, unit: TimeUnit): Single<T> {
    return this.retryWhen { error ->
        error.flatMap {
            Analytics.logError(logMessage, it)
            Flowable.just(it).delay(delay, unit)
        }
    }
}