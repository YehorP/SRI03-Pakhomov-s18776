package edu.pja.s18776.sri2.sri2;


import edu.pja.s18776.sri2.sri2.model.Student;
import edu.pja.s18776.sri2.sri2.model.StudentsGroup;
import edu.pja.s18776.sri2.sri2.repo.GroupRepository;
import edu.pja.s18776.sri2.sri2.repo.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

@AllArgsConstructor
@Component
public class DataInitializer implements ApplicationRunner {

    private StudentRepository studentRepository;
    private GroupRepository groupRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        StudentsGroup g1 = StudentsGroup.builder()
                .subject("ZPRO")
                .groupName("c18")
                .creationDate(LocalDate.now())
                .students(new HashSet<>())
                .build();
        StudentsGroup g2 = StudentsGroup.builder()
                .subject("PRI")
                .groupName("c19")
                .creationDate(LocalDate.now())
                .students(new HashSet<>())
                .build();
        Student s1 = Student.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .middleName("Paweł")
                .birthDate(LocalDate.now())
                .practiceHours(0)
                .semester(1)
                .groups(new HashSet<>())
                .build();
        Student s2 = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .middleName("Bill")
                .birthDate(LocalDate.now())
                .practiceHours(168)
                .semester(5)
                .groups(new HashSet<>())
                .build();
        Student s3 = Student.builder()
                .firstName("Joe")
                .lastName("Biden")
                .middleName("Alex")
                .birthDate(LocalDate.now())
                .practiceHours(168)
                .semester(7)
                .groups(new HashSet<>())
                .build();

        g1.getStudents().add(s1);
        g2.getStudents().add(s1);

        g1.getStudents().add(s2);
        g2.getStudents().add(s3);

        s1.getGroups().add(g1);
        s1.getGroups().add(g2);

        s2.getGroups().add(g1);

        s3.getGroups().add(g2);
        groupRepository.saveAll(Arrays.asList(g1,g2));
    }
}
