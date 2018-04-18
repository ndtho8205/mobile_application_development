package edu.bk.ndtho.currencyconverter.utils

import android.content.Context
import android.util.Log
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import edu.bk.ndtho.currencyconverter.models.CurrencyData
import org.json.JSONObject
import java.io.File

class CurrencyDataHandler(private val context: Context)
{

    val TAG = CurrencyDataHandler::class.java.simpleName

    val URL =
            "https://openexchangerates.org/api/latest.json?app_id=d898a3d9a74143269881a75ec3bf0c7e"

    fun readFromRawResource(resourceId: Int): String
    {
        val inputStream = context.resources.openRawResource(resourceId)
        return inputStream.bufferedReader().use { it.readText() }
    }

    fun checkFileExists(filename: String): Boolean
    {
        val fileCheck = File(context.filesDir, filename)
        return fileCheck.exists()
    }

    fun saveToFile(filename: String, contents: String)
    {
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(contents.toByteArray())
        }
    }

    fun readFromFile(filename: String): String
    {
        val file = context.openFileInput(filename)
        return file.bufferedReader().use { it.readText() }
    }

    fun sync(currencyData: CurrencyData, onSyncFinish: (Boolean) -> Unit)
    {
        val timeout = 5000
        val readTimeout = 60000

        URL.httpGet().timeout(timeout).timeoutRead(readTimeout)
                .responseJson { _, _, result ->
                    val (json, error) = result

                    if (error == null && json != null)
                    {
                        val jsonObject = json.obj()
                        Log.d(TAG, "Response: $jsonObject")

                        currencyData.timestamp = jsonObject["timestamp"] as Int
                        val rates = jsonObject["rates"] as JSONObject
                        currencyData.currencies.forEach {
                            val rate: Double =
                                    rates[it.code] as? Double ?: 1.0 * rates[it.code] as Int
                            it.rate = rate
                        }
                        Log.d(TAG, "After updated: $currencyData")
                        onSyncFinish(true)
                    } else
                    {
                        Log.e(TAG, error.toString())
                        onSyncFinish(false)
                    }
                }
    }
}
