package cn.jestar.syncdemo.sync

import android.provider.ContactsContract

/**
 * 联系人数据列
 */
interface ContactProfile {
    companion object {
        const val COLUMN_ID = ContactsContract.Contacts._ID
        const val COLUMN_NAME = ContactsContract.Data.DISPLAY_NAME
        const val COLUMN_PHONE = ContactsContract.Data.DATA1
        const val COLUMN_LABEL = ContactsContract.Data.DATA2
        const val COLUMN_MSG = ContactsContract.Data.DATA3
        const val COLUMN_ACCOUNT_NUMBER = ContactsContract.Data.SYNC1
        const val COLUMN_LOGIN_ID = ContactsContract.Data.SYNC2
    }
    fun getContactName():String
    fun getContactPhone():String
    fun getAppId():String
}