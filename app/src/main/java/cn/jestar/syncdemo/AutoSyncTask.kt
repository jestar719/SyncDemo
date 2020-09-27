package cn.jestar.syncdemo

class AutoSyncTask : Runnable {
    override fun run() {
        ContactSyncManager.executeAutoSync()
    }
}