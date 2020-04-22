package com.haomins.www.rxapplication

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RxTwo {

    companion object {
        const val TAG = "RxTwo"
    }

    fun learnZipOperator() {
        val observable1 = Observable.interval(1000L, TimeUnit.MILLISECONDS).take(5)
        val observable2 = Observable.interval(1500L, TimeUnit.MILLISECONDS).take(5)
        Observable.zip(observable1, observable2, BiFunction { o1: Long, o2: Long ->
            return@BiFunction "o1: $o1, o2: $o2"
        }).map {
            Log.d(TAG, "from learnZipOperator :: $it")
        }.doOnComplete {
            Log.d(TAG, "from learnZipOperator :: complete")
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    /**
     * Consider what if one emitter 2 times faster then the other one, how does the zip handle it?
     */
    fun learnZipOperator2() {
        val observable1 = Observable.interval(1000L, TimeUnit.MILLISECONDS).take(5)
        val observable2 = Observable.interval(300L, TimeUnit.MILLISECONDS).take(5)
        Observable.zip(observable1, observable2, BiFunction { o1: Long, o2: Long ->
            return@BiFunction "o1: $o1, o2: $o2"
        }).map {
            Log.d(TAG, "from learnZipOperator2 duplicate test:: $it")
        }.doOnComplete {
            Log.d(TAG, "from learnZipOperator2 duplicate test:: complete")
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    /**
     * Consider what if one emitter emit more value then the other?
     */
    fun learnZipOperator3() {
        val observable1 = Observable.interval(1000L, TimeUnit.MILLISECONDS).take(2)
        val observable2 = Observable.interval(300L, TimeUnit.MILLISECONDS).take(5)
        Observable.zip(observable1, observable2, BiFunction { o1: Long, o2: Long ->
            return@BiFunction "o1: $o1, o2: $o2"
        }).map {
            Log.d(TAG, "from learnZipOperator3 different amount of event test:: $it")
        }.doOnComplete {
            Log.d(TAG, "from learnZipOperator3 different amount of event test:: complete")
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    /**
     * Consider how is combineLatest behaved.
     * When running this, notice that the first 2 values from the Observable 1 has been ditched, so
     * keep this in mind about the combine latest operator. Since at the original first 2 emits from
     * observable 1, the observable 2 has not emit anything yet,that is why the first 2 emits from o1
     * is ditched...
     */
    fun learnCombineLatest() {
        val observable1 = Observable.interval(300L, TimeUnit.MILLISECONDS).take(10)
        val observable2 = Observable.interval(900L, TimeUnit.MILLISECONDS).take(10)
        Observable.combineLatest(observable1, observable2, BiFunction { o1: Long, o2: Long ->
            return@BiFunction "o1: $o1, o2: $o2"
        }).map { Log.d(TAG, "from learnCombineLatest withCombineLatest :: $it") }
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    /**
     * withLatestFrom() operator
     */
    fun learnWithLatestFrom() {
        val observable1 = Observable.interval(300L, TimeUnit.MILLISECONDS).take(10)
        val observable2 = Observable.interval(900L, TimeUnit.MILLISECONDS).take(10)
        observable2.withLatestFrom(observable1, BiFunction { o1: Long, o2: Long ->
            "o1: $o1, o2: $o2"
        }).map { Log.d(TAG, "from learnWithLatestFrom :: $it") }
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    /**
     * learnAmb(), this operator will amplify the first event stream that emit the first event
     */
    fun learnAmp() {
        val observable1 = Observable.interval(300L, TimeUnit.MILLISECONDS).take(10).map { "O1: $it" }
        val observable2 = Observable.interval(200L, TimeUnit.MILLISECONDS).take(10).map { "O2: $it" }
        observable1.doOnSubscribe { Log.d(TAG, "learnAmp, observable1 subscribed") }
        observable1.doOnDispose { Log.d(TAG, "learnAmp, observable1 disposed") }
        observable2.doOnSubscribe { Log.d(TAG, "learnAmp, observable2 subscribed") }
        observable2.doOnDispose { Log.d(TAG, "learnAmp, observable2 disposed") }
        observable1.ambWith(observable2).subscribe {
            Log.d(TAG, "learnAmp, emitting: $it")
        }
    }


}