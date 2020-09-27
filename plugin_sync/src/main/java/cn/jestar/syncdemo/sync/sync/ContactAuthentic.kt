package cn.jestar.syncdemo.sync.sync

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder

/**
 * ClassName:      ContactAuthenticator
 * Description:    class for create account that call by system. it just work for AccountManager call method with future
 * Author:         Joestar
 * CreateDate:     2020-10-16 10:43
 * UpdateUser:     Joestar
 * UpdateDate:     2020-10-16 10:43
 * UpdateRemark:   Modify the description
 */
class ContactAuthenticator(context: Context) : AbstractAccountAuthenticator(context) {

    override fun addAccount(response: AccountAuthenticatorResponse?, accountType: String, authTokenType: String?, requiredFeatures: Array<out String>?, options: Bundle?): Bundle {
        return Bundle.EMPTY
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account, authTokenType: String?, options: Bundle?): Bundle {
        return Bundle.EMPTY
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse, account: Account, options: Bundle?): Bundle {
        return Bundle.EMPTY
    }

    override fun editProperties(response: AccountAuthenticatorResponse, accountType: String): Bundle {
        return Bundle.EMPTY
    }

    override fun getAuthTokenLabel(authTokenType: String): String {
        return authTokenType
    }

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account, authTokenType: String, options: Bundle?): Bundle {
        return Bundle.EMPTY
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse, account: Account, features: Array<out String>?): Bundle {
        return Bundle.EMPTY
    }

}

/**
 * ClassName:      ContactAuthenticator
 * Description:    Service for create Account that called by system
 * Author:         Joestar
 * CreateDate:     2020-10-16 10:43
 * UpdateUser:     Joestar
 * UpdateDate:     2020-10-16 10:43
 * UpdateRemark:   Modify the description
 */
class ContactAuthenticatorService : Service() {
    private lateinit var authenticator: ContactAuthenticator
    override fun onCreate() {
        // Create a new authenticator object
        authenticator = ContactAuthenticator(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        return authenticator.iBinder
    }
}