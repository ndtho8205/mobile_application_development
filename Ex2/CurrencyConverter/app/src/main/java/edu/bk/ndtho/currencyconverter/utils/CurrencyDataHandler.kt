package edu.bk.ndtho.currencyconverter.utils

import android.content.Context
import com.beust.klaxon.Klaxon
import edu.bk.ndtho.currencyconverter.models.CurrencyData

class CurrencyDataHandler
{

    fun loadFromJson(context: Context, resourceId: Int): CurrencyData
    {
        val inputStream = context.resources.openRawResource(resourceId)
        return Klaxon().parse<CurrencyData>(inputStream) ?: CurrencyData(0, emptyList())
    }
}
