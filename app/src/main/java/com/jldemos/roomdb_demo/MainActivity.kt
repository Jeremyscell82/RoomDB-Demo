package com.jldemos.roomdb_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.jldemos.roomdb_demo.Utils.MyAppDatabase
import com.jldemos.roomdb_demo.Utils.SuggestionModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var myDB: MyAppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myDB = Room.databaseBuilder(
            applicationContext,
            MyAppDatabase::class.java,
            "suggestion_list.db"
        ).build()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_container,
                    HomeFragment()
                )
                .commit()
        }
    }

    //For simplicity reasons all DB calls are below
    fun saveEntry(suggestionModel: SuggestionModel){
        GlobalScope.launch {
            myDB.SuggestionDao().insertSuggestion(suggestionModel)
        }
    }

    fun updateEntry(suggestionModel: SuggestionModel){
        GlobalScope.launch {
            myDB.SuggestionDao().updateSuggestion(suggestionModel)
        }
    }

    fun deleteEntry(suggestionModel: SuggestionModel){
        GlobalScope.launch {
            myDB.SuggestionDao().deleteSuggestion(suggestionModel)
        }
    }



}
