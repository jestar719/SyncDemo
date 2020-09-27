package cn.jestar.syncdemo.sync

import android.accounts.Account
import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log

class ContactBatch(private val provider: ContentResolver) {
    private val list: ArrayList<ContentProviderOperation> = ArrayList()

    fun deleteContactByAccount(account: Account): ContactBatch {
        ContactOperations(this).delete(account)
        return this
    }

    fun addSettings(account: Account): ContactBatch {
        ContactOperations(this).addSetting(account.name)
        return this
    }

    fun addContact(accountName: String,profile: ContactProfile): ContactBatch {
        val size = list.size
        ContactOperations(this)
                .addAccount(accountName)
                .addName(profile.getContactName(), size)
                .addProfile(profile, size)
        return this
    }

    fun addNewContact(accountName: String,profile: ContactProfile): ContactBatch {
        val size = list.size
        ContactOperations(this)
                .addAccount(accountName)
                .addName(profile.getContactName(), size)
                .addPhone(profile.getContactPhone(),size)
                .addProfile(profile, size)
        return this
    }

    fun add(operation: ContentProviderOperation): ContactBatch {
        list.add(operation)
        return this
    }

    fun  size(): Int {
        return list.size
    }

    fun execute(): Boolean {
        var success = true
        try {
            val applyBatch = provider.applyBatch(ContactsContract.AUTHORITY, list)
            Log.d("execute -----> ", applyBatch.contentToString())
        } catch (e: Exception) {
            Log.e("execute -----> ", e.toString())
            success = false
        }
        list.clear()
        return success
    }
}