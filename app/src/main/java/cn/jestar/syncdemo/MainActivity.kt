package cn.jestar.syncdemo;

import android.accounts.Account;
import android.os.Bundle;
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity;
import cn.jestar.syncdemo.contact.SyncManager;

class MainActivity : AppCompatActivity() {

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
        account = SyncManager.getAccount()
        showInput(account)
    }

    private fun onAddAccount() {
         account = SyncManager.addAccount(etInput.editableText.toString())
          showInput(account)
    }

    private fun onManual() {
        if (account != null)
            SyncManager.sync(account!!)
    }

    private fun onSync() {
        if (account != null)
            SyncManager.forceSync(account!!, this)
    }

    private fun showInput(account: Account?) {
        val nullAccount = account == null
        val visible = if (nullAccount) View.VISIBLE else View.GONE
        if (!nullAccount)
            tvTitle.text = account!!.name
        etInput.visibility = visible
        btnAddAccount.visibility = visible
    }
}
