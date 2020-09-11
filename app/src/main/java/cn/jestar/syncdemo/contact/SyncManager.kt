package cn.jestar.syncdemo.contact

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentProviderClient
import android.content.ContentResolver
import android.content.SyncRequest
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import cn.jestar.syncdemo.App

object SyncManager {
    private val context = App.CONTEXT
    private val utils = ContactUtils(context.contentResolver)

    fun sync(account: Account,isSyncOperation: Boolean=true): ArrayList<ContactBean> {
        val list = utils.queryContactWithPhone()
        if (list.isNotEmpty()) {
            val batchOperation = BatchOperation(context.contentResolver)
            val bean = list[0]
            ContactOperations.createNewContact(
                    account.name,
                    isSyncOperation,
                    batchOperation,
                    bean.name,
                    bean.phone
            )
            val execute = batchOperation.execute()
            Log.i("jestar", execute.toString())
            return utils.queryByCurrentAccount(account.name)
        }
        return ArrayList()
    }

    fun sync(account: Account, providerClient: ContentProviderClient) {
        val list = utils.queryContactWithPhone()
        if (list.isNotEmpty()) {
            val batchOperation = BatchOperation(context.contentResolver)
            val bean = list[0]
            ContactOperations.createNewContact(
                    account.name,
                    true,
                    batchOperation,
                    bean.name,
                    bean.phone
            )
            val applyBatch = providerClient.applyBatch(batchOperation.getList())
            Log.i("jestar", applyBatch.toString())
        }
    }



    fun addAccount(name: String): Account? {
        val account = Account(name, ACCOUNT_TYPE)
        val manager = AccountManager.get(context)
//        val bundle = Bundle()
//        bundle.putString(TOKEN_TYPE, name + token)
        if (manager.addAccountExplicitly(account, null, Bundle.EMPTY)) {
            manager.setAuthToken(account, ACCOUT_TAG, name + ACCOUT_TAG)
            //设置自动同步
            ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true)
            ContentResolver.setIsSyncable(account, ContactsContract.AUTHORITY, 1)
        } else {
            Log.i("tag", "Create Account Fail")
        }
        return account
    }

    fun getAccount(): Account? {
        val accounts = AccountManager.get(context).getAccountsByType(ACCOUNT_TYPE)
        return if (accounts.isNullOrEmpty()) {
            null
        } else {
            accounts[0]
        }
    }

    fun forceSync(account: Account) {
        val build = SyncRequest.Builder()
                .setSyncAdapter(account, ContactsContract.AUTHORITY)
                .setExtras(Bundle.EMPTY)
                .build()
        ContentResolver.requestSync(account, ContactsContract.AUTHORITY, Bundle.EMPTY)
//        ContentResolver.requestSync(build)
    }

    fun deleteData(name: String): ArrayList<ContactBean> {
        utils.deleteContactByAccount(name)
        return utils.queryByCurrentAccount(name)
    }

    fun getAccountData(name: String): ArrayList<ContactBean> {
        return utils.queryByCurrentAccount(name)
    }

}