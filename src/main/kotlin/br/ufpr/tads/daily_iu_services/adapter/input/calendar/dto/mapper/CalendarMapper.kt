package br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.CalendarDayDTO
import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.UrinationDataDTO
import br.ufpr.tads.daily_iu_services.domain.entity.calendar.CalendarDay
import br.ufpr.tads.daily_iu_services.domain.entity.calendar.UrinationData
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import java.time.LocalDate

@Mapper
abstract class CalendarMapper {

    companion object{
        val INSTANCE: CalendarMapper = Mappers.getMapper(CalendarMapper::class.java)
    }

    @Mapping(target = "leakageLevel", expression = "java(calendar.getLeakageLevel().toString())")
    @Mapping(target = "urinationData", expression = "java(urinationDataListToDTO(calendar.getUrinationData()))")
    @Mapping(target = "dayNumber", constant = "1")
    @Mapping(target = "isToday", constant = "false")
    abstract fun calendarDaytoDTO(calendar: CalendarDay): CalendarDayDTO

    @AfterMapping
    fun calendarDTOAfterMapping(@MappingTarget calendarDTO: CalendarDayDTO){
        val date: LocalDate = calendarDTO.date
        val today: LocalDate = LocalDate.now()
        calendarDTO.dayNumber = date.dayOfMonth
        calendarDTO.isToday = date.isEqual(today)
    }

    @Named("timeToLocalTime")
    fun timeToLocalTime(time: java.sql.Time): java.time.LocalTime {
        return time.toLocalTime()
    }

    @Named("localTimeToTime")
    fun localTimeToTime(localTime: java.time.LocalTime): java.sql.Time {
        return java.sql.Time.valueOf(localTime)
    }

    @Mapping(target = "amount", expression = "java(data.getAmount().toString())")
    @Mapping(target = "time", source = "time", qualifiedByName = ["timeToLocalTime"])
    abstract fun urinationDatatoDTO(data: UrinationData): UrinationDataDTO

    @Mapping(target = "calendarDay", ignore = true )
    @Mapping(target = "amount", expression = "java(LeakageLevel.Companion.from(data.getAmount()))")
    @Mapping(target = "time", source = "time", qualifiedByName = ["localTimeToTime"])
    @Mapping(target = "urgency", source = "urgency", defaultValue = "false")
    abstract fun urinationDataDTOtoEntity(data: UrinationDataDTO): UrinationData

    fun urinationDataListToDTO(data: List<UrinationData>?): List<UrinationDataDTO>?{
        return data?.map { urinationDatatoDTO(it) }
    }
}