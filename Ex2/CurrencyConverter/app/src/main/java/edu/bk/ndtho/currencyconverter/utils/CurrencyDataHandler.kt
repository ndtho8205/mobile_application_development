package edu.bk.ndtho.currencyconverter.utils

import android.content.Context
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import edu.bk.ndtho.currencyconverter.models.CurrencyData

object CurrencyDataHandler
{

    const val URL =
            "https://openexchangerates.org/api/latest.json?app_id=d898a3d9a74143269881a75ec3bf0c7e"

    fun loadFromJson(context: Context, resourceId: Int): CurrencyData
    {
        val inputStream = context.resources.openRawResource(resourceId)
        return Klaxon().parse<CurrencyData>(inputStream) ?: CurrencyData(0, emptyList())
    }

    fun saveToFile(context: Context, currencyData: CurrencyData, resourceId: Int)
    {

        val data = json {
            obj("timestamp" to currencyData.timestamp.toString(),
                "currencies" to array(currencyData.currencies.map {
                    obj("code" to it.code,
                        "symbol" to it.symbol,
                        "name" to it.name,
                        "rate" to it.rate)
                }))
        }
        print(data.toJsonString())
    }

    fun sync(currencyData: CurrencyData)
    {
        val timeout = 5000
        val readTimeout = 60000

        URL.httpGet().timeout(timeout).timeoutRead(readTimeout)
                .responseJson { request, response, result ->
                    val (json, error) = result
                    if (error != null && json != null)
                    {
                        val jsonObject = json.obj()
                        currencyData.timestamp = jsonObject["timestamp"] as Long
                        val rates = jsonObject["rates"] as JsonObject
                        currencyData.currencies.forEach { it.rate = rates[it.code] as Double }
                    }
                }
        print(currencyData)
    }
}
