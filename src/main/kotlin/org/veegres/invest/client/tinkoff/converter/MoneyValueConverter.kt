package org.veegres.invest.client.tinkoff.converter

import ru.tinkoff.piapi.contract.v1.MoneyValue
import java.math.BigDecimal
import java.util.*

fun MoneyValue.toBigDecimal(): BigDecimal {
    if (this.units == 0L && this.nano == 0) return BigDecimal.ZERO;
    return BigDecimal.valueOf(units).add(BigDecimal.valueOf(nano.toLong(), 9));
}

fun MoneyValue.fromBigDecimal(value: BigDecimal, currency: String?): MoneyValue {
    return MoneyValue.newBuilder()
        .setUnits(getUnits(value))
        .setNano(getNano(value))
        .setCurrency(toLowerCaseNullable(currency))
        .build();
}

private fun getUnits(value: BigDecimal): Long {
    return value.toLong()
}

private fun getNano(value: BigDecimal): Int {
    return value.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(1000000000L)).toInt()
}

private fun toLowerCaseNullable(value: String?): String {
    return value?.lowercase(Locale.getDefault()) ?: ""
}