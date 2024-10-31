package com.example.notessqllite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notessqllite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var db : NotesDatebaseHelper
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatebaseHelper(this)
        notesAdapter = NotesAdapter(db.getAllNotes(),this)

        binding.notesRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerview.adapter = notesAdapter

        binding.addButton.setOnClickListener{
            val intent = Intent(this,AddNoteActivity::class.java)
            startActivity(intent)
        }


        binding.aboutButton.setOnClickListener{
            val intent =Intent(this,about::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }
}