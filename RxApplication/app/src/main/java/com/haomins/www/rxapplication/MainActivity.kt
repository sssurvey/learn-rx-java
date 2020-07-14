package com.haomins.www.rxapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var rxOne: RxOne
    private lateinit var rxTwo: RxTwo
    private lateinit var rxThree: RxThree
    private lateinit var rxFour: RxFour
    private lateinit var rxFive: RxFive
    private lateinit var rxSubjects: RxSubjects
    private lateinit var rxConnectableObservable: RxConnectableObservable
    private lateinit var rxComposingObservables: RxComposingObservables
    private lateinit var rxFlowControl: RxFlowControl
    private lateinit var rxBackPressure: RxBackPressure

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnclickListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        rxOne.clear()
    }

    private fun setOnclickListeners() {
        rx_one_button.setOnClickListener { initRxOne() }
        rx_two_button.setOnClickListener { initRxTwo() }
        rx_three_button.setOnClickListener { initRxThree() }
        rx_four_button.setOnClickListener { initRxFour() }
        rx_five_button.setOnClickListener { initRxFive() }
        rx_subject_button.setOnClickListener { initRxSubject() }
        rx_connectable_observable_button.setOnClickListener { initConnectableObservable() }
        rx_composing_observables_button.setOnClickListener { initComposingObservables() }
        rx_flow_control_button.setOnClickListener { initRxFlowControl() }
        rx_back_pressure.setOnClickListener { initRxBackPressure() }
    }

    private fun initRxOne() {
        rxOne = RxOne()
        rxOne.delayOperator()
        rxOne.flatMapNoOrder()
        rxOne.flatMapIterable()
    }

    private fun initRxTwo() {
        rxTwo = RxTwo()
        rxTwo.learnZipOperator()
        rxTwo.learnZipOperator2()
        rxTwo.learnZipOperator3()
        rxTwo.learnCombineLatest()
        rxTwo.learnWithLatestFrom()
        rxTwo.learnAmp()
    }

    private fun initRxThree() {
        rxThree = RxThree()
        rxThree.learnScan()
        rxThree.learnReduced()
        rxThree.learnCollect()
        rxThree.learnDistinct()
        rxThree.learnDistinctUntilChanged()
    }

    private fun initRxFour() {
        rxFour = RxFour()
        rxFour.learnTake()
        rxFour.learnSkip()
        rxFour.learnTakeLast()
        rxFour.learnSkipLast()
        rxFour.learnFirst()
        rxFour.learnLast()
        rxFour.learnTakeFirst()
        rxFour.learnTakeUntil()
        rxFour.learnTakeWhile()
        rxFour.learnElementAt()
        rxFour.learnOrDefault()
        rxFour.learnCount()
        rxFour.learnAll()
        rxFour.learnContains()
        rxFour.learnExist()
    }

    private fun initRxFive() {
        rxFive = RxFive()
        rxFive.learnCompose()
        rxFive.learnLift()
    }

    private fun initRxSubject() {
        rxSubjects = RxSubjects()
        rxSubjects.fakeMain()
    }

    private fun initConnectableObservable() {
        rxConnectableObservable = RxConnectableObservable()
        rxConnectableObservable.fakeMain()
    }

    private fun initComposingObservables() {
        rxComposingObservables = RxComposingObservables()
        rxComposingObservables.fakeMain()
    }

    private fun initRxFlowControl() {
        rxFlowControl = RxFlowControl()
        rxFlowControl.buffer1()
        rxFlowControl.buffer2()
        rxFlowControl.buffer3()
        rxFlowControl.window1()
        rxFlowControl.debounce1()
        rxFlowControl.debounce2()
    }

    private fun initRxBackPressure() {
        rxBackPressure = RxBackPressure()
        rxBackPressure.backPressure1()
    }
}
