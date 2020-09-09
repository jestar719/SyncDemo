package cn.jestar.syncdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class LinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("LinkActivity", intent.dataString ?: "Null intent data string")
    }
}