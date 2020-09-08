package cn.jestar.syncdemo.sync.contact

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.content.ContentResolver
import android.content.SyncRequest
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import cn.jestar.syncdemo.App
import cn.jestar.syncdemo.MainActivity

object SyncManager {
    val context = App.CONTEXT
    val utils = ContactUtils(context.contentResolver)

    fun sync(account: Account): ArrayList<ContactBean> {
        val list = utils.queryContactWithPhone()
        if (list.isNotEmpty()) {
            val batchOperation = BatchOperation(context.contentResolver)
            val bean = list[0]
            ContactOperations.createNewContact(account.name, true, batchOperation, bean.name, bean.phone)
            batchOperation.execute()
            return utils.queryByCurrentAccount(account.name)
        }
        return ArrayList()
    }

    fun addAccount(name: String): Account? {
        val account=Account(name, ACCOUNT_TYPE)
        val manager = AccountManager.get(context)
//        val bundle = Bundle()
//        bundle.putString(TOKEN_TYPE, name + token)
        if (manager.addAccountExplicitly(account, ACCOUT_NAME, Bundle.EMPTY)) {
            manager.setAuthToken(account, ACCOUT_NAME, name + ACCOUT_NAME)
            //设置自动同步
            ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true)
        } else {
            Log.i("tag", "帐户插入失败")
        }
        return account
    }

    fun forceSync(account: Account,activity: Activity){
        ContentResolver.requestSync(account,ContactsContract.AUTHORITY, Bundle.EMPTY)
    }
}