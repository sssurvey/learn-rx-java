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
    }

    private fun initRxOne() {
        rxOne = RxOne()
        rxOne.delayOperator()
        rxOne.flatMapNoOrder()
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
}
