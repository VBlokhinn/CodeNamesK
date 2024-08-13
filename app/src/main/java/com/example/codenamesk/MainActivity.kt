package com.example.codenamesk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.codenamesk.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private lateinit var gridLayoutCard: GridLayout
    private lateinit var gridLayoutRoles: GridLayout
    private lateinit var scrollViewRoles: ScrollView
    private lateinit var imageViewShow: ImageView
    private lateinit var imageViewNewGame: ImageView
    private lateinit var main: ConstraintLayout

    private val imageViewRed = ArrayList<ImageView>()
    private val imageViewBlue = ArrayList<ImageView>()
    private val imageViewBlack = ArrayList<ImageView>()
    private val imageViewNeutral = ArrayList<ImageView>()

    private val imageViewRoleNeutral = java.util.ArrayList<ImageView>()
    private val imageViewRoleRed = java.util.ArrayList<ImageView>()
    private val imageViewRoleBlue = java.util.ArrayList<ImageView>()
    private val imageViewRoleBlack = java.util.ArrayList<ImageView>()

    private val TYPE_NEUTRAL: Int = 0
    private val TYPE_RED: Int = 1
    private val TYPE_BLUE: Int = 2
    private val TYPE_BLACK: Int = 3

    private val cfRed: Int = -0x7f340000
    private val cfBlue: Int = -0x7fff6634
    private val cfOrange: Int = -0x7f007800
    private val cfBlack: Int = -0x70000000

    private var areRed: Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        setupViewModel()
        setupListeners()
        observeViewModel()
    }

    private fun setupViews() {
        gridLayoutCard = findViewById(R.id.gridLayoutCard)
        gridLayoutRoles = findViewById(R.id.gridLayoutRoles)
        scrollViewRoles = findViewById(R.id.scrollViewRoles)
        imageViewShow = findViewById(R.id.imageViewShow)
        imageViewNewGame = findViewById(R.id.imageViewNewGame)
        main = findViewById(R.id.main)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getAreRed().observe(this, Observer { areRed = it })
        setAreRed()
    }

    private fun setupListeners() {
        imageViewNewGame.setOnClickListener { reload() }

        imageViewShow.setOnLongClickListener {
            showRoles()
            false
        }

        imageViewShow.setOnClickListener {
            hideRoles()
        }
    }

    private fun observeViewModel() {
        viewModel.generateCards(this).observe(this, Observer {
            displayCards(it)
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setMainBackgroundColor(isRedTurn: Boolean) {
        main.setBackgroundColor(if (isRedTurn) 0x30cc0000 else 0x300099cc)
    }

    private fun showRoles() {
        gridLayoutRoles.visibility = View.VISIBLE
        scrollViewRoles.visibility = View.VISIBLE
        imageViewShow.setImageResource(R.drawable.eye_angry)
        imageViewNewGame.visibility = View.VISIBLE

        // Setting role images
        setRoleImages()
    }

    private fun setRoleImages() {
        setRoleImage(imageViewRoleRed, R.drawable.red_agent_on_a_red_background, R.drawable.red_agent_woman_on_a_red_background)
        setRoleImage(imageViewRoleBlue, R.drawable.blue_agent_man_on_a_blue_background, R.drawable.blue_agent_woman_on_a_blue_background)
        setRoleImage(imageViewRoleBlack, R.drawable.black_agent)
        setRoleImage(imageViewRoleNeutral, R.drawable.neutral_man, R.drawable.neutral_woman)
    }

    private fun setRoleImage(imageViews: List<ImageView>, vararg imageResources: Int) {
        imageViews.forEach { imageView ->
            val resourceId = imageResources.random()
            imageView.setImageDrawable(ResourcesCompat.getDrawable(resources, resourceId, null))
        }
    }

    private fun hideRoles() {
        imageViewShow.setImageResource(R.drawable.eye)
        imageViewNewGame.visibility = View.INVISIBLE
        gridLayoutRoles.visibility = View.GONE
        scrollViewRoles.visibility = View.GONE
    }

    private fun displayCards(cards: List<Card>) {
        for (card in cards) {
            val view = layoutInflater.inflate(R.layout.card_item, gridLayoutCard, false)
            val imageView = view.findViewById<ImageView>(R.id.imageViewCard)
            imageView.setImageResource(card.resourceId)
            gridLayoutCard.addView(view)

            val viewRole = layoutInflater.inflate(R.layout.role_item, gridLayoutRoles, false)
            val imageViewRole = viewRole.findViewById<ImageView>(R.id.imageViewRole)
            gridLayoutRoles.addView(viewRole)

            when (card.type) {
                TYPE_RED -> {
                    imageViewRed.add(imageView)
                    imageViewRoleRed.add(imageViewRole)
                }
                TYPE_BLUE -> {
                    imageViewBlue.add(imageView)
                    imageViewRoleBlue.add(imageViewRole)
                }
                TYPE_BLACK -> {
                    imageViewBlack.add(imageView)
                    imageViewRoleBlack.add(imageViewRole)
                }
                TYPE_NEUTRAL -> {
                    imageViewNeutral.add(imageView)
                    imageViewRoleNeutral.add(imageViewRole)
                }
            }

            setupCardClickListener(card, imageView)
        }
    }

    private fun setupCardClickListener(card: Card, imageView: ImageView) {
        imageView.setOnClickListener {
            handleCardClick(card, imageView)
        }
    }

    private fun handleCardClick(card: Card, imageView: ImageView) {

        when (card.type) {
            TYPE_RED -> {
                imageView.setColorFilter(cfRed)
                if (areRed == true) {
                    showToast("Правильно")
                    areRed = true
                } else {
                    showToast("Неправильно, передавай ход")
                    areRed = true
                    setAreRed()
                }
            }
            TYPE_BLUE -> {
                imageView.setColorFilter(cfBlue)
                if (areRed == false) {
                    showToast("Правильно")
                    areRed = false
                } else {
                    showToast("Неправильно, передавай ход")
                    areRed = false
                    setAreRed()
                }
            }
            TYPE_BLACK -> {
                imageView.setColorFilter(cfBlack)
                showToast("Ты проиграл")
            }
            TYPE_NEUTRAL -> {
                imageView.setColorFilter(cfOrange)
                areRed == false
                showToast("Передавай ход")
                setAreRed()
            }
        }
    }
    private fun reload() {
        val intent = Intent(
            this,
            MainActivity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun setAreRed() {
        if (areRed == true) {
            Toast.makeText(this@MainActivity, "Ходят красные", Toast.LENGTH_SHORT).show()
            ConstraintLayout(this).setBackgroundColor(0x30cc0000)
        } else {
            Toast.makeText(this@MainActivity, "Ходят синие", Toast.LENGTH_SHORT).show()
            ConstraintLayout(this).setBackgroundColor(0x300099cc)
        }
    }
}
