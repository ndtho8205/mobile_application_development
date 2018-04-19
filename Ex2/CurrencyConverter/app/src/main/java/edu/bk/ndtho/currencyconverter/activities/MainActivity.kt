package edu.bk.ndtho.currencyconverter.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import edu.bk.ndtho.currencyconverter.R
import edu.bk.ndtho.currencyconverter.models.Currency
import edu.bk.ndtho.currencyconverter.models.CurrencyData
import edu.bk.ndtho.currencyconverter.utils.CurrencyDataHandler
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity()
{

    companion object
    {

        val FILENAME = "currencies_rate.json"

    }

    private lateinit var mCurrencyData: CurrencyData

    private var mBaseCurrencyIndex: Int = 0
    private var mQuoteCurrencyIndex: Int = 1
    private lateinit var mBaseCurrency: Currency
    private lateinit var mQuoteCurrency: Currency

    private val mCurrencyDataHandler = CurrencyDataHandler(this)

    override
    fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent?.let {
            val data: String = if (it.hasExtra(Intent.EXTRA_TEXT))
            {
                it.getStringExtra(Intent.EXTRA_TEXT)
            } else
            {
                mCurrencyDataHandler.readFromRawResource(R.raw.currencies)
            }
            mCurrencyData = CurrencyData.parse(data)
        }

        mBaseCurrency = mCurrencyData.currencies[mBaseCurrencyIndex]
        mQuoteCurrency = mCurrencyData.currencies[mQuoteCurrencyIndex]

        requestInternetPermission()
        initViews()
    }

    private fun initViews()
    {
        updateViewsWhenCurrencyChanged()

        initBaseCurrency()
        initQuoteCurrency()
        initSyncInfo()
    }

    private fun initBaseCurrency()
    {
        btnBaseCurrencySelector.setOnClickListener {
            showCurrencySelectionDialog(mBaseCurrencyIndex, {
                mBaseCurrencyIndex = it
                mBaseCurrency = mCurrencyData.currencies[it]
                updateViewsWhenCurrencyChanged()
            })
        }

        etBaseCurrencyAmount.onMoneyAmountChanged {
            etQuoteCurrencyAmount.updateMoneyAmount(it, mBaseCurrency, mQuoteCurrency)
        }
    }

    private fun initQuoteCurrency()
    {
        btnQuoteCurrencySelector.setOnClickListener {
            showCurrencySelectionDialog(mQuoteCurrencyIndex, {
                mQuoteCurrencyIndex = it
                mQuoteCurrency = mCurrencyData.currencies[it]

                updateViewsWhenCurrencyChanged()
            })
        }

        etQuoteCurrencyAmount.onMoneyAmountChanged {
            etBaseCurrencyAmount.updateMoneyAmount(it, mQuoteCurrency, mBaseCurrency)
        }
    }

    private fun initSyncInfo()
    {
        tvSyncInfo.updateSyncInfo()

        tvSyncInfo.setOnClickListener {
            if (hasInternetPermission())
            {
                showMessage("Updating...")
                mCurrencyDataHandler.sync(mCurrencyData) {
                    if (it)
                    {
                        mCurrencyDataHandler.saveToFile(FILENAME, mCurrencyData.toJsonString())

                        updateViewsWhenCurrencyChanged()
                        tvSyncInfo.updateSyncInfo()
                        showMessage("Updated :) Let's play!")
                    } else
                    {
                        showMessage("Some error occurred, please try again :(")
                    }
                }
            } else
                requestInternetPermission()
        }
    }

    private fun TextView.updateCurrencyUnitInfo(fromCurrency: Currency,
                                                toCurrency: Currency)
    {
        this.text = resources.getString(R.string.currencies_unit_info, fromCurrency.code,
                                        toCurrency.rate / fromCurrency.rate,
                                        toCurrency.code)
    }

    private fun EditText.onMoneyAmountChanged(onChanged: (Double) -> Unit)
    {
        val me = this
        me.addTextChangedListener(object : TextWatcher
                                  {
                                      override fun afterTextChanged(s: Editable?)
                                      {
                                      }

                                      override fun beforeTextChanged(s: CharSequence?, start: Int,
                                                                     count: Int, after: Int)
                                      {
                                      }

                                      override fun onTextChanged(s: CharSequence?, start: Int,
                                                                 before: Int, count: Int)
                                      {
                                          if (me.hasFocus())
                                          {
                                              val baseAmount = s?.toString()?.toDoubleOrNull()
                                              onChanged(baseAmount ?: 0.0)
                                          }
                                      }
                                  })
    }

    private fun EditText.updateMoneyAmount(amount: Double, fromCurrency: Currency,
                                           toCurrency: Currency)
    {
        val df = DecimalFormat("#.######")
        val rate = toCurrency.rate / fromCurrency.rate
        this.setText(df.format(rate * amount), android.widget.TextView.BufferType.EDITABLE)
    }

    private fun TextView.updateSyncInfo()
    {
        val messageId =
                if (mCurrencyData.outdated() == 0L) R.string.currencies_sync_info_latest else R.string.currencies_sync_info_outdated
        this.text = getString(messageId, mCurrencyData.outdated())
    }

    private fun updateViewsWhenCurrencyChanged()
    {
        btnBaseCurrencySelector.text = mBaseCurrency.name
        tvBaseCurrencySymbol.text = mBaseCurrency.symbol
        tvBaseCurrencyUnit.updateCurrencyUnitInfo(mBaseCurrency, mQuoteCurrency)
        etBaseCurrencyAmount.setText("")

        btnQuoteCurrencySelector.text = mQuoteCurrency.name
        tvQuoteCurrencySymbol.text = mQuoteCurrency.symbol
        tvQuoteCurrencyUnit.updateCurrencyUnitInfo(mQuoteCurrency, mBaseCurrency)
        etQuoteCurrencyAmount.setText("")

        clearAmount()
    }

    private fun clearAmount()
    {
        etBaseCurrencyAmount.setText("")
        etQuoteCurrencyAmount.setText("")
    }

    private fun showCurrencySelectionDialog(checkedItem: Int, onItemSelected: (Int) -> Unit)
    {
        val currencyNameList: Array<String> = mCurrencyData.currencyInfoList.toTypedArray()
        AlertDialog.Builder(this@MainActivity)
                .setTitle("Select currency")
                .setSingleChoiceItems(currencyNameList, checkedItem, { dialog, which ->
                    onItemSelected(which)
                    dialog.dismiss()
                })
                .show()
    }

    private fun hasInternetPermission(): Boolean = ContextCompat.checkSelfPermission(this,
                                                                                     Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED

    private fun requestInternetPermission()
    {
        ActivityCompat.requestPermissions(this,
                                          arrayOf(Manifest.permission.INTERNET),
                                          101)

    }

    private fun showMessage(message: String)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
