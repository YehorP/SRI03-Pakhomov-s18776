package edu.pja.s18776.sri2.sri2.rest;

import edu.pja.s18776.sri2.sri2.dto.DTOEntity;
import edu.pja.s18776.sri2.sri2.dto.EmployeeDto;
import edu.pja.s18776.sri2.sri2.model.Employee;
import edu.pja.s18776.sri2.sri2.repo.EmployeeRepository;
import edu.pja.s18776.sri2.sri2.services.SimpleMappingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    private SimpleMappingService mappingService;

    public EmployeeController(EmployeeRepository employeeRepository, SimpleMappingService mappingService) {
        this.employeeRepository = employeeRepository;
        this.mappingService = mappingService;
    }

    @GetMapping
    public ResponseEntity<Collection<DTOEntity>> getEmployees() {
        List<Employee> allEmployees = employeeRepository.findAll();
        List<DTOEntity> result = allEmployees.stream()
                .map(emp -> mappingService.convertToDto(emp, new EmployeeDto()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/{empId}")
    public ResponseEntity<DTOEntity>
    getEmployeeById(@PathVariable Long empId) {
        Optional<Employee> emp =
                employeeRepository.findById(empId);
        if(emp.isPresent()) {
            DTOEntity employeeDto = mappingService.convertToDto(emp.get(), new EmployeeDto());
            return new ResponseEntity<>(employeeDto,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,
                    HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity saveNewEmployee(@RequestBody EmployeeDto
                                                  emp) {
        Employee entity = (Employee) mappingService.convertToEntity(new Employee(), emp);
        employeeRepository.save(entity);
        HttpHeaders headers = new HttpHeaders();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        headers.add("Location", location.toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }


    @PutMapping("/{empId}")
    public ResponseEntity updateEmployee(@PathVariable Long
                                                 empId, @RequestBody EmployeeDto employeeDto) {
        Optional<Employee> currentEmp =
                employeeRepository.findById(empId);
        if(currentEmp.isPresent()) {
            employeeDto.setId(empId);
            Employee entity = (Employee) mappingService.convertToEntity(new Employee(), employeeDto);
            employeeRepository.save(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{empId}")
    public ResponseEntity deleteEmployee(@PathVariable Long empId)
    {
        employeeRepository.deleteById(empId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
