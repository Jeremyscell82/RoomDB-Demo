package com.jldemos.roomdb_demo.Utils

import androidx.room.*
import com.jldemos.roomdb_demo.Utils.DB_Controller.Companion.dbVersion
import io.reactivex.Flowable

class DB_Controller {
    companion object {
        const val dbVersion = 1
    }
}

//This is the Database Access Object, the 'wrapper' if you will for ROOM DB calls
@Dao
interface SuggestionDao {

    //Get all entries from the DB, The flow feature will auto update the view model with any new data presented to the database
    @Query("SELECT * FROM suggestionmodel")
    fun getAll(): Flowable<List<SuggestionModel>>

    //Get all entries that have the same type
    @Query("SELECT * FROM suggestionmodel WHERE type LIKE :type")
    fun findByType(type: String): SuggestionModel

    //Used to add a new suggestion entry to the database
    @Insert
    fun insertSuggestion(vararg suggestion: SuggestionModel)

    //Used to delete a suggestion from the database
    @Delete
    fun deleteSuggestion(vararg suggestion: SuggestionModel)


    //Used to update an entry that is already in the database
    @Update
    fun updateSuggestion(vararg suggestionModel: SuggestionModel)
}

//This is what creates the database
@Database(entities = [SuggestionModel::class], version = dbVersion, exportSchema = false)
abstract class MyAppDatabase: RoomDatabase() {
    abstract fun SuggestionDao(): SuggestionDao
}