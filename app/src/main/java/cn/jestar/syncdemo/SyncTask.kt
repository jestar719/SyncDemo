package cn.jestar.syncdemo

import cn.jestar.syncdemo.sync.ContactProfile

class SyncTask(private val profile: ContactProfile):Runnable {
    override fun run() {
        ContactSyncManager.executeSync(profile)
    }
}