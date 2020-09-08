package cn.jestar.syncdemo.sync

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import android.util.Log
import cn.jestar.syncdemo.sync.contact.SyncManager

class SyncAdapter(context: Context) : AbstractThreadedSyncAdapter(context, false, true) {
    override fun onPerformSync(p0: Account, p1: Bundle, p2: String, p3: ContentProviderClient, p4: SyncResult) {
        Log.i("onPerformSync", "onPerformSync() run now")
        SyncManager.sync(p0)
    }

}