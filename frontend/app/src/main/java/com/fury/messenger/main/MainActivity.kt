package com.fury.messenger.main

//import com.fury.messenger.middlewaregrpc.MiddlewareGrpc
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.data.db.DBHelper
import com.fury.messenger.data.db.DBUser
import com.fury.messenger.data.db.DbConnect
import com.fury.messenger.editprofile.EditProfile
import com.fury.messenger.helper.contact.ContactChats
import com.fury.messenger.helper.contact.Contacts
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.rsa.RSA.initRSA
import com.fury.messenger.ui.login.LoginActivity
import com.fury.messenger.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var userListView: RecyclerView
    private var userList: ArrayList<ContactChats> = arrayListOf<ContactChats>()
    private lateinit var recentUsers: RecyclerView

    private lateinit var adapter: UserAdapter
    private lateinit var recentAdapter: RecentAdapter
    private lateinit var db: SQLiteOpenHelper
    private var hasReadContactPermission: Boolean = false
    private var hasStoragePermission: Boolean = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var tokenManager: TokenManager;
    private var scope = CoroutineScope(Dispatchers.IO)
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
                hasStoragePermission =
                    permission[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: hasStoragePermission
                hasReadContactPermission =
                    permission[Manifest.permission.READ_CONTACTS] ?: hasReadContactPermission

            }
        requestPermission()
        val contacts = Contacts(this)
        DbConnect.getDatabase(this)
        progressBar = findViewById(R.id.progressBar)

        val ctx = this
        initRSA(ctx)
        this@MainActivity.progressBar.isVisible = true

        scope.launch {
            withContext(Dispatchers.IO) {
             try {

                 contacts.getContactsFromPhone()
                 contacts.validateContacts()
                 val data = contacts.getAllVerifiedContacts()
                 val contacts=    DBUser.getAllLastMessagesForContact(
                     this@MainActivity,
                     data
                 )

                runOnUiThread{
                    this@MainActivity.setContactList(
                        contacts
                    )

                }
             } catch (e: Error) {
                 Log.d("Error", e.toString())

             } finally {
                 runOnUiThread {
                     this@MainActivity.progressBar.isVisible = false

                 }

             }

         }
     }


        Log.d("Contact-z", userList.size.toString())

        tokenManager = TokenManager(this)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#696969")))
        recentAdapter = RecentAdapter(this, userList)
        adapter = UserAdapter(this, userList)
        userListView = findViewById(R.id.userslist)
        recentUsers = findViewById(R.id.recentlist)
        userListView.layoutManager = LinearLayoutManager(this)
        recentUsers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)



        userListView.adapter = adapter
        recentUsers.adapter = recentAdapter

        scope.launch {
            db = DBHelper(ctx)
            CurrentUser.startMessageThread(ctx, db)
            db.close()

        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            tokenManager.deleteToken()
            val intent = Intent(this, LoginActivity::class.java)

            // finish()
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.editProfile) {
            val intent = Intent(this, EditProfile::class.java)

            // finish()
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun requestPermission() {
        Log.d("ask permission", "")
        hasReadContactPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        hasStoragePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val permissionRequest: MutableList<String> = ArrayList()
        if (!hasStoragePermission) {
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionRequest.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)

        }
        if (!hasReadContactPermission) {
            permissionRequest.add(Manifest.permission.READ_CONTACTS)
        }
        permissionLauncher.launch(permissionRequest.toTypedArray())
    }

    private fun setContactList(data: ArrayList<ContactChats>) {
        super.onResume()
        Log.d("Thread-Messenger updating contacts", data.size.toString())
        userList = data
        recentAdapter.userList = userList
        adapter.userList = userList

        recentAdapter.notifyDataSetChanged()
        adapter.notifyDataSetChanged()

    }
}