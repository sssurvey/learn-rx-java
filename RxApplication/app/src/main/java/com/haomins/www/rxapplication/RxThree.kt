package com.haomins.www.rxapplication

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class RxThree {

    companion object {
        const val TAG = "RxThree"
    }

    fun learnScan() {
        val observableProgress =
            Observable.interval(100L, TimeUnit.MILLISECONDS).take(10).doOnNext {
                Log.d(TAG, "learnScan() :: emitting chunk $it")
            }
        val observableTotalProgress =
            observableProgress.scan(55L) { total: Long, chunk: Long ->
                total + chunk
            }
        observableTotalProgress.doOnNext { Log.d(TAG, "learnScan() :: total: $it") }.subscribe()
    }

    fun learnReduced() {
        val observableProgress =
            Observable.interval(100L, TimeUnit.MILLISECONDS).take(10).doOnNext {
                Log.d(TAG, "learnReduced() :: emitting value $it")
            }
        val observableTotalProgress = // seed is the initial value for reduced() like scan()
            observableProgress.reduce(55) { total: Long, chunk ->
                total + chunk
            }
        observableTotalProgress.doOnSuccess {
            Log.d(TAG, "learnReduced() :: completed with value $it")
        }.subscribe()
    }

    fun learnCollect() {
        val observableProgress =
            Observable.interval(100L, TimeUnit.MILLISECONDS).take(10).doOnNext {
                Log.d(TAG, "learnCollect() :: emitting value $it")
            }
        observableProgress.collect({ ArrayList<Long>() }, { list, value ->
            list.add(value)
        }).doOnSuccess {
            Log.d(TAG, "learnCollect() :: done value: list size = ${it.size} List is ->$it")
        }.subscribe()
    }

    fun learnDistinct() {
        Observable.interval(100L, TimeUnit.MILLISECONDS).take(10).flatMap {
            Observable.just(it, it)
        }.distinct().doOnNext {
            Log.d(TAG, "learnDistinct() :: emitting value $it")
        }.subscribe()
    }

    fun learnDistinctUntilChanged() {
        Observable.interval(100L, TimeUnit.MILLISECONDS).take(10).flatMap {
            Observable.just(it, it)
        }.distinctUntilChanged().doOnNext {
            Log.d(TAG, "learnDistinctUntilChanged() :: emitting value $it")
        }.subscribe()
    }
}