package nl.fontys.sioux.siouxbackend.domain.request.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GetSimplifiedAppointmentsRequest {
    private List<Long> employeeIDs;
    private Date startDateFilter;
    private Date endDateFilter;
}
