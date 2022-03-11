package com.raywenderlich.travelapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import java.lang.Exception

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "cities.db"
        private const val TBL_CITIES = "tbl_cities"
        private const val ID = "id"
        private const val CITY = "city"
        private const val COUNTRY = "country"
        private const val DESCRIPTION = "description"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblCities = ("CREATE TABLE " + TBL_CITIES + "("
                + ID + " INTEGER PRIMARY KEY," + CITY + " TEXT," + COUNTRY + " TEXT," + DESCRIPTION + " TEXT" + ")")
        db?.execSQL(createTblCities)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_CITIES")
        onCreate(db)
    }

    fun insertCity(cty: CityModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, cty.id)
        contentValues.put(CITY, cty.city)
        contentValues.put(COUNTRY, cty.country)
        contentValues.put(DESCRIPTION, cty.description)

        val success = db.insert(TBL_CITIES, null, contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllCities(): ArrayList<CityModel> {
        val ctyList: ArrayList<CityModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_CITIES"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var city: String
        var country: String
        var description: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                city = cursor.getString(cursor.getColumnIndex("city"))
                country = cursor.getString(cursor.getColumnIndex("country"))
                description = cursor.getString(cursor.getColumnIndex("description"))

                val cty =
                    CityModel(id = id, city = city, country = country, description = description)
                ctyList.add(cty)
            } while (cursor.moveToNext())
        }

        return ctyList
    }

    fun updateCity(cty: CityModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, cty.id)
        contentValues.put(CITY, cty.city)
        contentValues.put(COUNTRY, cty.country)
        contentValues.put(DESCRIPTION, cty.description)

        val success = db.update(TBL_CITIES, contentValues, "id=" + cty.id, null)
        db.close()
        return success
    }

    fun deleteCityById(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID,id)

        val success=db.delete(TBL_CITIES,"id=$id",null)
        db.close()
        return success


    }

}