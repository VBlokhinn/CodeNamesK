package com.example.codenamesk

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TYPE_NEUTRAL: Int = 0
        private const val TYPE_RED: Int = 1
        private const val TYPE_BLUE: Int = 2
        private const val TYPE_BLACK: Int = 3

    }

    private val cards = ArrayList<Card>()
    private val imageViewRed = arrayListOf<ImageView>()
    private val imageViewBlue = arrayListOf<ImageView>()
    private val imageViewBlack = arrayListOf<ImageView>()
    private val imageViewNeutral = arrayListOf<ImageView>()

    private val imageViewRoleNeutral = arrayListOf<ImageView>()
    private val imageViewRoleRed = arrayListOf<ImageView>()
    private val imageViewRoleBlue = arrayListOf<ImageView>()
    private val imageViewRoleBlack = arrayListOf<ImageView>()

    private var areRed: Boolean = false


    fun generateCards(context: Context): LiveData<ArrayList<Card>> {
        val usedIds = ArrayList<Int>()
        var resourceId: Int
        for (i in 0..21) {
            do {
                with(Random.nextInt(280)) {
                    resourceId = context.resources.getIdentifier(
                        "card_$this",
                        "drawable",
                        context.packageName
                    )
                }
            } while (!usedIds.add(resourceId))
            when (i) {
                0, 1, 2, 3, 4 -> {
                    val cardNeutral = CardNeutral(resourceId)
                    cards.add(cardNeutral)
                }

                5 -> {
                    val cardBlack = CardBlack(resourceId)
                    cards.add(cardBlack)
                }

                6, 7, 8, 10, 11, 12, 13 -> {
                    val cardRed = CardRed(resourceId)
                    cards.add(cardRed)
                }

                14 -> {
                    if (Random.nextBoolean()) {
                        val cardRedPlus = CardRed(resourceId)
                        cards.add(cardRedPlus)
                        areRed = true
                    } else {
                        val cardBluePlus = CardBlue(resourceId)
                        cards.add(cardBluePlus)
                    }
                }

                15, 16, 17, 18, 19, 20, 21 -> {
                    val cardBlue = CardBlue(resourceId)
                    cards.add(cardBlue)
                }
            }
        }
        cards.shuffle()
        Log.d("MainViewModel", "generateCards: $cards")
        val liveData = MutableLiveData<ArrayList<Card>>()
        liveData.value = cards
        return liveData
    }

    private val _areRed = MutableLiveData<Boolean>()

    fun getAreRed(): LiveData<Boolean> {
        _areRed.value = areRed
        return _areRed
    }

}