package com.example.guestlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

// needs to be unique
const val LAST_GUEST_NAME_KEY = "last-guest-name-bundle-key"

class MainActivity : AppCompatActivity() {

    private lateinit var addGuestButton: Button
    private lateinit var newGuestEditText: EditText
    private lateinit var guestList: TextView
    private lateinit var lastGuestAdded: TextView
    private lateinit var clearListButton: Button

    // create mutable list to add and store guest names
    // initialized empty
//    var guestNames = mutableListOf<String>()

    // instead of creating guestNames var, replace with call to use guestList View Model

    private val guestListViewModel: GuestListViewModel by lazy {
        // lazy means initializing not until needed
        // this lambda function with return the guestListViewModel but only once it's needed
        // this is because onCreate needs to run first so can precede it
        // also will help with performance because we're not initializing everything at once
        ViewModelProvider(this).get(GuestListViewModel::class.java)
        // makes app life-cycle aware
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addGuestButton = findViewById(R.id.add_guest_button)
        newGuestEditText = findViewById(R.id.new_guest_input)
        guestList = findViewById(R.id.list_of_guests)
        lastGuestAdded = findViewById(R.id.last_guest_added)
        clearListButton = findViewById(R.id.clear_guests_button)

        addGuestButton.setOnClickListener {
            addNewGuest()
        }

        clearListButton.setOnClickListener {
            resetGuestList()
        }

        // if no bundle, this will be null
        val savedLastGuestMessage = savedInstanceState?.getString(LAST_GUEST_NAME_KEY)
        lastGuestAdded.text = savedLastGuestMessage

        // this will help get the stored data (if there is any) and display it
        // when phone is rotated or for whatever reason the activity is destroyed
        updateGuestList()
    }

    // having this function means the last guest name will be saved in a bundle when app is being shut down
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_GUEST_NAME_KEY, lastGuestAdded.text.toString())
    }

    private fun resetGuestList() {
        guestListViewModel.clearList()
        updateGuestList()
        lastGuestAdded.text = ""
    }

    private fun addNewGuest() {
        val newGuestName = newGuestEditText.text.toString()
        if (newGuestName.isNotBlank()) {
//            guestNames.add(newGuestName) - this was used when guest list was built in mainactivity
            guestListViewModel.addGuest(newGuestName)
            updateGuestList()
            newGuestEditText.text.clear()
            lastGuestAdded.text = getString(R.string.last_guest_message, newGuestName)
        }
    }

    private fun updateGuestList() {
        val guests = guestListViewModel.getSortedGuestNames()
//        val guestDisplay = guestNames.sorted().joinToString(separator = "\n")
        val guestDisplay = guests.joinToString(separator = "\n")
        guestList.text = guestDisplay
    }
}