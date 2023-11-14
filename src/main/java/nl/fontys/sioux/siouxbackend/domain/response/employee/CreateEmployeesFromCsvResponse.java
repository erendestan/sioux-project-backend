package nl.fontys.sioux.siouxbackend.domain.response.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeesFromCsvResponse {
    private Long count;
}
