package com.haomins.www.rxapplication

import android.os.Handler
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observables.ConnectableObservable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RxConnectableObservable {

    companion object {
        const val TAG = "RxConnectableObservable"
    }

    private lateinit var connectableObservable: ConnectableObservable<Long>

    init {
        Log.d(TAG, "init {} called")
    }

    private val fakeApi = Observable.interval(1, TimeUnit.SECONDS).doOnNext {
            Log.d(TAG, "just for logging, now fakeAPI is emitting:: $it")
    }.observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())

    /**
     * Forcing subscription without any subscribers, this is called at init{} for this demo
     */
    private fun understandPublish() {
        // after called a publish to force connection, it returns a connectable observable that
        // we can use or expose that connectableObservable as an API
        // notice after calling the publish, nothing will happen just yet, it will only start subscribe
        // after we called the connect() function to it
        connectableObservable = fakeApi.publish()   // nothing will happen now, it is just for us to
                                                    // to get the connectableObservable
    }

    /**
     * Overview:
     * Connectable observable is obtained via calling .publish() on observables. In which .publish()
     * returns connectable observable. we can then subscribe to this connectable observable (a.k.a middle man)
     * and .subscribe() called on this connectable will simply add these new observers to a observers list
     * and will not trigger the connectable observable to start, unless the function .connect() is called
     * to this connectable observable. And once .connect() is called, all the observers subbed to this
     * connectable observable will start to get onNext() ... callbacks. and observers called after the
     * connect() will simply be continue added to the list, and tapped into the stream without causing
     * a new observable to be created.
     *
     * Connectable observable is useful to batch controll the emssion of data stream. And it is also useful
     * like publish().refCont() a.k.a share() to prevent duplicate stream caused by mutiple observers
     */
    fun fakeMain() {
        Log.d(TAG, "fakeMain() called")
        understandPublish() // obtain connectable observable

        // notice since .connect() is not called for connectable observable, so this will do nothing
        // for now. And once connect() is called by the 4000 ms handler, this will also fire up
        // that is the nature of connectable observable
        Log.d(TAG, "subscribe to connectableObservable after 0.0 sec delay")
        connectableObservable.subscribe {
            Log.d(TAG, "-> 0000 subscribe :: onNext called :: emitting -> $it")
        }

        Handler().postDelayed({
            Log.d(TAG, "connected to connectableObservable after 4.0 sec delay")
            // notice here the connection is started, thus the fakeAPI will start emitting values,
            // but knowing that it is cold until the following line is called, so we know it@fakeAPI
            // will start emitting from 0 after the next line (we will not able to notice this)
            connectableObservable.connect()
            Log.d(TAG, "-> 4000 connect() :: connected")
        }, 4000)

        // notice new sub added will not create new observables from fakeAPI()
        Handler().postDelayed({
            Log.d(TAG, "subscribe to connectableObservable after 8.0 sec delay")
            connectableObservable.subscribe {
                Log.d(TAG, "-> 8000 subscribe :: onNext called :: emitting -> $it")
            }
        }, 8000)
    }

}