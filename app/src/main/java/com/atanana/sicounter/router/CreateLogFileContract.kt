package com.atanana.sicounter.router

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument

class CreateLogFileContract : CreateDocument("text/plain") {

    override fun createIntent(context: Context, input: String): Intent =
        super.createIntent(context, input).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
        }
}
