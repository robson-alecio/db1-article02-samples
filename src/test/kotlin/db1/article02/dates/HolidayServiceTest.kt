package db1.article02.dates

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.time.Month
import java.util.stream.Stream

internal class HolidayServiceTest {

    private var holidayService: HolidayService = HolidayService()

    companion object {
        @JvmStatic
        fun regularDayInput(): Stream<LocalDate> = Stream.of(
            LocalDate.of(2019, Month.SEPTEMBER, 6),
            LocalDate.of(2019, Month.SEPTEMBER, 8),
            LocalDate.of(2019, Month.DECEMBER, 24),
            LocalDate.of(2019, Month.DECEMBER, 26),
            LocalDate.of(2019, Month.DECEMBER, 31),
            LocalDate.of(2020, Month.JANUARY, 2)
        )

        @JvmStatic
        fun knownHolidaysInput(): Stream<LocalDate> = Stream.of(
            LocalDate.of(2019, Month.SEPTEMBER, 7),
            LocalDate.of(2019, Month.DECEMBER, 25),
            LocalDate.of(2020, Month.JANUARY, 1),
            LocalDate.of(2020, Month.SEPTEMBER, 7),
            LocalDate.of(2020, Month.DECEMBER, 25),
            LocalDate.of(2021, Month.JANUARY, 1)
        )
    }

    @ParameterizedTest
    @MethodSource("regularDayInput")
    fun `regular day`(date: LocalDate) {
        assertThat(holidayService.existFor(date)).isFalse()
    }

    @ParameterizedTest
    @MethodSource("knownHolidaysInput")
    fun `known holidays`(date: LocalDate) {
        assertThat(holidayService.existFor(date)).isTrue()
    }

}