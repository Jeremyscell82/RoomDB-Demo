package com.jldemos.roomdb_demo

import androidx.lifecycle.ViewModel
import java.text.DecimalFormat

class HomeViewModel : ViewModel() {


    var suggestionModel: BoredSuggestion? = null


    data class BoredSuggestion(
        val suggestion: String,
        val accessibility: Double,
        val type: TYPE,
        val participants: Int,
//        val price: String,
        val link: String,
        val key: String
    )

    fun populateSuggestion(result: BoredApiService.BoredResult) {
        suggestionModel = BoredSuggestion(
            suggestion = result.description,
            accessibility = result.accessibility,
            type = convertTypeToEnum(result.type),
            participants = result.participants,
//            price = DecimalFormat().format(result.price),
            link = result.link,
            key = result.key
        )
    }

    fun retreiveSuggestion(): BoredSuggestion? {
        return suggestionModel
    }


    /** HELPER FUNCTIONS **/
    enum class TYPE {
        EDUCATION,
        RECREATIONAL,
        SOCIAL,
        DIY,
        CHARITY,
        COOKING,
        RELAXATION,
        MUSIC,
        BUSYWORK
    }

    fun convertTypeToEnum(type: String): TYPE {
        return when (type) {
            "education" -> {
                TYPE.EDUCATION
            }
            "recreational" -> {
                TYPE.RECREATIONAL
            }
            "social" -> {
                TYPE.SOCIAL
            }
            "diy" -> {
                TYPE.DIY
            }
            "charity" -> {
                TYPE.CHARITY
            }
            "cooking" -> {
                TYPE.COOKING
            }
            "relaxation" -> {
                TYPE.RELAXATION
            }
            "music" -> {
                TYPE.MUSIC
            }
            "busywork" -> {
                TYPE.BUSYWORK
            }
            else -> {
                TYPE.EDUCATION
            }
        }
    }

    fun convertTypeToString(type: TYPE): String {
        return when (type) {
            TYPE.EDUCATION -> {
                "education"
            }
            TYPE.RECREATIONAL -> {
                "recreational"
            }
            TYPE.SOCIAL -> {
                "social"
            }
            TYPE.DIY -> {
                "diy"
            }
            TYPE.CHARITY -> {
                "charity"
            }
            TYPE.COOKING -> {
                "cooking"
            }
            TYPE.RELAXATION -> {
                "relaxation"
            }
            TYPE.MUSIC -> {
                "music"
            }
            TYPE.BUSYWORK -> {
                "busywork"
            }
        }
    }
}