package com.haomins.www.rxapplication

import android.os.Handler
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.concurrent.TimeUnit

/**
 * Refer to book page 51 -> rx.subjects.Subject
 */
class RxSubjects {

    companion object {
        const val TAG = "RxSubjects"

        /**
         * Imagine this is some API that is not rxJava, we just want to emit values
         */
        fun fakeAPI(callbackNext: (Int) -> Unit, callBackDone: () -> Unit) {
            Log.d(TAG, "Initial Creation, this should only be executed once!!!")
            Observable.interval(1, TimeUnit.SECONDS).subscribe {
                if (it == 20L) callBackDone()
                else callbackNext(it.toInt())
            }
        }
    }

    private val demoSubject: Subject<Int> = PublishSubject.create()

    init {
        fakeAPI({ demoSubject.onNext(it) }, { demoSubject.onComplete() })
    }

    /**
     * Imagine this as a public API
     */
    private fun observe(): Observable<Int> {
        return demoSubject
    }

    /**
     * Imagine calling this in Activity
     */
    fun fakeMain() {
        observe().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(
            object : DisposableObserver<Int>() {
                override fun onComplete() {
                    Log.d(TAG, "subject onComplete says -> DONE")
                }

                override fun onNext(t: Int?) {
                    Log.d(TAG, "subject onNext says -> index: $t")
                }

                override fun onError(e: Throwable?) {
                    Log.d(TAG, "subject onError says -> ERROR")
                }
            }
        )

        // Now imagine someOne else wants to listen
        Handler().postDelayed({
            Log.d(TAG, "Another observer is tapping in to listen, it should not start with index 0")
            observe().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    object : DisposableObserver<Int>() {
                        override fun onComplete() {
                            Log.d(TAG, "subject 2 onComplete says -> DONE")
                        }

                        override fun onNext(t: Int?) {
                            Log.d(TAG, "subject 2 onNext says -> index: $t")
                        }

                        override fun onError(e: Throwable?) {
                            Log.d(TAG, "subject 2 onError says -> ERROR")
                        }
                    }
                )
        }, 5000)
    }

}