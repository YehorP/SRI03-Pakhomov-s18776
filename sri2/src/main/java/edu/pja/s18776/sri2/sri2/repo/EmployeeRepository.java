package edu.pja.s18776.sri2.sri2.repo;

import edu.pja.s18776.sri2.sri2.model.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    List<Employee> findAll();
}
