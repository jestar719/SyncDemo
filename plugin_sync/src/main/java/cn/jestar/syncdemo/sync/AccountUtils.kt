package cn.jestar.syncdemo.sync

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract

class AccountUtils(private val context: Context) {
    companion object {
        const val ACCOUNT_TYPE = "cn.jestar.syncdemo"
        const val ACCOUNT_LABEL = "Sync Demo"
        const val ACCOUNT_MIME_TYPE = "vnd.android.cursor.item/cn.jestar.syncdemo"
        const val PROFILE_MSG_SUFFIX="Talk to "
    }
        /**
         * 新增用户
         */
        fun addAccount(name: String) {
            try {
                val manager = AccountManager.get(context)
                val currentAccount = getAccount()
                if (currentAccount != null) {
                    return
                }
                val account = Account(name, ACCOUNT_TYPE)
                val isAdd = manager.addAccountExplicitly(account, null, Bundle.EMPTY)
                if (isAdd) {
                    ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true)
                    ContentResolver.setIsSyncable(account, ContactsContract.AUTHORITY, 1)
                }
//            LogUtils.d("Add account =${account.name} Success=${isAdd.toString()}")
            } catch (e: Exception) {
                e.stackTrace
//            LogUtils.e(e)
            }
        }

        /**
         * 删除用户
         */
        fun removeAccount(account:Account?) {
            if (account != null) {
//            LogUtils.d(account.name)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    AccountManager.get(context).removeAccountExplicitly(account)
                }
            }
        }

        fun getAccount(): Account? {
            val manager = AccountManager.get(context)
            val accountsByType = manager.getAccountsByType(ACCOUNT_TYPE)
            return if (accountsByType.isNullOrEmpty()) null else accountsByType[0]
        }

    /**
     * 请求系统同步
     */
    fun requestSync() {
        val account = getAccount()
        if (account != null) {
            val settingsBundle = Bundle().apply {
                putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
                putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
            }
            ContentResolver.requestSync(account, ContactsContract.AUTHORITY, settingsBundle)
        }
    }
}