package nl.fontys.sioux.siouxbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.*;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.CreateAppointmentRequest;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.GetSimplifiedAppointmentsRequest;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.UpdateAppointmentRequest;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.CreateAppointmentResponse;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.GetAppointmentResponse;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.GetSimplifiedAppointmentsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
@CrossOrigin
public class AppointmentController {
    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final UpdateAppointmentUseCase updateAppointmentUseCase;
    private final DeleteAppointmentUseCase deleteAppointmentUseCase;
    private final GetSimplifiedAppointmentsUseCase getSimplifiedAppointmentsUseCase;
    private final GetAppointmentUseCase getAppointmentUseCase;

    @PostMapping()
    public ResponseEntity<CreateAppointmentResponse> createAppointment(@RequestBody @Valid CreateAppointmentRequest request){
        CreateAppointmentResponse response = createAppointmentUseCase.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateAppointment(@PathVariable("id") Long id, UpdateAppointmentRequest request){
        request.setAppointmentID(id);
        updateAppointmentUseCase.updateAppointment(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("id") Long id) {
        deleteAppointmentUseCase.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<GetSimplifiedAppointmentsResponse>> getAppointmentsOfEmployeesByDateRange(@RequestBody @Valid GetSimplifiedAppointmentsRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(getSimplifiedAppointmentsUseCase.getSimplifiedAppointments(request));
    }

    @GetMapping("{id}")
    public ResponseEntity<GetAppointmentResponse> getAppointment(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(getAppointmentUseCase.getAppointment(id));
    }
}
