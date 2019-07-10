package db1.article02.dates

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

internal class HolidayServiceTest {

    private var holidayService: HolidayService = HolidayService()

    @Test
    fun `regular day`() {
        assertThat(holidayService.existFor(LocalDate.of(2019, Month.SEPTEMBER, 6))).isFalse()
        assertThat(holidayService.existFor(LocalDate.of(2019, Month.SEPTEMBER, 8))).isFalse()
        assertThat(holidayService.existFor(LocalDate.of(2019, Month.DECEMBER, 24))).isFalse()
        assertThat(holidayService.existFor(LocalDate.of(2019, Month.DECEMBER, 26))).isFalse()
        assertThat(holidayService.existFor(LocalDate.of(2019, Month.DECEMBER, 31))).isFalse()
        assertThat(holidayService.existFor(LocalDate.of(2020, Month.JANUARY, 2))).isFalse()
    }

    @Test
    fun `known holidays`() {
        assertThat(holidayService.existFor(LocalDate.of(2019, Month.SEPTEMBER, 7))).isTrue()
        assertThat(holidayService.existFor(LocalDate.of(2019, Month.DECEMBER, 25))).isTrue()
        assertThat(holidayService.existFor(LocalDate.of(2020, Month.JANUARY, 1))).isTrue()
        assertThat(holidayService.existFor(LocalDate.of(2020, Month.SEPTEMBER, 7))).isTrue()
        assertThat(holidayService.existFor(LocalDate.of(2020, Month.DECEMBER, 25))).isTrue()
        assertThat(holidayService.existFor(LocalDate.of(2021, Month.JANUARY, 1))).isTrue()
    }

}