package nl.fontys.sioux.siouxbackend.business.interf.employee;

import nl.fontys.sioux.siouxbackend.domain.request.employee.LoginRequest;
import nl.fontys.sioux.siouxbackend.domain.response.employee.AccessTokenResponse;

public interface LoginUseCase {
    AccessTokenResponse login(LoginRequest request);
}
