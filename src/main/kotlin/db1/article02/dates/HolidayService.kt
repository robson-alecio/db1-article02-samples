package db1.article02.dates

import java.time.LocalDate
import java.time.Month

class HolidayService {
    private val holidaysFixed = listOf(
        1 to Month.JANUARY,
        7 to Month.SEPTEMBER,
        25 to Month.DECEMBER
    )

    fun existFor(date: LocalDate): Boolean {
        return holidaysFixed.find { it.first == date.dayOfMonth && it.second == date.month } != null
    }

}
