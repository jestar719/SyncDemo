package cn.jestar.syncdemo

import cn.jestar.syncdemo.sync.ContactProfile

class SimpleContact(private val name: String, private val phone: String) : ContactProfile {
    private val id = name + phone
    override fun getContactName(): String {
        return name
    }

    override fun getContactPhone(): String {
        return phone
    }

    override fun getAppId(): String {
        return id
    }
}