package nl.fontys.sioux.siouxbackend.repository;

import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    @Query("SELECT DISTINCT a FROM AppointmentEntity a JOIN a.employees e " +
            "WHERE e.id IN :employeeIds " +
            "AND a.startTime <= :endTime " +
            "AND a.endTime >= :startTime")
    List<Optional<AppointmentEntity>> findAppointmentsByEmployeeIdsAndTimeRange(
            @Param("employeeIds") List<Long> employeeIds,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AppointmentEntity a " +
            "JOIN a.employees e " +
            "WHERE e.id IN :employeeIDs " +
            "AND ((a.startTime BETWEEN :startTime AND :endTime) OR (a.endTime BETWEEN :startTime AND :endTime))")
    boolean existsAppointmentForEmployeesInTimeRange(
            @Param("employeeIDs") List<Long> employeeIDs,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime
    );

    // Find appointment starting in the next hour for a specific license plate
    Optional<AppointmentEntity> findFirstByStartTimeBetweenAndLicensePlate(Date start, Date end, String licensePlate);
}
