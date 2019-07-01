package com.atanana.sicounter.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.RadioGroup
import com.atanana.sicounter.R
import com.atanana.sicounter.exceptions.SiCounterException

class PriceSelector(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val selector: RadioGroup by lazy { findViewById<RadioGroup>(R.id.price_radio_selector) }

    init {
        inflate(context, R.layout.price_selector, this)
    }

    var price: Int
        get() = when (selector.checkedRadioButtonId) {
            R.id.price_10 -> 10
            R.id.price_20 -> 20
            R.id.price_30 -> 30
            R.id.price_40 -> 40
            R.id.price_50 -> 50
            else -> throw SiCounterException("Incorrect button selected!")
        }
        set(price) {
            val id = when (price) {
                10 -> R.id.price_10
                20 -> R.id.price_20
                30 -> R.id.price_30
                40 -> R.id.price_40
                50 -> R.id.price_50
                else -> throw SiCounterException("Incorrect price selected")
            }
            selector.check(id)
        }
}