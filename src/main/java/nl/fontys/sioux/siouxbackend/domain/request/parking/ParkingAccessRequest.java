package nl.fontys.sioux.siouxbackend.domain.request.parking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingAccessRequest {
    private String licensePlate;
    private boolean parkingTaken;
}
