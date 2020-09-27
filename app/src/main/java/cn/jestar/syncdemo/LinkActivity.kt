package cn.jestar.syncdemo

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.jestar.syncdemo.sync.ContactProfile
import kotlinx.android.synthetic.main.activity_link.*

class LinkActivity : AppCompatActivity() {
    private val NULL_TEXT = "No Contact Receive"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dataString = intent.dataString
        Log.i("LinkActivity", dataString ?: "Null intent data string")
        val data = intent.data
        if (data == null) {
            textView.text = NULL_TEXT
        } else {
            val projection = arrayOf(ContactProfile.COLUMN_NAME, ContactProfile.COLUMN_NAME)
            val query = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, null, null, null)
            if (query == null || query.count == 0) {
                textView.text = NULL_TEXT
            } else {
                query.moveToFirst()
                val name = query.getString(0)
                val phone = query.getString(1)
                val text = "Name=$name  Phone=$phone"
                textView.text = text
                query.close()
            }
        }
    }

}