package db1.article02.dates

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

internal class WorkDayServiceTest {

    private var holidayService: HolidayService = mockkClass(HolidayService::class)
    private lateinit var workDayService: WorkDayService

    @BeforeEach
    fun setUp() {
        workDayService = WorkDayService(holidayService)
    }

    @Test
    fun `regular monday is workday`() {
        regularWeekDay(5)
    }

    @Test
    fun `regular tuesday is workday`() {
        regularWeekDay(6)
    }

    @Test
    fun `regular wednesday is workday`() {
        regularWeekDay(7)
    }

    @Test
    fun `regular thursday is workday`() {
        regularWeekDay(8)
    }

    @Test
    fun `regular friday is workday`() {
        regularWeekDay(9)
    }

    private fun regularWeekDay(day: Int) {
        val date = LocalDate.of(2020, Month.OCTOBER, day)
        every { holidayService.existFor(date) } returns false

        val result = workDayService.nextForInclusive(date)
        assertThat(result).isEqualTo(date)

        verifyAll {
            holidayService.existFor(date)
        }

        confirmAllVerified()
    }

    @Test
    fun `saturday is NOT workday`() {
        weekendDay(10)
    }

    @Test
    fun `sunday is NOT workday`() {
        weekendDay(11)
    }

    private fun weekendDay(day: Int) {
        every { holidayService.existFor(any()) } returns false
        val result = workDayService.nextForInclusive(LocalDate.of(2020, Month.OCTOBER, day))
        assertThat(result).isEqualTo(LocalDate.of(2020, Month.OCTOBER, 12))

        verify(atLeast = 1, atMost = 2) {
            holidayService.existFor(any())
        }
        confirmAllVerified()
    }

    @Test
    fun `holiday is NOT workday`() {
        val holiday = LocalDate.of(2020, Month.OCTOBER, 12)
        val workday = LocalDate.of(2020, Month.OCTOBER, 13)
        every { holidayService.existFor(holiday) } returns true
        every { holidayService.existFor(workday) } returns false

        val result = workDayService.nextForInclusive(holiday)
        assertThat(result).isEqualTo(workday)

        verifyAll {
            holidayService.existFor(holiday)
            holidayService.existFor(workday)
        }

        confirmAllVerified()
    }

    private fun confirmAllVerified() {
        confirmVerified(holidayService)
    }
}