package nl.fontys.sioux.siouxbackend.business.interf.employee;

public interface SendNewEmployeeEmailUseCase {
    void sendEmployeeCreatedConfirmation(String to, String subject, String body);
}
