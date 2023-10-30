package nl.fontys.sioux.siouxbackend.repository;

import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    boolean existsByEmail(String username);

    @Query("select e from EmployeeEntity e where e.firstName like LOWER(CONCAT('%', :param,'%')) or e.lastName like LOWER(CONCAT('%', :param,'%')) or e.email like LOWER(CONCAT('%', :param,'%'))")
    List<EmployeeEntity> filterEmployees(@Param("param") String parameters);
}
