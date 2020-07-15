package com.haomins.www.rxapplication

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class RxBackPressure {

    companion object {
        const val TAG = "RxBackPressure"
    }

    private class Dish(private val id: Int) {

        // simulate memory consumption
        private val oneKb = ByteArray(1024)

        init {
            Log.d(TAG, "Created new Dish :: $id")
        }

        override fun toString(): String {
            return "Dish::$id"
        }
    }

    private val upstream = Observable.range(1, 100_000_000).map(::Dish)

    fun backPressure1() {
        upstream
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    Thread.sleep(50)
                    Log.d(RxFlowControl.TAG, "backPressure1 :: onNext called -> $it")
                },
                { Log.d(RxFlowControl.TAG, "backPressure1 :: onError called") },
                { Log.d(RxFlowControl.TAG, "backPressure1 :: onComplete called") }
            )
    }
}