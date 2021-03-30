package com.ikuuuyTimer.timerapp

import android.app.Application
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val currentTime: MutableLiveData<String> = MutableLiveData()
    val finishSentence: MutableLiveData<String> = MutableLiveData()

    lateinit var mCountDown: CountDownTimer

    var isRunning: Boolean = false
    var startTime: Long = 0

    var current: Long = 0
    var fisrtTime: Long = 0

    var mediaPlayer: MediaPlayer? = null
    var uri: Uri? = null

    fun startTimer(time: Long) {
        mCountDown = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                current = millisUntilFinished
                convertDisplayTime(millisUntilFinished)
            }

            override fun onFinish() {
                playAlarm()
                finishSentence.value = "Finish"
            }
        }
        mCountDown.start()
        isRunning = true
    }

    fun pauseTimer() {
        mCountDown.cancel()
        startTime = current
        isRunning = false
    }

    fun resetTimer() {
        convertDisplayTime(fisrtTime)
        mCountDown.cancel()
        isRunning = false
        startTime = fisrtTime
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
        currentTime.value = String.format("%02d:%02d:%02d", hh, mm, ss)
    }

    fun playAlarm() {
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_ALARM)
            mediaPlayer!!.setDataSource(getApplication(), uri!!)
            true.also { mediaPlayer!!.isLooping = it }
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
