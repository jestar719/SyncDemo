package cn.jestar.syncdemo.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder



class MySyncService: Service(){
    private lateinit var mAdapter:SyncAdapter
    override fun onCreate() {
        super.onCreate()
        mAdapter=SyncAdapter(applicationContext)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return mAdapter.syncAdapterBinder
    }
}

