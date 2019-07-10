package db1.article02.dates

import java.time.DayOfWeek
import java.time.LocalDate

class WorkDayService(private val holidayService: HolidayService) {
    fun nextForInclusive(date: LocalDate): LocalDate {
        return if (isValid(date)) { date } else { nextForInclusive(date.plusDays(1)) }
    }

    fun isValid(date: LocalDate): Boolean {
        return date.dayOfWeek < DayOfWeek.SATURDAY && !holidayService.existFor(date)
    }
}

