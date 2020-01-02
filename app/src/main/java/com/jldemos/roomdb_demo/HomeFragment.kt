package com.jldemos.roomdb_demo

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jldemos.roomdb_demo.Utils.SavedSuggestionsAdapter
import com.jldemos.roomdb_demo.Utils.SuggestionApiService
import com.jldemos.roomdb_demo.Utils.SuggestionModel
import com.squareup.seismic.ShakeDetector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import timber.log.Timber

class HomeFragment : Fragment() {

    //Used to pull down the 'suggestion' from the api endpoint
    private var connection: Disposable? = null
    private val pullAnotherService by lazy {
        SuggestionApiService.ApiService.create()
    }

    //Set up for the suggestions viewmodel and adapter
    private var disposableSuggestions: Disposable? = null
    lateinit var homeViewModel: HomeViewModel //ViewModel is in charge of creating and keeping its own data
    lateinit var suggestionsAdapter: SavedSuggestionsAdapter

    lateinit var shakeDetector: ShakeDetector
    lateinit var sensorManager: SensorManager

    var loading: Boolean = false //Patch to prevent multiple api calls accidentally


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        suggestionsAdapter =
            SavedSuggestionsAdapter()

        //Code for the shake sensor, added for not reason other than curiosity
        sensorManager =
            (activity as MainActivity).getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector(ShakeDetector.Listener {
            if (!loading) pullAnotherSuggestion()
        })
        shakeDetector.setSensitivity(ShakeDetector.SENSITIVITY_LIGHT)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null || homeViewModel.displayedSuggestion == null) { //If no suggestion is in viewmodel, pull another suggestion
            pullAnotherSuggestion()
        } else {
            displaySuggestion()
        }


        view.apply {
            home_save_button.setOnClickListener {
                (activity as MainActivity).saveEntry(homeViewModel.displayedSuggestion!!)
            }
            home_refresh_fab.setOnClickListener {
                pullAnotherSuggestion()
            }

            home_saved_suggestion_recyclerview.apply {
                layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
                adapter = suggestionsAdapter
            }
        }

        //Setup the viewmodel to display its flowable list directly on the screen
        disposableSuggestions = homeViewModel.getMySuggestions((activity as MainActivity).myDB)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Timber.d("New entries have been added, reload the list with ${result.size} items")
                    suggestionsAdapter.updateAdapter(result)
                },
                { error ->
                    Timber.d("Something has gone wrong $error")
                }
            )
    }

    private fun pullAnotherSuggestion() {
        home_fab_loading_indicator.visibility = View.VISIBLE
        loading = true
        connection = pullAnotherService.pullAnotherSuggestion()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Timber.d("JL_ Success, ${result.description} was suggested")
                    homeViewModel.displayedSuggestion = SuggestionModel(
                        dbKey = 0,
                        suggestion = result.description,
                        accessibility = result.accessibility,
                        type = result.type,
                        participants = result.participants,
                        link = result.link,
                        apiKey = result.key
                    )
                    displaySuggestion()
                },
                { error ->
                    Timber.d("JL_ Error... something has gone wrong $error")
                }
            )
    }

    private fun displaySuggestion() {
        val model = homeViewModel.displayedSuggestion!!
        home_type_text.text = model.type.capitalize()
        home_suggestion_text.text = model.suggestion
        home_participants_text.text = resources.getString(R.string.participants, model.participants)
        Handler().postDelayed({
            loading = false
            home_fab_loading_indicator.visibility = View.GONE
        }, 500)
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start(sensorManager)
    }

    //To dispose of the disposable, we could use onStop, onPause, onDestroy etc...
    override fun onPause() {
        super.onPause()
        disposableSuggestions?.dispose()
        connection?.dispose()
        shakeDetector.stop()
    }
}