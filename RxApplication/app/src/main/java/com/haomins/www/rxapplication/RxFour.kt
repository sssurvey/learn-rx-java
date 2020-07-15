package com.haomins.www.rxapplication

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RxFour {

    companion object {
        const val TAG = "RxFour"
    }

    fun learnTake() {
        Observable
            .range(1, 5) //Notice the range does not include last number
            .take(3)
            .doOnNext { Log.d(TAG, "learnTake() :: onNext $it") }
            .subscribe()
    }

    fun learnSkip() {
        Observable
            .range(0, 20)
            .skip(15)
            .doOnNext { Log.d(TAG, "learnSkip() :: onNext $it") }
            .subscribe()
    }

    fun learnTakeLast() {
        Observable
            .range(0, 10)
            .takeLast(3)
            .doOnNext { Log.d(TAG, "learnTakeLast() :: onNext $it") }
            .subscribe()
    }

    fun learnSkipLast() {
        Observable
            .range(0, 10)
            .skipLast(4)
            .doOnNext { Log.d(TAG, "learnSkipLast() :: onNext $it") }
            .subscribe()
    }

    fun learnFirst() {
        Observable
            .range(0, 10)
            .first(-1)
            .doOnSuccess { Log.d(TAG, "learnFirst() :: onSuccess $it") }
            .subscribe()
    }

    fun learnLast() {
        Observable
            .range(0, 10)
            .last(-1)
            .doOnSuccess { Log.d(TAG, "learnLast() :: onSuccess $it") }
            .subscribe()
    }

    @Deprecated(message = "takeFirst is replaced with Observable.filter().first()")
    fun learnTakeFirst() {
        Observable
            .range(0, 10)
            .filter { it == 2 }
            .first(-1)
            .doOnSuccess { Log.d(TAG, "learnTakeFirst() :: filter().first() onSuccess $it") }
            .subscribe()
    }

    fun learnTakeUntil() {
        Observable
            .range(0, 10)
            .takeUntil { it >= 5 }
            .doOnNext { Log.d(TAG, "learnTakeUntil() :: onNext $it") }
            .subscribe()
    }

    fun learnTakeWhile() {
        Observable
            .range(0, 10)
            .takeWhile { it >= 5 }
            .doOnNext { Log.d(TAG, "learnTakeWhile() :: onNext $it") }
            .subscribe()
    }

    fun learnElementAt() {
        Observable
            .range(0, 10)
            .elementAt(3)
            .doOnSuccess { Log.d(TAG, "learnElementAt() :: onSuccess $it") }
            .subscribe()
    }

    /**
     * It is interesting, since the the default case is actually only used when the Observable is
     * completed with onComplete called right away without emitting anything... this is actually a
     * rare case? I figured. But any way, here it is...
     */
    @Deprecated("Use other ways to achieve it, see method for example")
    fun learnOrDefault() {
        Observable.create<Int> { it.onComplete() }
            .last(-1)
            .doOnSuccess { Log.d(TAG, "learnOrDefault() :: onSuccess $it") }
            .subscribe({/* handled */ }, {/* ignored */ })

    }

    fun learnCount() {
        Observable
            .interval(100, TimeUnit.MILLISECONDS)
            .take(5)
            .count()
            .doOnSuccess { Log.d(TAG, "learnCount() :: onSuccess count is :$it") }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun learnAll() {
        Observable
            .range(0, 10)
            .all { it >= 0 }
            .doOnSuccess { Log.d(TAG, "learnAll() :: onSuccess is all larger than 0? $it") }
            .subscribe()
    }

    fun learnContains() {
        Observable
            .range(0, 10)
            .contains(3)
            .doOnSuccess { Log.d(TAG, "learnContains() :: onSuccess is 3 contained? $it") }
            .subscribe()
    }

    @Deprecated("exist seems to be deprecated, have to use other ways to achieve the same, see method")
    fun learnExist() {
        Observable
            .range(0, 10)
            .filter { it >= 3 }
            .map { true }
            .first(false)
            .doOnSuccess { Log.d(TAG, "learnExist() :: onSuccess is >=3 exist in range? $it") }
            .subscribe()
    }

    fun learnDefer() {
        val observable1 = Observable.just(Whatever, Whatever, Whatever)
        val observable2 = Observable.just("a", "b", "c")
        val observable3 = Observable
            .defer {
                observable1
            }.flatMap {
                observable2.doOnComplete { it.doThings() }
            }.flatMap {
                Observable.just("read the hint from IDE, 'it' says String!")
            }

        observable3.subscribe {
            Log.d(RxBackPressure.TAG, "learnDefer() :: read the code $it")
        }
    }

    private object Whatever {
        fun doThings() {}
    }
}