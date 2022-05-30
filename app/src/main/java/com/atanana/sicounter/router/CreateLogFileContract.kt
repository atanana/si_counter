package com.atanana.sicounter.router

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

class CreateLogFileContract : ActivityResultContracts.CreateDocument() {

    override fun createIntent(context: Context, input: String): Intent =
        super.createIntent(context, input).apply {
            type = "text/plain"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
}