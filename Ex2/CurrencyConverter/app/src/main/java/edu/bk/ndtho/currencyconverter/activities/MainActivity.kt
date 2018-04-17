package edu.bk.ndtho.currencyconverter.activities

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import edu.bk.ndtho.currencyconverter.R
import edu.bk.ndtho.currencyconverter.models.CurrencyData
import edu.bk.ndtho.currencyconverter.utils.CurrencyDataHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{

    private val mCurrencyDataHandler = CurrencyDataHandler()
    private lateinit var mCurrencyData: CurrencyData

    private var mBaseCurrencyIndex = 0
    private var mQuoteCurrencyIndex = 0

    override
    fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCurrencyData = mCurrencyDataHandler.loadFromJson(this, R.raw.currencies)

        btnBaseCurrencySelector.showCurrencySelectionDialog {
            mBaseCurrencyIndex = it
            btnBaseCurrencySelector.text = mCurrencyData.currencies[it].name
        }

        btnQuoteCurrencySelector.showCurrencySelectionDialog {
            mQuoteCurrencyIndex = it
            btnQuoteCurrencySelector.text = mCurrencyData.currencies[it].name
        }

        etBaseCurrencyAmount.onMoneyAmountChanged(etQuoteCurrencyAmount)
        etQuoteCurrencyAmount.onMoneyAmountChanged(etBaseCurrencyAmount)

    }

    private fun Button.showCurrencySelectionDialog(onItemSelected: (Int) -> Unit)
    {
        this.setOnClickListener {
            val currencyNameList: Array<String> = mCurrencyData.getCurrencyInfoList().toTypedArray()
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Select currency")
            builder.setItems(currencyNameList, { _, which -> onItemSelected(which) })
            builder.show()
        }
    }

    private fun EditText.onMoneyAmountChanged(toCurrencyConverterEditText: EditText)
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
                                              val rate =
                                                      mCurrencyData.currencies[mQuoteCurrencyIndex].rate / mCurrencyData.currencies[mBaseCurrencyIndex].rate
                                              val quoteAmount = rate * (baseAmount ?: 0.0)
                                              toCurrencyConverterEditText.setText(
                                                      quoteAmount.toString(),
                                                      TextView.BufferType.EDITABLE)
                                          }
                                      }
                                  })
    }

}
