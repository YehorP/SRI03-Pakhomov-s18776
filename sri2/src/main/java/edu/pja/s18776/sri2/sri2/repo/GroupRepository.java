package edu.pja.s18776.sri2.sri2.repo;

import edu.pja.s18776.sri2.sri2.model.Student;
import edu.pja.s18776.sri2.sri2.model.StudentsGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends CrudRepository<StudentsGroup, Long> {
    List<StudentsGroup> findAll();

    @Query("from StudentsGroup as g left join fetch g.students where g.id=:groupId")
    Optional<StudentsGroup> getGroupDetailsById(@Param("groupId") long groupId);

    @Query("select g.students from StudentsGroup as g where g.id=:groupId")
    List<Student> findStudentsByGroupId(@PathVariable long groupId);
}
