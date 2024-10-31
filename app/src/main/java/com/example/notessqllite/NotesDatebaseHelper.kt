package com.example.notessqllite
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import java.io.ByteArrayOutputStream

class NotesDatebaseHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null,DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "booksapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "book"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_NICKNAME = "nickname"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_DATEOFBIRTH = "dateofbirth"
        private const val COLUMN_PHONE = "phone"

    }

    override fun onCreate(db: SQLiteDatabase?)
    {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT NOT NULL, " +
                "$COLUMN_NICKNAME TEXT NOT NULL, " +
                "$COLUMN_IMAGE BLOB NOT NULL," +
                "$COLUMN_EMAIL TEXT NOT NULL, " +
                "$COLUMN_ADDRESS TEXT, " +
                "$COLUMN_DATEOFBIRTH DATE," +
                "$COLUMN_PHONE TEXT NOT NULL)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertNote(note : Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, note.name)
            put(COLUMN_NICKNAME,  note.nickname)
            put(COLUMN_IMAGE, note.image)
            put(COLUMN_EMAIL,  note.email)
            put(COLUMN_ADDRESS, note.address)
            put(COLUMN_DATEOFBIRTH,  note.dateofbirth)
            put(COLUMN_PHONE, note.phone)
        }
        db.insert(TABLE_NAME,null,values)
        db.close()
    }


    fun getAllNotes(): List<Note>{
        val noteList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)

        while (cursor.moveToNext()){
            val id =cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name =cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val nickname =cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NICKNAME))
            val image =cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
            val email =cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val address =cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
            val dateOfBirth =cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATEOFBIRTH))
            val phone =cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))


            val note =Note(id,name,nickname,image,email,address,dateOfBirth,phone)
            noteList.add(note)
        }

        cursor.close()
        db.close()
        return noteList
    }

    fun updateNote(note: Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, note.name)
            put(COLUMN_NICKNAME,  note.nickname)
            put(COLUMN_IMAGE, note.image)
            put(COLUMN_EMAIL,  note.email)
            put(COLUMN_ADDRESS, note.address)
            put(COLUMN_DATEOFBIRTH,  note.dateofbirth)
            put(COLUMN_PHONE, note.phone)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values,whereClause, whereArgs)
        db.close()
    }

    fun getNoteByID(noteId:Int):Note{
        val db=readableDatabase
        val query ="SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor =db.rawQuery(query,null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val nickname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NICKNAME))
        val image =cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
        val email =cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
        val address =cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
        val dateOfBirth =cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATEOFBIRTH))
        val phone =cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))


        cursor.close()
        db.close()
        return Note(id,name,nickname,image,email,address,dateOfBirth,phone)
    }

    fun deletenote(noteId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }

    fun ImageViewToByte(img: ImageView): ByteArray {
        val bitmap: Bitmap = (img.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val bytes: ByteArray = stream.toByteArray()
        return bytes
    }
}