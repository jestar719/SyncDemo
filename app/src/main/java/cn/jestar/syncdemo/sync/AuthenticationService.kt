package cn.jestar.syncdemo.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class AuthenticationService:Service(){
    private val TAG = "AuthenticationService"

    private lateinit var mAuthenticator: Authenticator

    override fun onCreate() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "SampleSyncAdapter Authentication Service started.")
        }
        mAuthenticator = Authenticator(this)
    }

    override fun onDestroy() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "SampleSyncAdapter Authentication Service stopped.")
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "getBinder()...  returning the AccountAuthenticator binder for intent "
                    + intent)
        }
        return mAuthenticator.iBinder
    }
}