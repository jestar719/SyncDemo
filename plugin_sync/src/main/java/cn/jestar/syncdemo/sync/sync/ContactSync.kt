package cn.jestar.syncdemo.sync.sync

import android.accounts.Account
import android.app.Service
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log

/**
 * ClassName:      ContactSyncAdapter
 * Description:    Adapter for execute sync that call by system
 * Author:         Joetar
 * CreateDate:     2020-9-16 11:10
 * UpdateUser:     Joetar
 * UpdateDate:     2020-9-16 11:10
 * UpdateRemark:   Modify the description
 */
class ContactSyncAdapter(private val mContext: Context) : AbstractThreadedSyncAdapter(mContext, true) {
    companion object {
        private var runnable: Runnable? = null
        fun setTask(task: Runnable) {
            runnable = task
        }
    }

    override fun onPerformSync(account: Account,
                               bundle: Bundle,
                               s: String,
                               contentProviderClient: ContentProviderClient,
                               syncResult: SyncResult) {
        Log.d("onPerformSync----", "onPerformSync start")
        runnable?.run()
    }
}


/**
 * ClassName:      ContactSyncAdapter
 * Description:    Service for execute sync that call by system
 * Author:         Joetar
 * CreateDate:     2020-9-16 11:10
 * UpdateUser:     Joetar
 * UpdateDate:     2020-9-16 11:10
 * UpdateRemark:   Modify the description
 */
class ContactSyncService : Service() {
    private var syncAdapter: ContactSyncAdapter? = null
    override fun onCreate() {
        super.onCreate()
        synchronized(this) {
            if (syncAdapter == null) {
                syncAdapter = ContactSyncAdapter(this)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return syncAdapter!!.syncAdapterBinder
    }
}