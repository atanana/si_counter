package com.atanana.sicounter.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.RadioGroup
import com.atanana.sicounter.R
import com.atanana.sicounter.exceptions.SiCounterException
import com.google.common.collect.ImmutableBiMap

open class PriceSelector(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val BUTTONS_TO_VALUES: ImmutableBiMap<Int, Int> = ImmutableBiMap.of(
            R.id.price_10, 10,
            R.id.price_20, 20,
            R.id.price_30, 30,
            R.id.price_40, 40,
            R.id.price_50, 50
    )

    private val selector: RadioGroup by lazy { findViewById(R.id.price_radio_selector) as RadioGroup }

    init {
        LayoutInflater.from(context).inflate(R.layout.price_selector, this)
    }

    open var price: Int
        get() = BUTTONS_TO_VALUES[selector.checkedRadioButtonId] ?: throw SiCounterException("Incorrect button selected!")
        set(price: Int) = selector.check(BUTTONS_TO_VALUES.inverse()[price] ?: throw SiCounterException("Incorrect price selected"))
}