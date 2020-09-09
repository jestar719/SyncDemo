package cn.jestar.syncdemo.contact

import android.provider.ContactsContract

val ACCOUT_TAG = "Jestar"
val ACCOUNT_TYPE = "cn.jestar.syncdemo"
val ACCOUT_MIME_TYPE = "vnd.android.cursor.item/vnd.cn.jestar.sync.link"


class CustomColumns {
    companion object {
        /**
         * MIME-type used when storing a profile [Data] entry.
         */
        val MIME_PROFILE = "vnd.android.cursor.item/vnd.cn.jestar.sync.link"

        val DATA_PHONE = ContactsContract.Data.DATA1

        val DATA_SUMMARY = ContactsContract.Data.DATA2

        val DATA_DETAIL = ContactsContract.Data.DATA3
    }
}