package cn.jestar.syncdemo

import android.accounts.Account
import android.content.ContentResolver
import android.os.AsyncTask
import android.provider.ContactsContract
import cn.jestar.syncdemo.sync.AccountUtils
import cn.jestar.syncdemo.sync.ContactBatch
import cn.jestar.syncdemo.sync.ContactProfile
import cn.jestar.syncdemo.sync.sync.ContactSyncAdapter

object ContactSyncManager {
    private val context = App.CONTEXT
    private val accountUtils = AccountUtils(context)


    fun getAccount(): Account? {
        return accountUtils.getAccount()
    }

    fun addAccount(name: String) {
        accountUtils.addAccount(name)
    }

    fun sync() {
        ContactSyncAdapter.setTask(AutoSyncTask())
        accountUtils.requestSync()
    }

    fun addContact(name: String, phone: String) {
        val simpleContact = SimpleContact(name, phone)
        AsyncTask.execute(SyncTask(simpleContact))
    }


    fun deleteContacts() {
        val batch = ContactBatch(context.contentResolver)
        val account = accountUtils.getAccount() ?: return
        batch.deleteContactByAccount(account).execute()
    }

    fun deleteAccount() {
        val account = accountUtils.getAccount()
        accountUtils.removeAccount(account)
    }

    fun executeSync(profile: ContactProfile) {
        val account = accountUtils.getAccount() ?: return
        val batch = ContactBatch(context.contentResolver)
        batch.deleteContactByAccount(account)
        batch.addNewContact(account.name, profile)
        batch.addSettings(account)
        batch.execute()
    }

    fun executeAutoSync() {
        val account = accountUtils.getAccount() ?: return
        val provider = context.contentResolver
        val contacts = getContacts(provider)
        if (contacts.isEmpty())
            return
        val batch = ContactBatch(provider)
        batch.deleteContactByAccount(account)
        for (contact in contacts) {
            batch.addContact(account.name, contact)
            if (batch.size() > 50)
                batch.execute()
        }
        batch.addSettings(account).execute()
    }

    private fun getContacts(provider: ContentResolver): ArrayList<ContactProfile> {
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER)
        val list = ArrayList<ContactProfile>()
        val cursor = provider.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null)
        if (cursor == null || cursor.count == 0) {
            return list
        }
        var phone: String?
        var name: String?
        while (cursor.moveToNext()) {
            name = cursor.getString(0)
            phone = cursor.getString(1)
            if (name.isNullOrEmpty() || phone.isNullOrEmpty() || !phone.startsWith("130")) {
                continue
            } else {
                list.add(SimpleContact(name, phone))
            }
        }
        cursor.close()
        return list
    }
}