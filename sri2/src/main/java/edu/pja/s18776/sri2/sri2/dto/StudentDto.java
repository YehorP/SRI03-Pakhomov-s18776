package edu.pja.s18776.sri2.sri2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.pja.s18776.sri2.sri2.model.StudentsGroup;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto implements  DTOEntity{
    private Long id;
    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    private String lastName;
    private String middleName;
    @NotNull(message = "Birth Date is required")
    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate birthDate;
    @Min(value = 1, message = "minimum 1")
    private int semester;
    @Min(value = 1, message = "minimum 0")
    private double practiceHours;
}
