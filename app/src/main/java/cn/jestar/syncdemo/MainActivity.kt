package cn.jestar.syncdemo;

import android.Manifest
import android.accounts.Account
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cn.jestar.syncdemo.contact.ContactBean
import cn.jestar.syncdemo.contact.SyncManager

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<ContactBean>
    private val PERMISSION_REQUEST: Int = 0x19
    private var account: Account? = null
    private lateinit var etInput: EditText
    private lateinit var tvTitle: TextView
    private lateinit var btnAddAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle = findViewById<TextView>(R.id.tv_title)
        etInput = findViewById<EditText>(R.id.et_input)
        btnAddAccount = findViewById<Button>(R.id.btn_add_account)
        btnAddAccount.setOnClickListener { onAddAccount() }
        findViewById<Button>(R.id.btn_sync).setOnClickListener { onSync() }
        findViewById<Button>(R.id.btn_manual_write_data).setOnClickListener { onManual() }
        findViewById<Button>(R.id.btn_delete_contact_data).setOnClickListener { onDeleteData() }
        adapter =
            ArrayAdapter<ContactBean>(this, android.R.layout.simple_list_item_1)
        findViewById<ListView>(R.id.lv_data).adapter = adapter
    }

    private fun onDeleteData() {
        if (account != null) {
            val deleteData = SyncManager.deleteData(account!!.name)
            setAdapter(deleteData)
        }
    }

    override fun onResume() {
        super.onResume()
        account = SyncManager.getAccount()
        showInput(account)
    }

    private fun setAdapter(list: ArrayList<ContactBean>) {
        adapter.clear()
        adapter.addAll(list)
    }

    private fun onAddAccount() {
        account = SyncManager.addAccount(etInput.editableText.toString())
        showInput(account)
    }

    private fun onManual() {
        if (checkPermission()) {
            manualWriteContact()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
                ), PERMISSION_REQUEST
            )
        }
    }

    private fun manualWriteContact() {
        if (account != null) {
            val sync = SyncManager.sync(account!!)
            setAdapter(sync)
        }
    }

    private fun onSync() {
        if (account != null) {
            SyncManager.forceSync(account!!)
        }
    }

    private fun showInput(account: Account?) {
        val nullAccount = account == null
        val visible = if (nullAccount) View.VISIBLE else View.GONE
        if (!nullAccount) {
            tvTitle.text = account!!.name
            if (checkPermission()) {
                val accountData = SyncManager.getAccountData(account.name)
                setAdapter(accountData)
            }
        }
        etInput.visibility = visible
        btnAddAccount.visibility = visible

    }

    private fun checkPermission(): Boolean {
        val checkSelfPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS)
        return checkSelfPermission == PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
            manualWriteContact()
        }
    }
}
