package nl.fontys.sioux.siouxbackend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.interf.SendMessagesUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.parking.ParkingAccessRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parking")
@AllArgsConstructor
public class ParkingController {
    private final SendMessagesUseCase sendMessagesUseCase;
    @PostMapping
    public ResponseEntity<String> sendMessages(@RequestBody @Valid ParkingAccessRequest request) {
        String response = sendMessagesUseCase.handleUpcomingAppointmentNotifications(request);

        return ResponseEntity.ok(response);
    }
}
