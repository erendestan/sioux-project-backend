package nl.fontys.sioux.siouxbackend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.interf.employee.LoginUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.employee.LoginRequest;
import nl.fontys.sioux.siouxbackend.domain.response.employee.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class AuthController {
    private final LoginUseCase loginUseCase;
    @PostMapping
    public ResponseEntity<AccessTokenResponse> login(@RequestBody @Valid LoginRequest request) {
        AccessTokenResponse accessTokenResponse = loginUseCase.login(request);

        return ResponseEntity.ok(accessTokenResponse);
    }
}
