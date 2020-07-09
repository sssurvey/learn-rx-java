package com.haomins.www.rxapplication

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RxFlowControl {

    companion object {
        const val TAG = "RxFlowControl"
    }

    fun buffer1() {
        Observable
            .interval(1, TimeUnit.SECONDS)
            .take(15)
//            .buffer(5, { HashSet<Long>() }) // change to hash set with size == 5
            .buffer(5) // change to list with size == 5, default to list...
            .subscribeOn(Schedulers.io())
            .subscribe(
                { Log.d(TAG, "buffer1 :: onNext called -> $it") },
                { Log.d(TAG, "buffer1 :: onError called") },
                { Log.d(TAG, "buffer1 :: onComplete called") }
            )
    }

    fun buffer2() {
        /**
         * Buffer to list with size = 5
         * And with each new value, the first value in the list is kicked out of the array, with
         * array size decrease by 1 for each new value starting with the last 5th value.
         * And for the first five value, the list will not be emitted until first 5 value is emitted.
         */
        Observable
            .interval(500, TimeUnit.MILLISECONDS)
            .take(15)
            .buffer(5, 1)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { Log.d(TAG, "buffer2 :: onNext called -> $it") },
                { Log.d(TAG, "buffer2 :: onError called") },
                { Log.d(TAG, "buffer2 :: onComplete called") }
            )
    }

    fun buffer3() {
        val names = Observable
            .just(
                "Marry", "Harry", "Terry", "Jerry", "Larry",
                "Berry", "Lorry", "Henry", "Rob", "Bob"
            )
        val delays = Observable
            .just(
                100L, 600L, 900L, 1100L, 3300L,
                3400L, 3500L, 3600L, 4400L, 4800L
            )
        val delayedNames = Observable
            .zip(
                names,
                delays,
                BiFunction { name: String, delay: Long -> Observable.just(name).delay(delay, TimeUnit.MILLISECONDS) }
            )
        delayedNames
            .flatMap { it }
            .buffer(1L, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { Log.d(TAG, "buffer3 :: onNext called -> $it ")},
                { Log.d(TAG, "buffer3 :: onError called") },
                { Log.d(TAG, "buffer3 :: onCompleted called") }
            )
    }

    fun window1() {
        Observable
            .interval(500, TimeUnit.MILLISECONDS)
            .take(15)
            // use the window to get a new Observable that will emit values in the time span
            .window(1L, TimeUnit.SECONDS)
            .doOnNext { Log.d(TAG, "window1 :: peaking list length -> ${it.count()}") }
            // convert the new observable to list
            .map { it.toList() }
            .flatMapSingle { it }
            .subscribeOn(Schedulers.io())
            .subscribe(
                // Now we can get the same output as buffer
                { Log.d(TAG, "window1 :: onNext called -> $it") },
                { Log.d(TAG, "window1 :: onError called") },
                { Log.d(TAG, "window1 :: onComplete called") }
            )
    }

}