package edu.bk.ndtho.currencyconverter.models

import android.content.ContentValues.TAG
import android.util.Log
import com.beust.klaxon.Klaxon
import com.beust.klaxon.json
import java.io.Serializable

data class CurrencyData(var timestamp: Int, val currencies: List<Currency>) : Serializable
{

    companion object
    {

        fun parse(data: String): CurrencyData
        {
            return Klaxon().parse<CurrencyData>(data) ?: CurrencyData(0, emptyList())
        }
    }

    val currencyInfoList: List<String> = currencies.map { "${it.code} - ${it.name}" }

    fun outdated() = ((System.currentTimeMillis() / 1000 - timestamp) / 86400)

    fun toJsonString(): String
    {
        val data = json {
            obj("timestamp" to timestamp,
                "currencies" to array(currencies.map {
                    obj("code" to it.code,
                        "name" to it.name,
                        "symbol" to it.symbol,
                        "rate" to it.rate)
                }))
        }.toJsonString()
        Log.d(TAG, "toJsonString: ${data}")
        return data
    }
}
