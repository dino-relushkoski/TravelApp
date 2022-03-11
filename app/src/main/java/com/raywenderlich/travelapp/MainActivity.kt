package com.raywenderlich.travelapp

import android.app.backup.BackupAgentHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var edCity: EditText
    private lateinit var edCountry: EditText
    private lateinit var edDescription: EditText

    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button


    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: CityAdapter? = null
    private var cty: CityModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        sqliteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener { addCity() }
        btnView.setOnClickListener { getCities() }
        btnUpdate.setOnClickListener { updateCity() }
        adapter?.setOnClickItem {
            Toast.makeText(this, it.city, Toast.LENGTH_SHORT).show()
            edCity.setText(it.city)
            edCountry.setText(it.country)
            edDescription.setText(it.description)
            cty = it
        }

        adapter?.setOnClickDeleteItem {
            deleteCity(it.id)

        }


    }

    private fun getCities() {
        val ctyList = sqliteHelper.getAllCities()
        Log.e("pppp", "${ctyList.size}")

        adapter?.addItems(ctyList)

    }


    private fun addCity() {
        val city = edCity.text.toString()
        val country = edCountry.text.toString()
        val description = edDescription.text.toString()

        if (city.isEmpty() || country.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please enter required field ", Toast.LENGTH_SHORT).show()
        } else {
            val cty = CityModel(city = city, country = country, description = description)
            val status = sqliteHelper.insertCity(cty)
            //Check insert success or not success
            if (status > -1) {
                Toast.makeText(this, "City added...", Toast.LENGTH_SHORT).show()
                clearEditText()
                getCities()
            } else {
                Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateCity() {
        val city = edCity.text.toString()
        val country = edCountry.text.toString()
        val description = edDescription.text.toString()

        if (city == cty?.city && country == cty?.country && description == cty?.description) {
            Toast.makeText(this, "Record not changed...", Toast.LENGTH_SHORT).show()
            return

        }

        if (cty == null) return

        val cty = CityModel(id = cty!!.id, city = city, country = country, description = description)
        val status = sqliteHelper.updateCity(cty)
        if(status>-1){
            clearEditText()
            getCities()
        }else{
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }


    }

    private fun deleteCity(id:Int){

        val builder=AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete the record?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){dialog, _ ->
            sqliteHelper.deleteCityById(id)
            getCities()

            dialog.dismiss()
        }

        builder.setNegativeButton("No"){dialog, _ ->

            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText() {
        edCity.setText("")
        edCountry.setText("")
        edDescription.setText("")
        edCity.requestFocus()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CityAdapter()
        recyclerView.adapter = adapter

    }

    private fun initView() {
        edCity = findViewById(R.id.edCity)
        edCountry = findViewById(R.id.edCountry)
        edDescription = findViewById(R.id.edDescription)

        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)

        recyclerView = findViewById(R.id.recyclerView)


    }
}