package com.example.timerapp

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    var hour = 0
    var min = 0
    var sec = 0

    var countNum: Long = 0

    // millseconds
    var interval: Long = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSelect.setOnClickListener {
            val dialog = TimeSet()
            dialog.show(supportFragmentManager, "TimeSet")
        }

        btnStart.setOnClickListener {
            val countDown = object : CountDownTimer(countNum, interval) {
                override fun onTick(millisUntilFinished: Long) {
                    // 単位変換
                    val hms = String.format(
                        "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(
                                millisUntilFinished
                            )
                        ),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(
                                millisUntilFinished
                            )
                        )
                    )
                    tvTime.setText(hms)
                }

                override fun onFinish() {
                    TODO("Not yet implemented")
                }
            }
            countDown.start()
        }
    }

    fun displayTime(hour: Int, min: Int, sec: Int) {
        tvTime.text = "${hour}:${min}:${sec}"
        this.hour = hour
        this.min = min
        this.sec = sec

        // millseconds で開始時間設定
        countNum = ((hour * 60 * 60 * 1000) + (min * 60 * 1000) + (sec * 1000)).toLong()
    }
}