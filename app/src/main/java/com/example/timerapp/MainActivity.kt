package com.example.timerapp

import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
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

    var mediaPlayer: MediaPlayer? = null
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnSelect.setOnClickListener {
            val dialog = TimeSet()
            dialog.show(supportFragmentManager, "Setting Timer")
            isRunning = false
            if (!isFirst) mCountDown.cancel()
            isFirst = false
        }

        btnStart.setOnClickListener {
            if (!isRunning) startTimer(startTime)
        }

        btnReset.setOnClickListener {
            if (isRunning) resetTimer()
        }

        btnPause.setOnClickListener {
            if (isRunning) pauseTimer()
        }

        btnAlarmStop.setOnClickListener{
            mediaPlayer!!.stop()
            btnAlarmStop.visibility = View.GONE
            tvTime.setText("00:00:00")
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
                tvTime.setText("Finish")
                btnAlarmStop.visibility = View.VISIBLE
                playAlarm()
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

    //左右画面切り替え時の時間保持
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val value = tvTime.text.toString()
        outState.putString("CURRENT_TIME", value)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val value = savedInstanceState.getString("CURRENT_TIME")
        tvTime.text = value
        val (h, m, s) = value!!.split(":").map { it.toInt() }
        val mill = (((h * 60 * 60) + (m * 60) + s) * 1000).toLong()
        startTimer(mill)
    }

    private fun playAlarm(){
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_ALARM)
            mediaPlayer!!.setDataSource(this, uri!!)
            mediaPlayer!!.isLooping = true
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}