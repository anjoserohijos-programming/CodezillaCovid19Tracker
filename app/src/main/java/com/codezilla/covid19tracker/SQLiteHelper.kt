package com.codezilla.covid19tracker

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.createBitmap

class SQLiteHelper (context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_NAME = "codezilla_covid19tracker.db"
        private const val DATABASE_VERSION = 1

        //database variables
        private const val ENTRIES_TABLE = "tbl_entries"
        private const val ID = "id"
        private const val NAME = "name"
        private const val HOME_ADDRESS = "homeaddress"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableEntries = ("create table "+ ENTRIES_TABLE +"(" +
                ID + " integer primary key autoincrement, " +
                NAME + " text, " +
                HOME_ADDRESS + " text" + ")"
                )
        db?.execSQL(createTableEntries)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $ENTRIES_TABLE")
        onCreate(db)
    }
    fun insertEntry(std: CovidTrackerModel): Long{

        val db = this.writableDatabase
        val contentValues = ContentValues()
        //contentValues.put(ID, std.getAutoId())
        contentValues.put(NAME, std.name)
        contentValues.put(HOME_ADDRESS, std.homeAddress)

        val success = db.insert(ENTRIES_TABLE, null, contentValues)
        db.close()
        return success
    }

    fun getAllEntries(): ArrayList<CovidTrackerModel>{
        val stdList:  ArrayList<CovidTrackerModel> = ArrayList()
        val selectQuery = "select * from $ENTRIES_TABLE order by id desc"
        val db = this.readableDatabase
        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch(e: Exception){
            db.execSQL(selectQuery)
            e.printStackTrace()
            return ArrayList()
        }
        var id: Int
        var name: String
        var homeAddress: String

        if(cursor.moveToFirst()){

            do{
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                homeAddress = cursor.getString(cursor.getColumnIndexOrThrow("homeaddress"))
                val cvdtrk = CovidTrackerModel(id, name, homeAddress)
                stdList.add(cvdtrk)
            }while(cursor.moveToNext())
        }
        return stdList
    }

    fun updateStudent(std: CovidTrackerModel): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(NAME, std.name)
        contentValues.put(HOME_ADDRESS, std.homeAddress)

        val success = db.update(ENTRIES_TABLE,  contentValues, "id= "+std.id, null)
        db.close()
        return success
    }

     fun deleteEntryById(id: Int): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(ENTRIES_TABLE, "id= $id", null )
        db.close()
        return success

    }
}