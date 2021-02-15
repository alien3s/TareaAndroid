package com.example.coso

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import android.Manifest
import android.Manifest.permission.CAMERA
import android.content.ContentValues

class MainActivity : AppCompatActivity() {
    private var name: EditText? = null
    private var surname: EditText? = null
    private var emailAddress: EditText? = null
    private var phoneNumber: EditText? = null
    private var imageView : ImageView?=null
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
            val imageBitmap = imageView?.let { SV(it) }
            val row = ContentValues().apply {
                put(ContactsContract.CommonDataKinds.Photo.PHOTO,
                    imageBitmap?.let { BIT(it) })
                put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
            }
            val data = arrayListOf(row)

            putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)
        }
        startActivity(intent)
    }
    private fun takePicture() {

        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,1 )

    }
    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(CAMERA), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    takePicture()

                } else {
                    Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {

            }
        }
        fun takePicture() {

            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1)

        }
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == 1) {
                    val bitmap = data!!.extras!!.get("data") as Bitmap
                    imageView?.setImageBitmap(bitmap)
                }

            }
        }
    }


    private fun SV(view: View): Bitmap {
        val specSize = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED)
        view.measure(specSize, specSize)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(canvas)
        return bitmap
    }



    private fun BIT(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }

    fun subirFoto(view: View) {
        if (checkPermission()){
            takePicture()
        }
        else{
            requestPermission()
        }
    }

}