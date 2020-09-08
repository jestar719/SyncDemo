package cn.jestar.syncdemo.sync.contact

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract

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
    val accountTypeAndNameSelection = "${ContactsContract.RawContacts.ACCOUNT_NAME}=? AND ${ContactsContract.RawContacts.ACCOUNT_NAME}=?"


    private val contactProjection = arrayOf(
            ContactsContract.Data._ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.RawContacts.ACCOUNT_TYPE,
            ContactsContract.RawContacts.ACCOUNT_NAME,
            ContactsContract.Contacts.Data.MIMETYPE
    )

    /**
     * 查询所有电话
     */
    fun queryContactWithPhone(): ArrayList<ContactBean> {
        val query = query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, contactProjection)
        return getList(query)
    }

    fun queryByCurrentAccount(name:String): ArrayList<ContactBean> {
        val list = getList(query(ContactsContract.RawContacts.CONTENT_URI, contactProjection, accountTypeAndNameSelection, arrayOf(ACCOUT_NAME, name)))
        val list1 = getList(query(ContactsContract.Data.CONTENT_URI, contactProjection, accountTypeAndNameSelection, arrayOf(ACCOUT_NAME, name)))
        list.addAll(list1)
        return list
    }

    private fun getList(cursor: Cursor?): ArrayList<ContactBean> {
        val list = ArrayList<ContactBean>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val contactBean = ContactBean().let {
                    it.id = cursor.getString(0)
                    it.contactId = cursor.getString(1)
                    try {     //查询rawcontact表时没有raw_contact_id
                        it.rawContactId = cursor.getString(2)
                    } catch (e: Exception) {
                    }
                    it.name = cursor.getString(3)
                    it.phone = cursor.getString(4)
                    it.accountType = cursor.getString(5)
                    it.accountName = cursor.getString(5)
                    it.mimeType = cursor.getString(7)
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
        var delete = provider.delete(
                ContactsContract.Data.CONTENT_URI,
                accountTypeAndNameSelection,
                arrayOf(ACCOUNT_TYPE, name)
        )
        val build = ContactsContract.RawContacts.CONTENT_URI.buildUpon()
                .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build()
        provider.delete(build, "${ContactsContract.RawContacts.ACCOUNT_NAME}=?", arrayOf(name))
    }
}