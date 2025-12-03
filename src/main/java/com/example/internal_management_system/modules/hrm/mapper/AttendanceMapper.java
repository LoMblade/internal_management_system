package com.example.internal_management_system.modules.hrm.mapper;

import com.example.internal_management_system.modules.hrm.dto.AttendanceDto;
import com.example.internal_management_system.modules.hrm.model.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(target = "employeeName", expression = "java(attendance.getEmployee() != null ? attendance.getEmployee().getFirstName() + \" \" + attendance.getEmployee().getLastName() : null)")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    AttendanceDto toDto(Attendance attendance);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Attendance toEntity(AttendanceDto attendanceDto);

    @Named("statusToString")
    default String statusToString(Attendance.AttendanceStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToStatus")
    default Attendance.AttendanceStatus stringToStatus(String status) {
        return status != null ? Attendance.AttendanceStatus.valueOf(status) : null;
    }
}
