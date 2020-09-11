package cn.jestar.syncdemo.sync


import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.content.Context
import android.os.Bundle
import android.util.Log

class Authenticator(context: Context) : AbstractAccountAuthenticator(context) {
    override fun getAuthTokenLabel(p0: String?): String {
        return p0.toString()
    }

    override fun confirmCredentials(
            p0: AccountAuthenticatorResponse?,
            p1: Account?,
            p2: Bundle?
    ): Bundle {
        return Bundle.EMPTY
    }

    override fun updateCredentials(
            p0: AccountAuthenticatorResponse?,
            p1: Account?,
            p2: String?,
            p3: Bundle?
    ): Bundle {
        return Bundle.EMPTY
    }

    override fun getAuthToken(
            p0: AccountAuthenticatorResponse?,
            p1: Account?,
            p2: String?,
            p3: Bundle?
    ): Bundle {
        Log.i("getAuthToken", "getAuthToken")
        return Bundle.EMPTY
    }

    override fun hasFeatures(
            p0: AccountAuthenticatorResponse?,
            p1: Account?,
            p2: Array<out String>?
        ): Bundle {
        return Bundle.EMPTY
    }

    override fun editProperties(p0: AccountAuthenticatorResponse?, p1: String?): Bundle {
        return Bundle.EMPTY
    }

    override fun addAccount(
            p0: AccountAuthenticatorResponse?,
            accountType: String?,
            authTokenType: String?,
            requiredFeatures: Array<out String>?,
            options: Bundle?
    ): Bundle {
        Log.i("addAccount", "addAccount")
        return Bundle.EMPTY
    }
}