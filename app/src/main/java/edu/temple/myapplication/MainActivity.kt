package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var binder: TimerService.TimerBinder
    val handler = Handler(Looper.getMainLooper()) {
        findViewById<TextView>(R.id.textView).text = it.what.toString()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                binder = service as TimerService.TimerBinder
                binder.setHandler(handler)
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                TODO("Not yet implemented")
            }
        }

        bindService(Intent(this, TimerService::class.java), serviceConnection, BIND_AUTO_CREATE)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            with(findViewById<Button>(R.id.startButton)) {
                if (text == "Start") {
                    binder.start(100)
                    Log.d("CountDown", "Started")
                    text = "Pause"
                } else {
                    text = if(text == "Resume") "Pause" else "Resume"
                    binder.pause()
                }
            }
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            binder.stop()
            findViewById<Button>(R.id.startButton).text = "Start"
            findViewById<TextView>(R.id.textView).text = "100"
        }
    }
}