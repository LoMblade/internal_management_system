package com.example.internal_management_system.modules.hrm.mapper;

import com.example.internal_management_system.modules.hrm.dto.PayrollDto;
import com.example.internal_management_system.modules.hrm.model.Payroll;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PayrollMapper {

    @Mapping(target = "employeeName", expression = "java(payroll.getEmployee() != null ? payroll.getEmployee().getFirstName() + \" \" + payroll.getEmployee().getLastName() : null)")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    PayrollDto toDto(Payroll payroll);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Payroll toEntity(PayrollDto payrollDto);

    @Named("statusToString")
    default String statusToString(Payroll.PaymentStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToStatus")
    default Payroll.PaymentStatus stringToStatus(String status) {
        return status != null ? Payroll.PaymentStatus.valueOf(status) : null;
    }
}
