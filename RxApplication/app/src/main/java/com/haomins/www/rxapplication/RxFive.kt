package com.haomins.www.rxapplication

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOperator
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver

class RxFive {

    companion object {
        const val TAG = "RxFive"
    }

    fun learnCompose() {

    }

    /**
     * Demo of how to use the lift and work with Observable to create custom operators,
     * ObservableOperator<DownStream, UpStream>
     */
    fun learnLift() {
        Observable.just(1, 2, 3).lift(
            ObservableOperator<String, Int> {
                object : Observer<Int> {
                    override fun onComplete() { Log.d(TAG, "lift onComplete") }
                    override fun onSubscribe(d: Disposable?) { Log.d(TAG, "lift onSubscribe") }
                    override fun onNext(t: Int?) { it.onNext(t.toString() + " haha") }
                    override fun onError(e: Throwable?) { Log.d(TAG, "lift onError :: ${e?.printStackTrace()}") }
                }
            }
        ).subscribe(
            object : DisposableObserver<String>() {
                override fun onComplete() { Log.d(TAG, "final onComplete") }
                override fun onNext(t: String?) { Log.d(TAG, "final onNext :: $t") }
                override fun onError(e: Throwable?) { Log.d(TAG, "final onError :: ${e?.printStackTrace()}") }
            }
        )
    }


}