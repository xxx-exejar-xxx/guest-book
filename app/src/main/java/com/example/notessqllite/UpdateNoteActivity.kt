package com.example.notessqllite

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqllite.databinding.ActivityUpdateBinding
import java.util.Calendar

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: NotesDatebaseHelper
    private var noteId: Int = -1

    // Variable to hold the image URI
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatebaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish()
            return
        }

        val note = db.getNoteByID(noteId)

        val img = note.image
        val bitmap = BitmapFactory.decodeByteArray(img, 0, img.size)
        binding.nameEditText.setText(note.name)
        binding.nicknameEditText.setText(note.nickname)
        binding.imageView3.setImageBitmap(bitmap)
        binding.emailEditText.setText(note.email)
        binding.addressEditText.setText(note.address)
        binding.dateofbirthEditText.setText(note.dateofbirth)
        binding.phoneEditText.setText(note.phone)

        binding.imageView3.setOnClickListener {
            showImageSourceOptions()
        }

        binding.updatesaveButton.setOnClickListener {
            val newname = binding.nameEditText.text.toString()
            val newnickname = binding.nicknameEditText.text.toString()
            val newimage = db.ImageViewToByte(binding.imageView3)
            val newemail = binding.emailEditText.text.toString()
            val newaddress = binding.addressEditText.text.toString()
            val newdateofbirth = binding.dateofbirthEditText.text.toString()
            val newphone = binding.phoneEditText.text.toString()
            val updatedNote = Note(noteId, newname, newnickname, newimage, newemail, newaddress, newdateofbirth, newphone)
            db.updateNote(updatedNote)
            finish()
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
        }

        // Optionally, you can set up the date picker dialog
        binding.dateofbirthEditText.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showImageSourceOptions() {
        val options = arrayOf("Camera", "Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Image Source")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera() // Camera option
                1 -> openGallery() // Gallery option
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Check if there is a camera app available to handle the intent
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
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
                    binding.imageView3.setImageBitmap(bitmap)
                }
                REQUEST_IMAGE_PICK -> {
                    val selectedImageUri = data?.data
                    binding.imageView3.setImageURI(selectedImageUri)
                }
            }
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
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear" // Month is 0-based
            binding.dateofbirthEditText.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_PICK = 2
    }
}