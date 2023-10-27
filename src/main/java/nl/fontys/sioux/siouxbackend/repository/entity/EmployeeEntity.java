package nl.fontys.sioux.siouxbackend.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.sioux.siouxbackend.domain.Position;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
@DynamicUpdate
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @NotBlank
    @Column(name = "first_name")
    @Length(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    @Length(min = 2, max = 50)
    private String lastName;

    @NotBlank
    @Email
    @Column(name = "email")
    @Length(min = 2, max = 50)
    private String email;

    @NotBlank
    @Length(min = 2)
    @Column(name="password")
    private String password;

    @NotNull
    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private Position position;

    @NotNull
    @Column(name = "active")
    private Boolean active;
}
