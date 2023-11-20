package nl.fontys.sioux.siouxbackend.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
@DynamicUpdate
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @NotBlank
    @Column(name = "client_name")
    private String clientName;

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @JoinTable(
            name = "appointment_assignment",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<EmployeeEntity> employees;

    @NotBlank
    @Email
    @Column(name = "client_email")
    private String clientEmail;

    @NotBlank
    @Column(name = "client_phone_number")
    private String clientPhoneNumber;

    @NotNull
    @Column(name = "start_time")
    private Date startTime;

    @NotNull
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "license_plate")
    private String licensePlate;

}
