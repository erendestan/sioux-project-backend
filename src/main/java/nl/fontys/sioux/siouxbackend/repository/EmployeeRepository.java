package nl.fontys.sioux.siouxbackend.repository;

import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    boolean existsByEmail(String username);

    //List<EmployeeEntity> filter()
}
