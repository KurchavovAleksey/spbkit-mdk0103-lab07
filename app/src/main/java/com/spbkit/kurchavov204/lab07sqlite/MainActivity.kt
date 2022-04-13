package com.spbkit.kurchavov204.lab07sqlite

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Button
import android.widget.EditText

object ContractClass { // Курчавов Алексей 204 группа СПБКИТ
    object DbEntry : BaseColumns {
        const val TABLE_NAME = "store"
        const val COLUMN_NAME_KEY = "key" // Курчавов Алексей 204 группа СПБКИТ
        const val COLUMN_NAME_VALUE = "value"
    }
    const val SQL_CREATE_STORE = "create table ${DbEntry.TABLE_NAME} (" +
            "${DbEntry.COLUMN_NAME_KEY} primary key, " +
            "${DbEntry.COLUMN_NAME_VALUE} text" +
            ");" // Курчавов Алексей 204 группа СПБКИТ
}

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) { // Курчавов Алексей 204 группа СПБКИТ
        db?.execSQL(ContractClass.SQL_CREATE_STORE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, v1: Int, v2: Int) {} // Курчавов Алексей 204 группа СПБКИТ

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "store.sqlite"
    } // Курчавов Алексей 204 группа СПБКИТ
}

class MainActivity : AppCompatActivity() {
    private var dbHelper: DBHelper? = null
    private var db: SQLiteDatabase? = null // Курчавов Алексей 204 группа СПБКИТ
    private val key: String
        get() = findViewById<EditText>(R.id.keyEditText).text.toString()
    private var value: String
        get() = findViewById<EditText>(R.id.valueEditText).text.toString()
        set(value) { // Курчавов Алексей 204 группа СПБКИТ
            findViewById<EditText>(R.id.valueEditText).setText(value)
        }

    private fun notify(title: String, msg: String) {
        val builder = AlertDialog.Builder(this)  // Курчавов Алексей 204 группа СПБКИТ
        builder.create() // Курчавов Алексей 204 группа СПБКИТ
        builder.setTitle(title)
        builder.setMessage(msg)  // Курчавов Алексей 204 группа СПБКИТ
        builder.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Курчавов Алексей 204 группа СПБКИТ
        dbHelper = DBHelper(this)
        db = dbHelper?.writableDatabase

        findViewById<Button>(R.id.insertButton).setOnClickListener {
 // Курчавов Алексей 204 группа СПБКИТ
            try {
                val values = ContentValues().apply {
                put(ContractClass.DbEntry.COLUMN_NAME_KEY, key)
                put(ContractClass.DbEntry.COLUMN_NAME_VALUE, value) // Курчавов Алексей 204 группа СПБКИТ
                }
                db?.insertOrThrow(ContractClass.DbEntry.TABLE_NAME, null, values)

            } catch (e: android.database.sqlite.SQLiteConstraintException) { // Курчавов Алексей 204 группа СПБКИТ
                notify(getString(R.string.integrity_error), getString(R.string.key_exists))
                return@setOnClickListener
            }
        } // Курчавов Алексей 204 группа СПБКИТ

        findViewById<Button>(R.id.updateButton).setOnClickListener {
            val toUpdate = "${ContractClass.DbEntry.COLUMN_NAME_KEY} = ?"
            val selectionArgs = arrayOf(key) // Курчавов Алексей 204 группа СПБКИТ
            val newValue = ContentValues().apply {
                put(ContractClass.DbEntry.COLUMN_NAME_VALUE, value)
            }
            val rowsAffected = db?.update( // Курчавов Алексей 204 группа СПБКИТ
                ContractClass.DbEntry.TABLE_NAME,
                newValue,
                toUpdate,
                selectionArgs
            ) // Курчавов Алексей 204 группа СПБКИТ
            if (rowsAffected != 1) {
                notify(getString(R.string.error), getString(R.string.key_not_exists))
            }
        }
         // Курчавов Алексей 204 группа СПБКИТ
        findViewById<Button>(R.id.selectButton).setOnClickListener {
            val cursor = db?.query(
                ContractClass.DbEntry.TABLE_NAME,
                arrayOf(ContractClass.DbEntry.COLUMN_NAME_VALUE), // Курчавов Алексей 204 группа СПБКИТ
                "${ContractClass.DbEntry.COLUMN_NAME_KEY} = ?",
                arrayOf(key),
                null,
                null, // Курчавов Алексей 204 группа СПБКИТ
                null
            )
            if (cursor?.count == 1) {
                cursor.moveToFirst()
                value = cursor.getString(0)
            } // Курчавов Алексей 204 группа СПБКИТ
            else {
                notify(getString(R.string.error), getString(R.string.key_not_exists))
            }

            cursor?.close() // Курчавов Алексей 204 группа СПБКИТ
        }

        findViewById<Button>(R.id.deleteButton).setOnClickListener {
            val affectedRowsCount = db?.delete(
                ContractClass.DbEntry.TABLE_NAME,
                "${ContractClass.DbEntry.COLUMN_NAME_KEY} = ?",
                arrayOf(key) // Курчавов Алексей 204 группа СПБКИТ
                )
            if (affectedRowsCount == 1) {
                notify(getString(R.string.delete), getString(R.string.deleted_successfully))
            } else {
                notify(getString(R.string.error), getString(R.string.key_not_exists)) // Курчавов Алексей 204 группа СПБКИТ
            }
        }

    }

    override fun onDestroy() {
        db?.close()
        super.onDestroy()
    } // Курчавов Алексей 204 группа СПБКИТ
}
