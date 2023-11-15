package nl.fontys.sioux.siouxbackend.repository;

import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
}
