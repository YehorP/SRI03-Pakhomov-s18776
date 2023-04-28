package edu.pja.s18776.sri2.sri2.repo;

import edu.pja.s18776.sri2.sri2.model.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


public interface StudentRepository extends CrudRepository<Student, Long> {
    List<Student> findAll();


}
