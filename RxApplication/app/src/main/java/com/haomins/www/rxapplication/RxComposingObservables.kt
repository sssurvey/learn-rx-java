package com.haomins.www.rxapplication

import android.os.Handler
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class RxComposingObservables {

    companion object {

        const val TAG = "RxComposingObservables"

        /**
         * A fake db query method, simulate a dump db request that is blocking and returns based on
         * the amount of page you want it to load, limited to 10 row per page
         */
        fun fakeDbQuery(page: Int): List<String> {
            Log.d(TAG, "fakeDbQuery thread name is :: ${Thread.currentThread()}")
            Thread.sleep(1000)
            val queryResult = mutableListOf<String>()
            for (index in 0 until 5) {
                queryResult.add("result: ${index}000345, page #: $page")
            }
            return queryResult
        }
    }

    private fun loadExistingApiLazilyWithPaging(initialPage: Int): Observable<String> {
        return Observable.defer {
            Log.d(TAG, "loadExistingApiLazilyWithPaging() called with page: $initialPage")
            Observable.fromIterable(fakeDbQuery(initialPage))
        }.concatWith(
            Observable.defer {
                loadExistingApiLazilyWithPaging(initialPage + 1)
            }
        ).observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    fun fakeMain() {
        val sub = loadExistingApiLazilyWithPaging(initialPage = 1).doOnNext {
            Log.d(TAG, "result loaded = row -> $it")
        }.subscribe()

        val runnable = Runnable {
            Log.d(TAG, "--- disposed subscription, we are not querying anymore")
            sub.dispose()
        }
        //TODO: why is this never called?
        Handler().postDelayed(runnable, 4000)
    }

}