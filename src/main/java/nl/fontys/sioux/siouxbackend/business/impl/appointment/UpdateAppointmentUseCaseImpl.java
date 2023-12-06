package nl.fontys.sioux.siouxbackend.business.impl.appointment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidAppointmentException;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.SendAppointmentEmailUseCase;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.UpdateAppointmentUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.UpdateAppointmentRequest;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateAppointmentUseCaseImpl implements UpdateAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final SendAppointmentEmailUseCase sendAppointmentEmailUseCase;
    @Transactional
    @Override
    public void updateAppointment(UpdateAppointmentRequest request) {
        if(request.getEmployeeIDs() != null){
            for (Long id: request.getEmployeeIDs()) {
                if(!employeeRepository.existsById(id)) {
                    throw new InvalidEmployeeException("EMPLOYEE_ID_INVALID");
                }
            }
        }

        if(!appointmentRepository.existsById(request.getAppointmentID())){
            throw new InvalidAppointmentException("APPOINTMENT_ID_INVALID");
        }
        if(request.getClientPhoneNumber() != null && !CustomRegexValidator.checkPhoneNumberValid(request.getClientPhoneNumber())){
            throw new InvalidAppointmentException("INVALID_CLIENT_PHONE_NUMBER");
        }
        if(request.getLicensePlate() != null && !CustomRegexValidator.checkLicensePlateValid(request.getLicensePlate())){
            throw new InvalidAppointmentException("INVALID_CLIENT_LICENSE_PLATE");
        }

        AppointmentEntity appointment = appointmentRepository.findById(request.getAppointmentID()).get();

        if(request.getStartTime() != null && request.getEndTime() != null) {
            if(request.getStartTime().getTime() >= request.getEndTime().getTime()){
                throw new InvalidAppointmentException("INVALID_TIME");
            }
        } else if (request.getStartTime() != null){
            if(request.getStartTime().getTime() >= appointment.getEndTime().getTime()){
                throw new InvalidAppointmentException("INVALID_TIME");
            }
        } else if (request.getEndTime() != null) {
            if(appointment.getStartTime().getTime() >= request.getEndTime().getTime()){
                throw new InvalidAppointmentException("INVALID_TIME");
            }
        }

        if(request.getClientName() != null) { appointment.setClientName(request.getClientName()); }

        if(request.getClientEmail() != null) { appointment.setClientEmail(request.getClientEmail()); }

        if(request.getClientPhoneNumber() != null) { appointment.setClientPhoneNumber(request.getClientPhoneNumber()); }

        if(request.getDescription() != null) { appointment.setDescription(request.getDescription()); }

        if(request.getLocation() != null) { appointment.setLocation(request.getLocation()); }

        if(request.getLicensePlate() != null) { appointment.setLicensePlate(request.getLicensePlate()); }

        if(request.getStartTime() != null) { appointment.setStartTime(request.getStartTime()); }

        if(request.getEndTime() != null) { appointment.setEndTime(request.getEndTime()); }

        List<EmployeeEntity> removedEmployees = new ArrayList<>();
        List<EmployeeEntity> addedEmployees;
        List<EmployeeEntity> employeesWithoutAdded = new ArrayList<>();

        if(request.getEmployeeIDs() != null) {
            List<EmployeeEntity> updatedEmployees = request.getEmployeeIDs()
                    .stream()
                    .map(employeeRepository::findById)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());

            List<EmployeeEntity> initialEmployees = appointment.getEmployees();

            removedEmployees = initialEmployees.stream()
                    .filter(employee -> !updatedEmployees.contains(employee))
                    .collect(Collectors.toList());

            addedEmployees = updatedEmployees.stream()
                    .filter(employee -> !initialEmployees.contains(employee))
                    .collect(Collectors.toList());

            employeesWithoutAdded = updatedEmployees.stream()
                    .filter(employee -> !addedEmployees.contains(employee))
                    .collect(Collectors.toList());

            appointment.setEmployees(updatedEmployees);
        } else {
            addedEmployees = new ArrayList<>();
        }

        appointmentRepository.save(appointment);

        sendClientEmail(appointment);
        sendEmployeesEmail(employeesWithoutAdded, appointment);

        if(!removedEmployees.isEmpty()){
            sendRemovedEmployeesEmail(removedEmployees, appointment);
        }

        if(!addedEmployees.isEmpty()){
            sendAddedEmployeesEmail(addedEmployees, appointment);
        }
    }

    private List<String> getEmployeeEmails(List<EmployeeEntity> employeeEntities){
        List<String> emails = new ArrayList<>();

        for(EmployeeEntity emp: employeeEntities){
            emails.add(emp.getEmail());
        }

        return emails;
    }

    private void sendClientEmail(AppointmentEntity appointment){
        String subject = "Sioux Appointment Updated!";
        StringBuilder employeesList = new StringBuilder();
        for (EmployeeEntity employee : appointment.getEmployees()) {
            employeesList.append("<li>").append(employee.getFirstName()).append(" ").append(employee.getLastName()).append("</li>");
        }

        String htmlTemplate = """
                <html>
                    <body>
                        <h1>Updated Appointment</h1>
                        <p>Dear %s, your appointment details has been changed. Below are the updated details.</p>
                                
                        <h3>Appointment Details</h3>
                        <p>Attendees: </p>
                        <ul>
                            %s
                        </ul>
                        <p>Start Time: %s</p>
                        <p>End Time: %s</p>
                        <p>Location: %s</p>
                        <p>Description: %s</p>
                    </body>
                </html>
                """;

        String emailBody = String.format(htmlTemplate, appointment.getClientName(), employeesList, appointment.getStartTime().toString(), appointment.getEndTime().toString(), appointment.getLocation(), appointment.getDescription());

        sendAppointmentEmailUseCase.sendAppointmentConfirmation(List.of(appointment.getClientEmail()), subject, emailBody);
    }

    private void sendEmployeesEmail(List<EmployeeEntity> employeeEntities, AppointmentEntity appointment){
        List<String> emails = getEmployeeEmails(employeeEntities);
        String subject = "Updated Appointment With Client";
        StringBuilder employeesList = new StringBuilder();
        for (EmployeeEntity employee : appointment.getEmployees()) {
            employeesList.append("<li>").append(employee.getFirstName()).append(" ").append(employee.getLastName()).append("</li>");
        }

        String htmlTemplate = """
                <html>
                    <body>
                        <h1>Updated Appointment</h1>
                        <p>Dear Employee, your appointment details has been changed. Below are the updated details.</p>
                                
                        <h3>Appointment Details</h3>
                        <p>Client: %s</p>
                        <p>Attendees: </p>
                        <ul>
                            %s
                        </ul>
                        <p>Start Time: %s</p>
                        <p>End Time: %s</p>
                        <p>Location: %s</p>
                        <p>Description: %s</p>
                    </body>
                </html>
                """;

        String emailBody = String.format(htmlTemplate, appointment.getClientName(), employeesList, appointment.getStartTime().toString(), appointment.getEndTime().toString(), appointment.getLocation(), appointment.getDescription());
        sendAppointmentEmailUseCase.sendAppointmentConfirmation(emails, subject, emailBody);
    }

    private void sendRemovedEmployeesEmail(List<EmployeeEntity> removedEmployees, AppointmentEntity appointment){
        List<String> emails = getEmployeeEmails(removedEmployees);
        String subject = "Appointment With Client Cancelled";

        String htmlTemplate = """
                <html>
                    <body>
                        <h1>Appointment Cancelled</h1>
                        <p>Dear Employee, your participation as an attendee has been revised, and you will no longer be part of the meeting</p>
                                
                        <h3>Appointment Details</h3>
                        <p>Client: %s</p>
                        <p>Start Time: %s</p>
                        <p>End Time: %s</p>
                        <p>Location: %s</p>
                        <p>Description: %s</p>
                    </body>
                </html>
                """;

        String emailBody = String.format(htmlTemplate, appointment.getClientName(), appointment.getStartTime().toString(), appointment.getEndTime().toString(), appointment.getLocation(), appointment.getDescription());
        sendAppointmentEmailUseCase.sendAppointmentConfirmation(emails, subject, emailBody);
    }

    private void sendAddedEmployeesEmail(List<EmployeeEntity> addedEmployees, AppointmentEntity appointment){
        List<String> emails = getEmployeeEmails(addedEmployees);
        String subject = "Appointment With Client Created!";
        StringBuilder employeesList = new StringBuilder();
        for (EmployeeEntity employee : appointment.getEmployees()) {
            employeesList.append("<li>").append(employee.getFirstName()).append(" ").append(employee.getLastName()).append("</li>");
        }

        String htmlTemplate = """
                <html>
                    <body>
                        <h1>Appointment Confirmation</h1>
                        <p>Dear Employee, you have been added to this meeting.</p>
                                
                        <h3>Appointment Details</h3>
                        <p>Client: %s</p>
                        <p>Attendees: </p>
                        <ul>
                            %s
                        </ul>
                        <p>Start Time: %s</p>
                        <p>End Time: %s</p>
                        <p>Location: %s</p>
                        <p>Description: %s</p>
                    </body>
                </html>
                """;

        String emailBody = String.format(htmlTemplate, appointment.getClientName(), employeesList, appointment.getStartTime().toString(), appointment.getEndTime().toString(), appointment.getLocation(), appointment.getDescription());
        sendAppointmentEmailUseCase.sendAppointmentConfirmation(emails, subject, emailBody);
    }
}
