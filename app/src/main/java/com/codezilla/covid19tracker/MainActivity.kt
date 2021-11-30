package com.codezilla.covid19tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.security.KeyStore
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var editFullName: EditText
    private lateinit var editHomeAddress: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var sqlitehelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: EntryAdapter? = null
    private var std: CovidTrackerModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initRecyclerView()
        sqlitehelper = SQLiteHelper(this)
        btnSubmit.setOnClickListener{ addEntry()}
        btnView.setOnClickListener{ getEntries()}
        btnUpdate.setOnClickListener{updateEntry()}

        adapter?.setOnClickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            editFullName.setText(it.name)
            editHomeAddress.setText(it.homeAddress)
            std = it
        }
        adapter?.setonClickDeleteItem {
            //Toast.makeText(this, "Record $it.name has been deleted.", Toast.LENGTH_SHORT).show()
            deleteEntry(it.id)
        }
    }

    private fun deleteEntry(id: Int){
        if (id == null) return
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete entry?")
        builder.setCancelable(true)

        builder.setPositiveButton("Yes"){
            dialog,_ ->
            val success = sqlitehelper.deleteEntryById(id)
            if(success > -1){
                Toast.makeText(this, "Record has been deleted.", Toast.LENGTH_SHORT).show()
                getEntries()
            }

            dialog.dismiss()
        }
        builder.setNegativeButton("No"){
                dialog,_ ->
            dialog.dismiss()

        }
        val alert = builder.create()
        alert.show()
    }

    private fun updateEntry() {
        val name = editFullName.text.toString()
        val homeaddress = editHomeAddress.text.toString()

        if(name == std?.name && homeaddress == std?.homeAddress){
            Toast.makeText(this, "Record is not changed.", Toast.LENGTH_SHORT).show()
            return

        }
        if(std == null) return
        val std = CovidTrackerModel(id = std!!.id, name = name, homeAddress = homeaddress)
        val status = sqlitehelper.updateStudent(std)
        if(status > -1){
            clearEditText()
            getEntries()
        }else{
            Toast.makeText(this, "failed to update information.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getEntries() {
        val stdList = sqlitehelper.getAllEntries()
        Log.e("pppp", "${stdList.size}")

        adapter?.addItems(stdList)
    }

    private fun addEntry() {
        val name = editFullName.text.toString()
        val homeAddress = editHomeAddress.text.toString()

        if(name.isEmpty() || homeAddress.isEmpty()){
            Toast.makeText(this, "Please enter required field", Toast.LENGTH_SHORT).show()
        }
        else{
            val std = CovidTrackerModel(name = name, homeAddress = homeAddress)
            val status = sqlitehelper.insertEntry(std)
            //check if insertion is success
            if (status > -1){
                Toast.makeText(this, "You may now enter the facility", Toast.LENGTH_SHORT).show()
                clearEditText()
                getEntries()
            }
            else{
                Toast.makeText(this, "Entry record not saved.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearEditText() {
        editFullName.setText("")
        editHomeAddress.setText("")
        editFullName.requestFocus()
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EntryAdapter()
        recyclerView.adapter = adapter

    }

    private fun initView(){
        editFullName = findViewById(R.id.editFullName)
        editHomeAddress = findViewById(R.id.editHomeAddress)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnView = findViewById(R.id.btnView)
        btnUpdate= findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }
}