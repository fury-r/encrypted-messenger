package com.fury.messenger.contactlist

//import com.fury.messenger.middlewaregrpc.MiddlewareGrpc
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.data.db.DBMessage
import com.fury.messenger.data.db.DBUser
import com.fury.messenger.data.db.DbConnect.getDatabase
import com.fury.messenger.editprofile.EditProfile
import com.fury.messenger.helper.contact.ContactChats
import com.fury.messenger.helper.contact.Contacts
import com.fury.messenger.helper.user.AppDatabase
import com.fury.messenger.helper.user.CurrentUser
import com.fury.messenger.main.UserAdapter
import com.fury.messenger.rsa.RSA.initRSA
import com.fury.messenger.ui.login.LoginActivity
import com.fury.messenger.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactListActivity : AppCompatActivity() {

    private lateinit var userListView: RecyclerView
    private var userList: ArrayList<ContactChats> = arrayListOf<ContactChats>()
    private lateinit var searchView: SearchView
    private lateinit var adapter: UserAdapter
    private lateinit var db: AppDatabase
    private lateinit var tokenManager: TokenManager;
    private var scope = CoroutineScope(Dispatchers.IO)
    private lateinit var progressBar: ProgressBar
    private var selected: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        val contacts = Contacts(this)
        progressBar = findViewById(R.id.progressBar)
        searchView = findViewById(R.id.searchView)
        db = getDatabase(this)
        val ctx = this
        initRSA(ctx)
        this@ContactListActivity.progressBar.isVisible = true
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the list based on the search query
                if (newText != null) {
                    val filteredList = this@ContactListActivity.userList.filter {
                        it.contact.name?.contains(newText, ignoreCase = true)
                            ?: false
                    }
                    if (filteredList.isEmpty()) {
                        Toast.makeText(this@ContactListActivity, "No contacts found", Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        this@ContactListActivity.adapter.userList = filteredList as ArrayList<ContactChats>
                    }


                } else {
                    this@ContactListActivity.adapter.userList = this@ContactListActivity.userList
                }
                this@ContactListActivity.adapter.notifyDataSetChanged()

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
                        this@ContactListActivity,
                        data
                    )

                    runOnUiThread {
                        this@ContactListActivity.setContactList(
                            contactsList
                        )

                    }
                } catch (e: Error) {
                    Log.d("Error", e.toString())

                } finally {
                    runOnUiThread {
                        this@ContactListActivity.progressBar.isVisible = false

                    }

                }

            }
        }


        Log.d("Contact-z", userList.size.toString())

        tokenManager = TokenManager(this)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#696969")))
        adapter = UserAdapter(this, userList,fun(pos:Int?){
            this.selected=pos
        },true)
        userListView = findViewById(R.id.contactlist)
        userListView.layoutManager = LinearLayoutManager(this)

        registerForContextMenu(userListView)


        userListView.adapter = adapter

        scope.launch {
            CurrentUser.startMessageThread(ctx)

        }


    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }



    @SuppressLint("NotifyDataSetChanged")
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val selectedItem = this.selected?.let { userList[it] }

        if(selectedItem!=null){

            when (item.itemId) {

                R.id.pin->{

                    scope.launch {
                        if(selectedItem.contact.isPinned==true){
                            selectedItem.contact.isPinned=false
                            db.contactDao().update(selectedItem.contact)

                        }
                        else{
                            selectedItem.contact.isPinned=true
                            db.contactDao().update(selectedItem.contact)

                        }

                    }

                }
                R.id.block->{
                    Log.d("onContextItemSelected","isPinned")

                    scope.launch {
                        DBMessage.blockUser(selectedItem.contact.phoneNumber)
                    }
                }
            }
        }
        return super.onContextItemSelected(item)
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


    @SuppressLint("NotifyDataSetChanged")
    private fun setContactList(data: ArrayList<ContactChats>) {
        super.onResume()
        Log.d("Thread-Messenger updating contacts", data.size.toString())

        adapter.userList = data


        adapter.notifyDataSetChanged()

    }
}