package nl.fontys.sioux.siouxbackend.repository.employee;

import jakarta.persistence.EntityManager;
import nl.fontys.sioux.siouxbackend.domain.Position;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(properties = {
//        "spring.datasource.url=jdbc:tc:mysql://localhost:3306/testdb",
//        "spring.datasource.driver-class-name=org.testcontainers.jdbc.MySQLContainerDatabaseDriver"
//})
public class EmployeeRepositoryTests {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void save_ShouldSave() {
        EmployeeEntity employee = EmployeeEntity.builder()
                .email("some@email.com")
                .firstName("bruh")
                .lastName("moment")
                .password("pass")
                .position(Position.Employee)
                .active(true)
                .build();

        EmployeeEntity savedEmployee = employeeRepository.save(employee);
        assertNotNull(savedEmployee);

        savedEmployee = entityManager.find(EmployeeEntity.class, savedEmployee.getId());
        assertEquals(savedEmployee, employee);
    }
}
