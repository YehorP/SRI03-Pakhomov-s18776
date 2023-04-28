package edu.pja.s18776.sri2.sri2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDetailsDto extends RepresentationModel<GroupDetailsDto> implements DTOEntity{
    private Long id;
    private String subject;
    private String groupName;
    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate creationDate;
    private Set<StudentDto> students;
}
