package edu.pja.s18776.sri2.sri2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    private String lastName;
    private String middleName;
    @NotNull(message = "Birth Date is required")
    private LocalDate birthDate;
    @Min(value = 1, message = "minimum 1")
    private int semester;
    @Min(value = 0, message = "minimum 0")
    private double practiceHours;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private Set<StudentsGroup> groups;
}
