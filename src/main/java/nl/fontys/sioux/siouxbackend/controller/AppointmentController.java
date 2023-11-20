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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public ResponseEntity<Void> updateAppointment(@PathVariable("id") Long id, @RequestBody @Valid UpdateAppointmentRequest request){
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
    public ResponseEntity<List<GetSimplifiedAppointmentsResponse>> getAppointmentsOfEmployeesByDateRange(
            @RequestParam(name = "employeeIDs", required = false) List<Long> employeeIDs,
            @RequestParam(name = "startDateFilter", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDateFilter,
            @RequestParam(name = "endDateFilter", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDateFilter) {
        GetSimplifiedAppointmentsRequest request = GetSimplifiedAppointmentsRequest.builder()
                .employeeIDs(employeeIDs)
                .startDateFilter(startDateFilter)
                .endDateFilter(endDateFilter)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(getSimplifiedAppointmentsUseCase.getSimplifiedAppointments(request));
    }

    @GetMapping("{id}")
    public ResponseEntity<GetAppointmentResponse> getAppointment(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(getAppointmentUseCase.getAppointment(id));
    }
}
