package com.haomins.www.rxapplication

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception
import java.util.concurrent.TimeUnit

class RxOne {

    private val compositeDisposable = CompositeDisposable()

    companion object {
        private const val TAG = "RxOne"
    }

    fun delayOperator() {
        val disposable = Observable.create<Long> {
            Thread.sleep(10000L)
            it.onNext(0L)
            it.onComplete()
        }.flatMap {
            Log.d(TAG, "flatMap took initial $it == 0")
            Observable.just(1, 2, 3)
        }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            { Log.d(TAG, "onNext: $it") },
            { Log.d(TAG, "onError: ${it.printStackTrace()}") },
            { Log.d(TAG, "onComplete: Done") }
        )
        /* val disposable = Observable.timer(10L, TimeUnit.SECONDS).doOnNext {
            Log.d(TAG, "timer onNext $it == 0")
        }.flatMap {
            Log.d(TAG, "flatMap took initial $it == 0")
            Observable.just(1, 2, 3)
        }.subscribe(
            { Log.d(TAG, "onNext: $it") },
            { Log.d(TAG, "onError: ${it.printStackTrace()}") },
            { Log.d(TAG, "onComplete: Done") }
        ) */
        compositeDisposable.add(disposable)
    }

    fun flatMapNoOrder() {
        Log.d(TAG, "flatMapNoOrder() starts")
        val disposable = Observable.just(1, 2).flatMap(this::doIf).observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d(TAG, "onNext:: $it")
            }, {
                Log.d(TAG, "onError:: Ignored")
            }, {
                Log.d(TAG, "onComplete flatMapNoOrder()")
                //Trigger concatMap demo
                concatMapKeepOrder()
            })
        compositeDisposable.add(disposable)
    }

    fun flatMapIterable() {
        Log.d(TAG, "flatMapIterable() starts")
        val disposable = Observable.just(
            "a" to listOf(1,2),
            "b" to listOf(3,4),
            "c" to listOf(5,6),
            "d" to listOf(7,8)
        )
            .flatMapIterable { it.second }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    Log.d(TAG, "flatMapIterable() onNext:: $it")
                },
                {
                    Log.d(TAG, "flatMapIterable() onError:: Ignored")
                },
                {
                    Log.d(TAG, "onComplete flatMapIterable()")
                }
            )
        compositeDisposable.add(disposable)
    }

    private fun concatMapKeepOrder() {
        Log.d(TAG, "concatMapKeepOrder() starts")
        val disposable = Observable.just(1, 2).concatMap(this::doIf).observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d(TAG, "onNext:: $it")
            }, {
                Log.d(TAG, "onError:: Ignored")
            }, {
                Log.d(TAG, "onComplete concatMapKeepOrder()")
            })
        compositeDisposable.add(disposable)
    }

    private fun doIf(int: Int): Observable<String> {
        return when (int) {
            1 -> Observable.interval(500, TimeUnit.MILLISECONDS).take(5).map { "ONE: $it" }
            2 -> Observable.interval(250, TimeUnit.MILLISECONDS).take(5).map { "TWO: $it" }
            else -> Observable.interval(150, TimeUnit.MILLISECONDS).take(5).map { "NULL: $it" }
        }
    }

    fun clear() {
        if (!compositeDisposable.isDisposed) compositeDisposable.clear()
    }
}