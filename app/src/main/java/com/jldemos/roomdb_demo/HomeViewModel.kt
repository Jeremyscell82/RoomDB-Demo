package com.jldemos.roomdb_demo

import androidx.lifecycle.ViewModel
import com.jldemos.roomdb_demo.Utils.MyAppDatabase
import com.jldemos.roomdb_demo.Utils.SuggestionModel
import io.reactivex.Flowable

class HomeViewModel : ViewModel() {


    var displayedSuggestion: SuggestionModel? = null

    //Get the list of suggestions from the database
    fun getMySuggestions(appDatabase: MyAppDatabase): Flowable<List<SuggestionModel>>{
        return appDatabase.SuggestionDao().getAll()
    }



}