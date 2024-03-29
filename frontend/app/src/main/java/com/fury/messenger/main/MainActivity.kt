package com.fury.messenger.main

//import com.fury.messenger.middlewaregrpc.MiddlewareGrpc
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.contactlist.ContactListActivity
import com.fury.messenger.data.db.DBMessage
import com.fury.messenger.data.db.DBUser
import com.fury.messenger.data.db.DbConnect
import com.fury.messenger.editprofile.EditProfile
import com.fury.messenger.helper.contact.ContactChats
import com.fury.messenger.helper.contact.Contacts
import com.fury.messenger.helper.user.AppDatabase
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.manageBuilder.createAuthenticationStub
import com.fury.messenger.rsa.RSA.initRSA
import com.fury.messenger.ui.login.LoginActivity
import com.fury.messenger.utils.TokenManager
import com.services.ServicesGrpc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var userListView: RecyclerView
    private var userList: ArrayList<ContactChats> = arrayListOf<ContactChats>()
    private lateinit var recentUsers: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: UserAdapter
    private lateinit var pinnedUserAdapter: PinnedUserAdapter
    private lateinit var db: AppDatabase
    private var hasReadContactPermission: Boolean = false
    private var hasStoragePermission: Boolean = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var tokenManager: TokenManager
    private var scope = CoroutineScope(Dispatchers.IO)
    private lateinit var progressBar: ProgressBar
    private var selected: Int? = null
    private lateinit var client: ServicesGrpc.ServicesBlockingStub
    private lateinit var contactButton: Button
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
        db = DbConnect.getDatabase(this)
        progressBar = findViewById(R.id.progressBar)
        searchView = findViewById(R.id.searchView)
        val ctx = this
        initRSA(ctx)
        this@MainActivity.progressBar.isVisible = true
        searchView.clearFocus()
        client = createAuthenticationStub(CurrentUser.getToken())


        contactButton = findViewById(R.id.contactButton)

        Log.d("Contact-z", userList.size.toString())

        tokenManager = TokenManager(this)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#696969")))
        pinnedUserAdapter = PinnedUserAdapter(this, userList, fun(pos: Int?) {
            this.selected = pos
        })
        adapter = UserAdapter(this, userList, fun(pos: Int?) {
            Log.d("this.selected.toString()", "UserAdapter $pos")
            this.selected = pos
        })
        userListView = findViewById(R.id.userslist)
        recentUsers = findViewById(R.id.recentlist)
        userListView.layoutManager = LinearLayoutManager(this)
        recentUsers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        registerForContextMenu(userListView)


        userListView.adapter = adapter
        recentUsers.adapter = pinnedUserAdapter


        contactButton.setOnClickListener {
            startActivity(Intent(this, ContactListActivity::class.java))
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val filteredList = this@MainActivity.userList.filter {
                        it.contact.name?.contains(newText, ignoreCase = true)
                            ?: false
                    }

                    if (filteredList.isEmpty()) {
                        Toast.makeText(this@MainActivity, "No contacts found", Toast.LENGTH_SHORT)
                            .show()
                        this@MainActivity.adapter.userList = arrayListOf()

                    } else {
                        this@MainActivity.adapter.userList = filteredList as ArrayList<ContactChats>
                    }


                } else {
                    this@MainActivity.adapter.userList = this@MainActivity.userList
                }
                this@MainActivity.adapter.notifyDataSetChanged()

                return true
            }
        })
        scope.launch {

            withContext(Dispatchers.IO) {
                try {

                    contacts.getContactsFromPhone()
                    contacts.validateContacts()
                    val data = contacts.getAllVerifiedContacts()
                    val contactsList = DBUser.getAllLastMessagesForContact(
                        this@MainActivity,
                        data
                    )

                    runOnUiThread {
                        this@MainActivity.setContactList(
                            contactsList
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
        scope.launch {
            CurrentUser.startMessageThread(ctx)

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

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {

        val deleteMenuItem = menu.findItem(R.id.deleteItem)
        val blockMenuItem = menu.findItem(R.id.block)
        val pinMenuItem = menu.findItem(R.id.pin)

        if (this.classLoader !== MainActivity::class && deleteMenuItem != null) {
            deleteMenuItem.isVisible = false
        }
        if (this.selected != null) {
            val contact = this.userList[this.selected!!]
            if (!contact.contact.isVerified) {
                blockMenuItem.isVisible = false
                pinMenuItem.isVisible = false

            }
            if (CurrentUser.isBlocked(contact.contact.phoneNumber)) {

                blockMenuItem.title = "Un block"
            }
            if (this.userList[this.selected!!].contact.isPinned == true) {

                pinMenuItem.title = "Un pinned"
                pinMenuItem.isVisible = false
            }
        }
        return true

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as? AdapterView.AdapterContextMenuInfo
        val selectedItem = this.selected?.let { userList[it] }

        if (selectedItem != null) {

            when (item.itemId) {
                R.id.deleteItem -> {
                    scope.launch {
                        this@MainActivity.db.chatsDao()
                            .deleteAllChats(selectedItem.contact.phoneNumber)

                    }
                    userList.remove(selectedItem)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(
                        this,
                        "Deleted: ${selectedItem.contact.phoneNumber}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return true

                }

                R.id.pin -> {

                    scope.launch {
                        if (selectedItem.contact.isPinned == true) {
                            selectedItem.contact.isPinned = false
                            db.contactDao().update(selectedItem.contact)
                            runOnUiThread {
                                item.title = "Pin"
                            }
                        } else {
                            selectedItem.contact.isPinned = true
                            db.contactDao().update(selectedItem.contact)
                            runOnUiThread {
                                item.title = "Un Pin"
                            }
                        }
                        runOnUiThread {
                            this@MainActivity.userList[this@MainActivity.selected!!] = selectedItem
                            pinnedUserAdapter.userList =
                                userList.filter { it.contact.isPinned == true } as ArrayList<ContactChats>
                            this@MainActivity.pinnedUserAdapter.notifyDataSetChanged()
                        }
                    }

                }

                R.id.block -> {
                    Log.d("onContextItemSelected", "isPinned")

                    scope.launch {
                       DBMessage.blockUser(selectedItem.contact.phoneNumber)
                    }

                }
            }
        }
        return super.onContextItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setContactList(data: ArrayList<ContactChats>) {
        super.onResume()
        userList = data
        pinnedUserAdapter.userList =
            userList.filter { it.contact.isPinned == true } as ArrayList<ContactChats>
        adapter.userList = userList

        pinnedUserAdapter.notifyDataSetChanged()
        adapter.notifyDataSetChanged()

    }
}