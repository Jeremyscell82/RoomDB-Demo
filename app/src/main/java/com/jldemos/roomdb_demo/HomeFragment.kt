package com.jldemos.roomdb_demo

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.squareup.seismic.ShakeDetector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import timber.log.Timber

class HomeFragment: Fragment() {

    private var connection: Disposable? = null
    private val pullAnotherService by lazy {
        BoredApiService.ApiService.create()
    }
    lateinit var homeViewModel: HomeViewModel
    lateinit var shakeDetector: ShakeDetector
    lateinit var sensorManager: SensorManager
    var loading: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        sensorManager = (activity as MainActivity).getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector(ShakeDetector.Listener {
            Timber.d("JL_ shake detector has detected a shake.....")
            if (!loading)pullAnotherSuggestion()
        })
        shakeDetector.setSensitivity(ShakeDetector.SENSITIVITY_LIGHT)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(homeViewModel.suggestionModel == null){
            pullAnotherSuggestion()
        } else {
            Timber.d("JL_ populating UI")
            populateUI()
        }
        view.apply {
            home_save_button.setOnClickListener {
                Toast.makeText(this.context, "Not so fast, im still working on this.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun pullAnotherSuggestion() {
        home_loading_screen.visibility = View.VISIBLE
        loading = true
        connection = pullAnotherService.pullAnotherSuggestion()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Timber.d("JL_ Success, ${result.description} was suggested")
                    homeViewModel.populateSuggestion(result)
                    Timber.d("JL_ model has been converted... ${homeViewModel.suggestionModel?.suggestion}")
                    populateUI()
                },
                { error ->
                    Timber.d("JL_ Error... something has gone wrong $error")
                }
            )
    }

    private fun populateUI(){
        val model = homeViewModel.suggestionModel!!
        home_type_text.text = homeViewModel.convertTypeToString(model.type).capitalize()
        home_suggestion_text.text = model.suggestion
        home_participants_text.text = resources.getString(R.string.participants, model.participants)
//        home_price_text.text = resources.getString(R.string.price, model.price)
        Handler().postDelayed({
            loading = false
            home_loading_screen.visibility = View.GONE
        }, 1500)
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start(sensorManager)
    }

    override fun onPause() {
        super.onPause()
        connection?.dispose()
        shakeDetector.stop()
    }
}