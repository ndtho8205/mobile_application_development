package edu.bk.ndtho.currencyconverter.models

data class CurrencyData(var timestamp: Long, val currencies: List<Currency>)
{

    val currencyInfoList: List<String> = currencies.map { "${it.code} - ${it.name}" }
}
