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
import edu.bk.ndtho.currencyconverter.models.Currency
import edu.bk.ndtho.currencyconverter.models.CurrencyData
import edu.bk.ndtho.currencyconverter.utils.CurrencyDataHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mCurrencyDataHandler = CurrencyDataHandler()
    private lateinit var mCurrencyData: CurrencyData

    private lateinit var mBaseCurrency: Currency
    private lateinit var mQuoteCurrency: Currency

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCurrencyData = mCurrencyDataHandler.loadFromJson(this, R.raw.currencies)
        mBaseCurrency = mCurrencyData.currencies[0]
        mQuoteCurrency = mCurrencyData.currencies[1]

        initViews()
    }

    private fun initViews() {
        updateViewsWhenCurrencyChanged()
        initBaseCurrency()
        initQuoteCurrency()
    }

    private fun initBaseCurrency() {
        btnBaseCurrencySelector.showCurrencySelectionDialog {
            mBaseCurrency = mCurrencyData.currencies[it]
            updateViewsWhenCurrencyChanged()
            clearAmount()
        }

        etBaseCurrencyAmount.onMoneyAmountChanged {
            etQuoteCurrencyAmount.updateMoneyAmount(it, mBaseCurrency, mQuoteCurrency)
        }
    }


    private fun initQuoteCurrency() {
        btnQuoteCurrencySelector.showCurrencySelectionDialog {
            mQuoteCurrency = mCurrencyData.currencies[it]
            updateViewsWhenCurrencyChanged()
            clearAmount()
        }

        etQuoteCurrencyAmount.onMoneyAmountChanged {
            etBaseCurrencyAmount.updateMoneyAmount(it, mQuoteCurrency, mBaseCurrency)
        }
    }

    private fun clearAmount() {
        etBaseCurrencyAmount.setText("")
        etQuoteCurrencyAmount.setText("")
    }

    private fun updateViewsWhenCurrencyChanged() {
        btnBaseCurrencySelector.text = mBaseCurrency.name
        tvBaseCurrencySymbol.text = mBaseCurrency.symbol
        tvBaseCurrencyUnit.updateCurrencyUnitInfo(mBaseCurrency, mQuoteCurrency)
        etBaseCurrencyAmount.setText("")

        btnQuoteCurrencySelector.text = mQuoteCurrency.name
        tvQuoteCurrencySymbol.text = mQuoteCurrency.symbol
        tvQuoteCurrencyUnit.updateCurrencyUnitInfo(mQuoteCurrency, mBaseCurrency)
        etQuoteCurrencyAmount.setText("")
    }

    private fun TextView.updateCurrencyUnitInfo(fromCurrency: Currency,
                                                toCurrency: Currency) {
        this.text = resources.getString(R.string.currencies_unit_info, fromCurrency.code,
                toCurrency.rate / fromCurrency.rate,
                toCurrency.code)
    }

    private fun Button.showCurrencySelectionDialog(onItemSelected: (Int) -> Unit) {
        this.setOnClickListener {
            val currencyNameList: Array<String> = mCurrencyData.currencyInfoList.toTypedArray()
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Select currency")
            builder.setItems(currencyNameList, { _, which -> onItemSelected(which) })
            builder.show()
        }
    }

    private fun EditText.onMoneyAmountChanged(onChanged: (Double) -> Unit) {
        val me = this
        me.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int,
                                       before: Int, count: Int) {
                if (me.hasFocus()) {
                    val baseAmount = s?.toString()?.toDoubleOrNull()
                    onChanged(baseAmount ?: 0.0)
                }
            }
        })
    }

    private fun EditText.updateMoneyAmount(amount: Double, fromCurrency: Currency, toCurrency: Currency) {
        val rate = toCurrency.rate / fromCurrency.rate
        this.setText((rate * amount).toString(), android.widget.TextView.BufferType.EDITABLE)
    }

}
