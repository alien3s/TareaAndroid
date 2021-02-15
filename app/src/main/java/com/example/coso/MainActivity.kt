package com.example.coso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private var name: EditText? = null
    private var surname: EditText? = null
    private var emailAddress: EditText? = null
    private var phoneNumber: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun add(view: View) {
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            // Sets the MIME type to match the Contacts Provider
            type = ContactsContract.RawContacts.CONTENT_TYPE
        }
        name = findViewById(R.id.name)
        surname = findViewById(R.id.surn)
        emailAddress = findViewById(R.id.email)
        phoneNumber = findViewById(R.id.tlf)
        var fullname = name?.text.toString() + " " + surname?.text.toString()
        /*
         * Inserts new data into the Intent. This data is passed to the
         * contacts app's Insert screen
         */
        intent.apply {
            // Inserts an email address
            putExtra(ContactsContract.Intents.Insert.NAME, fullname)
            putExtra(ContactsContract.Intents.Insert.EMAIL, emailAddress?.text)
            /*
             * In this example, sets the email type to be a work email.
             * You can set other email types as necessary.
             */
            putExtra(
                ContactsContract.Intents.Insert.EMAIL_TYPE,
                ContactsContract.CommonDataKinds.Email.TYPE_WORK
            )
            // Inserts a phone number
            putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber?.text)
            /*
             * In this example, sets the phone type to be a work phone.
             * You can set other phone types as necessary.
             */
            putExtra(
                ContactsContract.Intents.Insert.PHONE_TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK
            )

        }
        startActivity(intent)
    }
}