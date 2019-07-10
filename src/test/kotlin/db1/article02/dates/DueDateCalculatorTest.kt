package db1.article02.dates

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verifyAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

class DueDateCalculatorTest {

    private var workDayService: WorkDayService = mockkClass(WorkDayService::class)
    private lateinit var dueDateCalculator: DueDateCalculator

    @BeforeEach
    fun setUp() {
        dueDateCalculator = DueDateCalculator(workDayService)
    }

    @Test
    fun `regular due date`() {
        inRangeDueDate(9, 14)
    }

    @Test
    fun `closing on monday due date to next monday`() {
        dueToWeekend(10, 15, 17)
    }

    @Test
    fun `closing on tuesday due date to next monday`() {
        dueToWeekend(11, 16, 17)
    }

    private fun dueToWeekend(closeDay: Int, dayOnWeekend: Int, dueDay: Int) {
        val expected = LocalDate.of(2019, Month.JUNE, dueDay)
        val calculatedDate = LocalDate.of(2019, Month.JUNE, dayOnWeekend)
        every { workDayService.nextForInclusive(calculatedDate) } returns expected

        val closeDate = LocalDate.of(2019, Month.JUNE, closeDay)
        val dueDate = dueDateCalculator.calculate(closeDate)
        assertThat(dueDate).isEqualTo(expected)

        verifyAll {
            workDayService.nextForInclusive(calculatedDate)
        }
        confirmAllVerified()
    }

    @Test
    fun `closing on wednesday due date to next monday`() {
        inRangeDueDate(12, 17)
    }

    @Test
    fun `closing on thursday due date to next monday`() {
        inRangeDueDate(13, 18)
    }

    private fun inRangeDueDate(closeDay: Int, dueDayExpected: Int) {
        every { workDayService.nextForInclusive(any()) } returnsArgument 0

        val closeDate = LocalDate.of(2019, Month.JUNE, closeDay)
        val dueDate = dueDateCalculator.calculate(closeDate)
        val expected = LocalDate.of(2019, Month.JUNE, dueDayExpected)
        assertThat(dueDate).isEqualTo(expected)

        verifyAll {
            workDayService.nextForInclusive(expected)
        }
        confirmAllVerified()
    }

    @Test
    fun `closing on holiday due date to next day`() {
        val holiday = LocalDate.of(2020, Month.SEPTEMBER, 7)
        val expected = LocalDate.of(2020, Month.SEPTEMBER, 8)
        every { workDayService.nextForInclusive(holiday) } returns expected

        val closeDate = LocalDate.of(2020, Month.SEPTEMBER, 2)
        val dueDate = dueDateCalculator.calculate(closeDate)
        assertThat(dueDate).isEqualTo(expected)

        verifyAll {
            workDayService.nextForInclusive(holiday)
        }
        confirmAllVerified()
    }

    @Test
    fun `closing too far due date near`() {
        val calculatedDate = LocalDate.of(2020, Month.SEPTEMBER, 5)
        val afterHoliday = LocalDate.of(2020, Month.SEPTEMBER, 8)
        val expected = LocalDate.of(2020, Month.SEPTEMBER, 4)

        every { workDayService.nextForInclusive(calculatedDate) } returns afterHoliday
        every { workDayService.isValid(expected) } returns true

        val closeDate = LocalDate.of(2020, Month.AUGUST, 31)
        val dueDate = dueDateCalculator.calculate(closeDate)
        assertThat(dueDate).isEqualTo(expected)

        verifyAll {
            workDayService.nextForInclusive(calculatedDate)
            workDayService.isValid(expected)
        }
        confirmAllVerified()
    }

    private fun confirmAllVerified() {
        confirmVerified(workDayService)
    }
}