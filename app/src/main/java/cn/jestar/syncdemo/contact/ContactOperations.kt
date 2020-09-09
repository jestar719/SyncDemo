package cn.jestar.syncdemo.contact

import android.content.ContentProviderOperation
import android.content.ContentProviderResult
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.ContactsContract
import android.provider.SyncStateContract.Columns.ACCOUNT_TYPE
import android.util.Log


class BatchOperation(private var mResolver: ContentResolver) {
    private val TAG: String = "BatchOperation"
    private val mOperations = ArrayList<ContentProviderOperation>()
    fun add(cpo: ContentProviderOperation) {
        mOperations.add(cpo)
    }

    fun execute(): List<Uri>? {
        val resultUris: MutableList<Uri> = ArrayList()
        if (mOperations.size == 0) {
            return resultUris
        }
        // Apply the mOperations to the content provider
        try {
            val results: Array<ContentProviderResult> = mResolver.applyBatch(
                    ContactsContract.AUTHORITY,
                    mOperations
            )
            if (results.isNotEmpty()) {
                for (i in results.indices) {
                    resultUris.add(results[i].uri)
                }
            }
        } catch (e1: Exception) {
            Log.e(TAG, "storing contact data failed", e1)
        }
        mOperations.clear()
        return resultUris
    }

    fun size(): Int {
        return mOperations.size
    }
}

class ContactOperations(
        private val isSyncOperation: Boolean,
        val mBatchOperation: BatchOperation
) {
    private val mValues: ContentValues = ContentValues()
    private var mIsYieldAllowed = true
    private var mBackReference: Int = 0
    private var mIsNewContact: Boolean = true

    companion object {
        fun createNewContact(
                accountName: String,
                isSyncOperation: Boolean,
                batchOperation: BatchOperation,
                name:String,
                phone:String
        ){
            val cop=ContactOperations(isSyncOperation, batchOperation).apply {
                mBackReference = mBatchOperation.size()
                mIsNewContact = true
            }
            val contentValues = ContentValues()
            contentValues.put(ContactsContract.RawContacts.ACCOUNT_TYPE, cn.jestar.syncdemo.contact.ACCOUNT_TYPE)
            contentValues.put(ContactsContract.RawContacts.ACCOUNT_NAME, accountName)
            val builder: ContentProviderOperation.Builder = cop.newInsertCpo(ContactsContract.RawContacts.CONTENT_URI, isSyncOperation, true)
                                .withValues(contentValues)
            batchOperation.add(builder.build())
            cop.addName(name).addProfileAction(name,phone)
            }
        }

        private fun newInsertCpo(
                uri: Uri,
                isSyncOperation: Boolean, isYieldAllowed: Boolean
        ): ContentProviderOperation.Builder {
            return ContentProviderOperation
                    .newInsert(addCallerIsSyncAdapterParameter(uri, isSyncOperation))
                    .withYieldAllowed(isYieldAllowed)
        }

        private fun addCallerIsSyncAdapterParameter(
                uri: Uri,
                isSyncOperation: Boolean
        ): Uri {
            return if (isSyncOperation) {
                // If we're in the middle of a real sync-adapter operation, then go ahead
                // and tell the Contacts provider that we're the sync adapter.  That
                // gives us some special permissions - like the ability to really
                // delete a contact, and the ability to clear the dirty flag.
                //
                // If we're not in the middle of a sync operation (for example, we just
                // locally created/edited a new contact), then we don't want to use
                // the special permissions, and the system will automagically mark
                // the contact as 'dirty' for us!
                uri.buildUpon()
                        .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                        .build()
            } else uri
        }



    /**
     * Adds a contact name. We can take either a full name ("Bob Smith") or separated
     * first-name and last-name ("Bob" and "Smith").
     *
     * @param fullName The full name of the contact - typically from an edit form
     * Can be null if firstName/lastName are specified.
     */
    private fun addName(
            fullName: String
    ): ContactOperations {
        mValues.apply {
            clear()
            put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, fullName)
            put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, fullName)
            put(ContactsContract.CommonDataKinds.StructuredName.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
        }
        addInsertOp()
        return this
    }


    private fun addGroupMembership(groupId: Long): ContactOperations {
        mValues.apply {
            clear()
            put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, groupId)
            put(
                    ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,
                    ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE
            )
        }
        addInsertOp()
        return this
    }

    /**
     * Adds a profile action
     * @return instance of ContactOperations
     */
    private fun addProfileAction(name: String, phone: String): ContactOperations {
        mValues.apply {
            clear()
            put(CustomColumns.DATA_SUMMARY, cn.jestar.syncdemo.contact.ACCOUNT_TYPE)
            put(CustomColumns.DATA_DETAIL, "Transfer To $name")
            put(ContactsContract.Data.MIMETYPE, ACCOUT_MIME_TYPE)
        }
        addInsertOp()
        return this
    }

    /**
     * Adds an insert operation into the batch
     */
    private fun addInsertOp() {
        val builder: ContentProviderOperation.Builder = newInsertCpo(
                ContactsContract.Data.CONTENT_URI,
                isSyncOperation,
                mIsYieldAllowed
        )
        builder.withValues(mValues)
        if (mIsNewContact) {
            builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, mBackReference)
        }
        mIsYieldAllowed = false
        mBatchOperation.add(builder.build())
    }
}