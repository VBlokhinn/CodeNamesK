package com.example.codenamesk

import android.graphics.drawable.Drawable

abstract class Card(open val resourceId: Int, open val type: Int)

data class CardNeutral(override val resourceId: Int, override val type: Int = 0) : Card(resourceId, type)

data class CardRed(override val resourceId: Int, override val type: Int = 1) : Card(resourceId, type)

data class CardBlue(override val resourceId: Int, override val type: Int = 2) : Card(resourceId, type)

data class CardBlack(override val resourceId: Int, override val type: Int = 3) : Card(resourceId, type)

