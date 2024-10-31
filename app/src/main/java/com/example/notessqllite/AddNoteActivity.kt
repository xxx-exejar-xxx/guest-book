package com.example.notessqllite

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqllite.databinding.ActivityAddNoteBinding
import java.util.Calendar

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NotesDatebaseHelper
    private var imageUri: Uri? = null

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_PICK = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = NotesDatebaseHelper(this)

        // Set up the date picker for the date of birth
        binding.dateofbirthText.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up the image selection
        binding.imageView2.setOnClickListener {
            showImageSelectionDialog()
        }

        binding.saveButton.setOnClickListener {
            val name = binding.nameText.text.toString()
            val nickname = binding.nicknameText.text.toString()
            val image = db.ImageViewToByte(binding.imageView2)
            val email = binding.emailText.text.toString()
            val address = binding.addressText.text.toString()
            val dateofbirth = binding.dateofbirthText.text.toString()
            val phone = binding.phoneText.text.toString()

            val note = Note(0, name, nickname, image, email, address, dateofbirth, phone)
            db.insertNote(note)
            finish()
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePickerDialog() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Format the date and set it to the TextView
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear" // Month is 0-based
            binding.dateofbirthText.setText(formattedDate)
        }, year, month, day)

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    private fun showImageSelectionDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Image")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.imageView2.setImageBitmap(bitmap)
                }
                REQUEST_IMAGE_PICK -> {
                    imageUri = data?.data
                    binding.imageView2.setImageURI(imageUri)
                }
            }
        }
    }
}