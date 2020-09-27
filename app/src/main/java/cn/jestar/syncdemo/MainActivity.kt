package cn.jestar.syncdemo;

import android.Manifest
import android.accounts.Account
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST: Int = 0x19
    private var account: Account? = null
    private val buttons: MutableList<View> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews()
        checkAccount()
        checkMyPermission()
    }

    private fun checkAccount() {
        account = ContactSyncManager.getAccount()
        val hasAccount = account == null
        if (hasAccount) {
            et_input_name.setHint(R.string.please_sign_in)
            et_input_phone.visibility = View.GONE
            tv_title.setText(R.string.please_input_contact_name)
            btn_add_account.visibility = View.VISIBLE
        } else {
            tv_title.text = account!!.name
            btn_add_account.visibility = View.VISIBLE
            btn_add_account.visibility = View.GONE
            tv_title.setText(R.string.please_input_account_name)
        }
        val size = buttons.size
        val visible = if (hasAccount) View.VISIBLE else View.GONE
        for (i in 0 until size) {
            buttons[i].visibility = visible
        }
    }

    private fun initViews() {
        buttons.add(btn_add_account)
        buttons.add(btn_sync)
        buttons.add(btn_manual_sync)
        buttons.add(btn_delete_contact_data)
        btn_add_account.setOnClickListener { addAccount() }
        btn_sync.setOnClickListener { sync() }
        btn_manual_sync.setOnClickListener { manualSync() }
        btn_delete_contact_data.setOnClickListener { deleteContact() }
        btn_delete_account.setOnClickListener { deleteAccount() }
    }


    private fun addAccount() {
        val name = et_input_name.editableText.toString()
        if (name.isNotEmpty()) {
            ContactSyncManager.addAccount(name)
            checkAccount()
        } else {
            showMsg(R.string.account_name_not_be_null)
        }
    }

    private fun sync() {
        ContactSyncManager.sync()
        showMsg(R.string.start_sync)
    }

    private fun manualSync() {
        val name = et_input_name.editableText.toString()
        if (name.isEmpty()) {
            showMsg(R.string.contact_name_not_be_null)
            return
        }
        val phone = et_input_phone.editableText.toString()
        if (name.isEmpty()) {
            showMsg(R.string.contact_name_not_be_null)
            return
        }
        ContactSyncManager.addContact(name, phone)
        showMsg(R.string.add_contact_success)
    }

    private fun deleteContact() {
        ContactSyncManager.deleteContacts()
        showMsg(R.string.delete_contact_success)
    }

    private fun deleteAccount() {
        ContactSyncManager.deleteAccount()
        showMsg(R.string.delete_account_success)
        checkAccount()
    }

    private fun showMsg(@StringRes msgId: Int) {
        Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show()
    }

    private fun checkMyPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (PermissionChecker.checkSelfPermission(this, Manifest.permission_group.CONTACTS) != PermissionChecker.PERMISSION_DENIED) {
                enableButtons(false)
                requestPermissions(arrayOf(Manifest.permission_group.CONTACTS), PERMISSION_REQUEST)
            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val hasPermission = requestCode == PERMISSION_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PermissionChecker.PERMISSION_DENIED
        enableButtons(hasPermission)
    }

    private fun enableButtons(enable: Boolean) {
        for (button in buttons) {
            button.isEnabled = enable
            button.isClickable = enable
        }
    }
}
