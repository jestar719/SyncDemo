package cn.jestar.syncdemo.contact

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log

class ContactBean {
    var id = ""
    var contactId = ""
    var rawContactId = ""
    var name = ""
    var phone = ""
    var accountType = ""
    var accountName = ""
    var mimeType = ""
    override fun toString(): String {
        return "ContactBean(id='$id', contactId='$contactId', rawContactId='$rawContactId', name='$name', phone='$phone', accountType='$accountType', accountName='$accountName', mimeType='$mimeType')"
    }
}


class ContactUtils(private val provider: ContentResolver) {
    val accountTypeAndNameSelection =
        "${ContactsContract.RawContacts.ACCOUNT_TYPE}=? AND ${ContactsContract.RawContacts.ACCOUNT_NAME}=?"


    private val contactProjection = arrayOf(
        ContactsContract.Data._ID,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.RawContacts.ACCOUNT_TYPE,
        ContactsContract.RawContacts.ACCOUNT_NAME,
        ContactsContract.Contacts.Data.MIMETYPE
    )

    private val rawContactProjection = arrayOf(
        ContactsContract.Data._ID,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.RawContacts.ACCOUNT_TYPE,
        ContactsContract.RawContacts.ACCOUNT_NAME
    )

    /**
     * 查询所有电话
     */
    fun queryContactWithPhone(): ArrayList<ContactBean> {
        val query = query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, contactProjection)
        return getList(query)
    }

    fun queryByCurrentAccount(name: String): ArrayList<ContactBean> {
        val list = getRawContactList(
            query(
                ContactsContract.RawContacts.CONTENT_URI,
                rawContactProjection,
                accountTypeAndNameSelection,
                arrayOf(ACCOUNT_TYPE, name)
            )
        )
        val list1 = getList(
            query(
                ContactsContract.Data.CONTENT_URI,
                contactProjection,
                accountTypeAndNameSelection,
                arrayOf(ACCOUNT_TYPE, name)
            )
        )
        list.addAll(list1)
        Log.i("jestar", list.toString())
        return list
    }

    private fun getList(cursor: Cursor?): ArrayList<ContactBean> {
        val list = ArrayList<ContactBean>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val contactBean = ContactBean().let {
                    it.id = cursor.getString(0)
                    it.contactId = cursor.getString(1)
                    it.rawContactId = cursor.getString(2)
                    try {
                        it.phone = cursor.getString(3)
                    } catch (e: Exception) {
                    }
                    it.name = cursor.getString(4)
                    it.accountType = cursor.getString(5)
                    it.accountName = cursor.getString(6)
                    it.mimeType = cursor.getString(7)
                    it
                }
                list.add(contactBean)
            }
            cursor.close()
        }
        return list
    }

    private fun getRawContactList(cursor: Cursor?): ArrayList<ContactBean> {
        val list = ArrayList<ContactBean>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val contactBean = ContactBean().let {
                    it.id = cursor.getString(0)
                    it.contactId = cursor.getString(1)
                    it.name = cursor.getString(2)
                    it.accountType = cursor.getString(3)
                    it.accountName = cursor.getString(4)
                    it
                }
                list.add(contactBean)
            }
            cursor.close()
        }
        return list
    }


    private fun query(
        uri: Uri,
        projection: Array<String>? = null,
        selection: String? = null,
        selectArgs: Array<String>? = null,
        sort: String? = null
    ): Cursor? {
        return provider.query(uri, projection, selection, selectArgs, sort)
    }

    /**
     * 删除当前用户所有联系人数据(Data和RawContact)
     */
    fun deleteContactByAccount(name: String) {
//        val selection="${ContactsContract.RawContacts.ACCOUNT_TYPE}=?"
//        val arg= arrayOf(ACCOUNT_TYPE)
        val args = arrayOf(ACCOUNT_TYPE, name)
        var delete = provider.delete(
            ContactsContract.Data.CONTENT_URI,
            accountTypeAndNameSelection,
            args
        )
        Log.i("jestar", "delete=$delete")
        val build = ContactsContract.RawContacts.CONTENT_URI.buildUpon()
            .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build()
        delete = provider.delete(build, accountTypeAndNameSelection, args)
        Log.i("jestar", "delete=$delete")
    }
}