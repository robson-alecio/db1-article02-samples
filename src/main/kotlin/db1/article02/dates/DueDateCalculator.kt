package db1.article02.dates

import java.time.LocalDate
import java.time.Period

class DueDateCalculator(private val workDayService: WorkDayService) {

    fun calculate(closeDate: LocalDate): LocalDate {
        val idealDueDate = closeDate.plusDays(5)
        var dueDate = workDayService.nextForInclusive(idealDueDate)
        val period = Period.between(closeDate, dueDate)
        if (period.days > 7) {
            dueDate = idealDueDate
            do {
                dueDate = dueDate.minusDays(1)
            } while (!workDayService.isValid(dueDate))
        }

        return dueDate
    }
}