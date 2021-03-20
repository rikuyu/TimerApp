package com.example.timerapp

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var mCountDown: CountDownTimer
    private var isRunning: Boolean = false
    private var startTime: Long = 0

    var hour: Int = 0
    var min: Int = 0
    var sec: Int = 0

    var currentTime: Long = 0
    var isFirst: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSelect.setOnClickListener {
            val dialog = TimeSet()
            dialog.show(supportFragmentManager, "TimeSet")
            isRunning = false
            if (!isFirst) mCountDown.cancel()
            isFirst = false
        }

        btnStart.setOnClickListener {
            if (!isRunning) startTimer(startTime)
        }

        btnReset.setOnClickListener {
            resetTimer()
        }

        btnPause.setOnClickListener {
            if (isRunning) pauseTimer()
        }
    }

    fun initDisplayTime(hour: Int, min: Int, sec: Int) {
        tvTime.setText(String.format("%02d:%02d:%02d", hour, min, sec))
        this.hour = hour
        this.min = min
        this.sec = sec
    }

    fun setStartTime() {
        startTime = ((hour * 60 * 60 * 1000) + (min * 60 * 1000) + (sec * 1000)).toLong()
    }

    fun startTimer(time: Long) {
        mCountDown = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                currentTime = millisUntilFinished
                convertDisplayTime(millisUntilFinished)
            }

            override fun onFinish() {
                tvTime.setText(("00:00:00"))
            }
        }
        mCountDown.start()

        isRunning = true
    }

    fun pauseTimer() {
        mCountDown.cancel()
        startTime = currentTime
        isRunning = false
    }

    fun resetTimer() {
        setStartTime()
        convertDisplayTime(startTime)
        isRunning = false
        mCountDown.cancel()
    }

    private fun convertDisplayTime(time: Long) {
        val hh = TimeUnit.MILLISECONDS.toHours(time)
        val mm =
            TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    time
                )
            )
        val ss =
            TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    time
                )
            )
        tvTime.setText(String.format("%02d:%02d:%02d", hh, mm, ss))
    }
}