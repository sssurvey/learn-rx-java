package com.haomins.www.rxapplication

import android.util.Log
import io.reactivex.rxjava3.core.Observable
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
            .buffer(5) // change to list with size == 5
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

}