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

        /**
         * Explain code
         *
         * Notice that this RxSubject class has a init {}, and since init is called when the object (this)
         * is created, thus we actually started emitting value since 0.0 second, thus you see the log:
         * --- Log.d(TAG, "Initial Creation, this should only be executed once!!!")
         * And the values actually already started emitting, since it is eager. Now notice in the fake Main
         * after the initial eagerly emit, which has a interval between each emmit from he fakeAPI by 1 second.
         * Since we tapped into the demoSubject with new observers in 2.0 sec and 6.0 sec later. we see
         * --- Log.d(TAG, "subject onNext says -> index: $t") // <-- start with t == 2
         *
         * Which is the correct, and desired behavior in this demo
         *
         */

        // Now imagine we start to listen, only after the class has been instantiate for a while
        Handler().postDelayed({
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
        }, 2000)

        // Now imagine someOne else wants to listen at a later time, notice, fakeAPI() is not called again
        // we are tapping into the existing stream, actually this is the same for first delayed call also
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
        }, 6000)
    }

}