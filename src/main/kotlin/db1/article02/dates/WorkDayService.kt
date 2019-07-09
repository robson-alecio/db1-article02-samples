package db1.article02.dates

import java.time.LocalDate

class WorkDayService(private val holidayService: HolidayService) {
    fun nextFor(date: LocalDate): LocalDate {
        return date
    }

    fun isValid(date: LocalDate): Boolean {
        return true
    }
}

