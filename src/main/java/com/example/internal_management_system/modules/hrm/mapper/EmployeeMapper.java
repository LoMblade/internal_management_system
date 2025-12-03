package com.example.internal_management_system.modules.hrm.mapper;

import com.example.internal_management_system.modules.hrm.dto.EmployeeDto;
import com.example.internal_management_system.modules.hrm.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "fullName", expression = "java(employee.getFirstName() + \" \" + employee.getLastName())")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "positionTitle", source = "position.title")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    EmployeeDto toDto(Employee employee);

    @Mapping(target = "department", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Employee toEntity(EmployeeDto employeeDto);

    @Named("statusToString")
    default String statusToString(Employee.EmployeeStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToStatus")
    default Employee.EmployeeStatus stringToStatus(String status) {
        return status != null ? Employee.EmployeeStatus.valueOf(status) : null;
    }
}
