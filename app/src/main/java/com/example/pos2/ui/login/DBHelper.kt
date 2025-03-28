package com.example.pos2.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "UserDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE users (id INTEGER PRIMARY KEY, username TEXT, password TEXT, isAdmin INTEGER)"
        db.execSQL(createTable)
    }

    fun addUser(username: String, password: String, isAdmin: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("username", username)
        values.put("password", password)
        values.put("isAdmin", isAdmin)

        val cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", arrayOf(username))
        return if (cursor.count > 0) {
            cursor.close()
            false
        } else {
            db.insert("users", null, values)
            true
        }
    }

    fun checkUser(username: String, password: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT isAdmin FROM users WHERE username = ? AND password = ?", arrayOf(username, password))

        return if (cursor.moveToFirst()) {
            val isAdmin = cursor.getInt(0)
            cursor.close()
            isAdmin // 1 = Admin, 0 = User
        } else {
            cursor.close()
            -1 // ไม่พบผู้ใช้
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }
}
