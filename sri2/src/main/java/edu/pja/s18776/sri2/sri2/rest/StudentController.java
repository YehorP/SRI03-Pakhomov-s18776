package edu.pja.s18776.sri2.sri2.rest;

import edu.pja.s18776.sri2.sri2.dto.DTOEntity;
import edu.pja.s18776.sri2.sri2.dto.StudentDto;
import edu.pja.s18776.sri2.sri2.model.StudentsGroup;
import edu.pja.s18776.sri2.sri2.model.Student;
import edu.pja.s18776.sri2.sri2.repo.GroupRepository;
import edu.pja.s18776.sri2.sri2.repo.StudentRepository;
import edu.pja.s18776.sri2.sri2.services.SimpleMappingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/student")
public class StudentController {
    private StudentRepository repository;

    private GroupRepository groupRepository;
    private SimpleMappingService mappingService;

    public StudentController(StudentRepository repository, SimpleMappingService mappingService, GroupRepository groupRepository) {
        this.repository = repository;
        this.mappingService = mappingService;
        this.groupRepository = groupRepository;
    }

    @GetMapping
    public ResponseEntity<Collection<DTOEntity>> getStudents() {
        List<Student> students = repository.findAll();
        List<DTOEntity> result = students.stream()
                .map(el -> mappingService.convertToDto(el, new StudentDto()))
                .toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<DTOEntity> getStudentById(@PathVariable long studentId) {
        Optional<Student> student = repository.findById(studentId);
        return student.isPresent() ? new ResponseEntity<>(mappingService.convertToDto(student.get(), new StudentDto()), HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity createStudent(@Valid @RequestBody StudentDto studentData) {
        Student student = (Student) mappingService.convertToEntity(new Student(), studentData);
        this.repository.save(student);
        HttpHeaders headers = new HttpHeaders();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(student.getId())
                .toUri();
        headers.add("Location", location.toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }


    @PutMapping("/{studentId}")
    public ResponseEntity<StudentDto> updateStudent(@Valid @RequestBody StudentDto studentData, @PathVariable long studentId) {
        Optional<Student> dbStudent = repository.findById(studentId);
        if (dbStudent.isPresent()) {
            studentData.setId(studentId);
            Student student = (Student) mappingService.convertToEntity(new Student(), studentData);
            repository.save(student);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity deleteStudent(@PathVariable long studentId) {
        this.repository.deleteById(studentId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{studentId}/groups/{groupId}")
    public ResponseEntity<StudentDto> connectStudentWithGroup(@PathVariable long studentId, @PathVariable long groupId) {
        Optional<Student> student = repository.findById(studentId);
        Optional<StudentsGroup> group = groupRepository.findById(groupId);
        if (!group.isPresent() || !student.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Student s = student.get();
        StudentsGroup g = group.get();

        if(s.getGroups().contains(g))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        s.getGroups().add(g);
        repository.save(s);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{studentId}/groups/{groupId}")
    public ResponseEntity separateStudentAndGroup(@PathVariable long studentId, @PathVariable long groupId) {
        Optional<Student> student = repository.findById(studentId);
        Optional<StudentsGroup> group = groupRepository.findById(groupId);
        if (!student.isPresent() || !group.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Student s = student.get();
        StudentsGroup g = group.get();
        if (!s.getGroups().contains(g)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        s.getGroups().remove(g);
        repository.save(s);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
