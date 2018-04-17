package edu.bk.ndtho.currencyconverter.models

data class CurrencyData(val timestamp: Int, val currencies: List<Currency>)
{
    val currencyInfoList: List<String> = currencies.map { "${it.code} - ${it.name}" }
}
