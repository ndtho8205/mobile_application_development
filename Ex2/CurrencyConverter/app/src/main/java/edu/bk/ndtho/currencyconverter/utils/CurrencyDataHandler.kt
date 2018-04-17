package edu.bk.ndtho.currencyconverter.utils

import android.content.Context
import com.beust.klaxon.Klaxon
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

    fun sync()
    {
        val timeout = 5000
        val readTimeout = 60000

        URL.httpGet().timeout(timeout).timeoutRead(readTimeout)
                .responseJson { request, response, result ->
                    val (json, error) = result
                    print(result.get().obj())
                    if (error == null)
                    {
                        print("=======================================")
                        print(json)
                    }
                }
    }
}
