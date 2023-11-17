package nl.fontys.sioux.siouxbackend.business.impl.appointment;

import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.impl.employee.EmployeeConverter;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.GetSimplifiedAppointmentsUseCase;
import nl.fontys.sioux.siouxbackend.domain.Employee;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.GetSimplifiedAppointmentsRequest;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.GetSimplifiedAppointmentsResponse;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetSimplifiedAppointmentsUseCaseImpl implements GetSimplifiedAppointmentsUseCase {
    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    @Override
    public List<GetSimplifiedAppointmentsResponse> getSimplifiedAppointments(GetSimplifiedAppointmentsRequest request) {
        for (Long id: request.getEmployeeIDs()) {
            if(!employeeRepository.existsById(id)) {
                throw new InvalidEmployeeException("EMPLOYEE_ID_INVALID");
            }
        }

        List<Optional<AppointmentEntity>> appointments = appointmentRepository.findAppointmentsByEmployeeIdsAndTimeRange(request.getEmployeeIDs(), request.getStartDateFilter(), request.getEndDateFilter());

        List<GetSimplifiedAppointmentsResponse> response = new ArrayList<>();

        for(Optional<AppointmentEntity> appointment: appointments){

            if(appointment.isEmpty()){
                // throw exception
            }
            AppointmentEntity foundAppointment = appointment.get();

            List<Employee> employees = foundAppointment.getEmployees().stream()
                    .map(EmployeeConverter::convert)
                    .toList();

            response.add(new GetSimplifiedAppointmentsResponse(foundAppointment.getId(), foundAppointment.getClientName(), foundAppointment.getStartTime(), foundAppointment.getEndTime(), employees));
        }

        return response;
    }
}
