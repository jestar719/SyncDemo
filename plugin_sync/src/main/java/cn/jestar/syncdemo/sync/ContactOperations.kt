package cn.jestar.syncdemo.sync

import android.accounts.Account
import android.content.ContentProviderOperation
import android.content.ContentValues
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.provider.ContactsContract.RawContacts
import my.com.digital.sync.R


class ContactOperations(private val batch: ContactBatch) {
    private val values = ContentValues()

    private fun insertBatch(operation: ContentProviderOperation) {
        batch.add(operation)
        values.clear()
    }

    private fun addCallerIsSyncAdapterParameter(uri: Uri, isSyncOperation: Boolean): Uri {
        return if (isSyncOperation) {
            uri.buildUpon()
                    .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                    .build()
        } else uri
    }

    private fun setAccount(accountName: String) {
        values.put(RawContacts.ACCOUNT_NAME, accountName)
        values.put(RawContacts.ACCOUNT_TYPE, AccountUtils.ACCOUNT_TYPE)
    }

    private fun newInsert(uri: Uri, sync: Boolean = true): ContentProviderOperation.Builder {
        return ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(uri, sync)).withValues(values)
    }

    private fun newDelete(uri: Uri, sync: Boolean = true): ContentProviderOperation.Builder {
        return ContentProviderOperation.newDelete(addCallerIsSyncAdapterParameter(uri, sync))
    }

    fun addSetting(accountName: String): ContactOperations {
        setAccount(accountName)
        values.put(ContactsContract.Settings.UNGROUPED_VISIBLE, 1)
        val operation = newInsert(ContactsContract.Settings.CONTENT_URI).build()
        insertBatch(operation)
        return this
    }

    fun addAccount(accountName: String): ContactOperations {
        setAccount(accountName)
        val operation = newInsert(RawContacts.CONTENT_URI).withYieldAllowed(true).build()
        insertBatch(operation)
        return this
    }

    fun addName(name: String, index: Int): ContactOperations {
        values.let {
            it.put(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
            it.put(StructuredName.DISPLAY_NAME, name)
            it.put(StructuredName.GIVEN_NAME, name)
        }
        val operation = newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, index)
                .build()
        insertBatch(operation)
        return this
    }

    fun addPhone(phone: String, index: Int): ContactOperations {
        values.let {
            it.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            it.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
        }
        val operation = newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, index)
                .build()
        insertBatch(operation)
        return this
    }

    fun addProfile(profile: ContactProfile, index: Int): ContactOperations {
        val contactPhone = profile.getContactPhone()
        values.let {
            it.put(ContactsContract.Data.MIMETYPE, AccountUtils.ACCOUNT_MIME_TYPE)
            it.put(ContactProfile.COLUMN_PHONE, contactPhone)
            it.put(ContactProfile.COLUMN_LABEL, AccountUtils.ACCOUNT_LABEL)
            it.put(ContactProfile.COLUMN_MSG, AccountUtils.PROFILE_MSG_SUFFIX+contactPhone)
            it.put(ContactProfile.COLUMN_ACCOUNT_NUMBER, profile.getAppId())
        }
        val operation = newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, index)
                .build()
        insertBatch(operation)
        return this
    }

    fun addUpdateStatus(id: String, accountName: String, packageName: String): ContactOperations {
        values.apply {
            put(ContactsContract.StatusUpdates.DATA_ID, id)
            put(ContactsContract.StatusUpdates.STATUS, AccountUtils.ACCOUNT_LABEL)
            put(ContactsContract.StatusUpdates.STATUS_RES_PACKAGE, packageName)
            put(ContactsContract.StatusUpdates.STATUS_ICON, R.drawable.ic_contact_profile)
            put(ContactsContract.StatusUpdates.STATUS_LABEL, R.string.account_label)
        }
        val operation = newInsert(ContactsContract.StatusUpdates.CONTENT_URI, false)
                .withYieldAllowed(true)
                .build()
        insertBatch(operation)
        return this
    }

    fun delete(account: Account) {
        deleteData()
        deleteRawContact(account)
    }

    private fun deleteRawContact(account: Account) {
        val operation = newDelete(RawContacts.CONTENT_URI)
                .withSelection("${RawContacts.ACCOUNT_TYPE} =?", arrayOf(account.type))
                .build()
        insertBatch(operation)
    }

    private fun deleteData() {
        val operation = newDelete(ContactsContract.Data.CONTENT_URI, false)
                .withSelection("${ContactsContract.Data.MIMETYPE} =?", arrayOf(AccountUtils.ACCOUNT_MIME_TYPE))
                .build()
        insertBatch(operation)
    }

}