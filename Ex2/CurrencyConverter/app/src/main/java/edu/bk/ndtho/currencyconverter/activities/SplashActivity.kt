package edu.bk.ndtho.currencyconverter.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import edu.bk.ndtho.currencyconverter.R
import edu.bk.ndtho.currencyconverter.utils.CurrencyDataHandler

class SplashActivity : AppCompatActivity()
{

    val TAG = SplashActivity::class.java.simpleName

    private val mCurrencyDataHandler = CurrencyDataHandler(this)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Intent.EXTRA_TEXT, loadData())
        startActivity(intent)
        finish()
    }

    private fun loadData(): String
    {
        return if (mCurrencyDataHandler.checkFileExists(MainActivity.FILENAME))
            mCurrencyDataHandler.readFromFile(MainActivity.FILENAME)
        else mCurrencyDataHandler.readFromRawResource(R.raw.currencies)
    }
}
