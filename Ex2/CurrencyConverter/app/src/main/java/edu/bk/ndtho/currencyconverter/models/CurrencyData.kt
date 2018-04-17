package edu.bk.ndtho.currencyconverter.models

data class CurrencyData(val timestamp: Int, val currencies: List<Currency>)
{

    fun getCurrencyInfoList(): List<String>
    {
        return currencies.map { "${it.code} - ${it.name}" }
    }

}
