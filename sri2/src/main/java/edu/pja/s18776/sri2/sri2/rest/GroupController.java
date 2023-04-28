package edu.pja.s18776.sri2.sri2.rest;

import edu.pja.s18776.sri2.sri2.dto.DTOEntity;
import edu.pja.s18776.sri2.sri2.dto.GroupDetailsDto;
import edu.pja.s18776.sri2.sri2.dto.StudentsGroupDto;
import edu.pja.s18776.sri2.sri2.dto.StudentDto;
import edu.pja.s18776.sri2.sri2.model.Employee;
import edu.pja.s18776.sri2.sri2.model.StudentsGroup;
import edu.pja.s18776.sri2.sri2.model.Student;
import edu.pja.s18776.sri2.sri2.repo.GroupRepository;
import edu.pja.s18776.sri2.sri2.services.SimpleMappingService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private GroupRepository groupRepository;
    private SimpleMappingService mappingService;

    public GroupController(GroupRepository groupRepository, SimpleMappingService mappingService) {
        this.groupRepository = groupRepository;
        this.mappingService = mappingService;
    }

    @GetMapping(produces = {"application/hal+json"})
    public ResponseEntity<CollectionModel<DTOEntity>> getGroups() {
        List<StudentsGroup> allGroups = groupRepository.findAll();
        List<DTOEntity> result = allGroups.stream()
                .map(emp -> mappingService.convertToDto(emp, new StudentsGroupDto()))
                .collect(Collectors.toList());
        for(DTOEntity dto: result) {
            StudentsGroupDto group =  ((StudentsGroupDto)dto);
           group.add(createGroupSelfLink(group.getId()));
            group.add(createGroupStudentsLink(group.getId()));
        }
        Link linkSelf =  linkTo(methodOn(GroupController.class).getGroups()).withSelfRel();
        CollectionModel<DTOEntity> res = CollectionModel.of(result, linkSelf);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @GetMapping(value = "/{groupId}", produces = {"application/hal+json"})
    public ResponseEntity<DTOEntity>
    getGroupDetailsById(@PathVariable Long groupId) {
        Optional<StudentsGroup> emp =
                groupRepository.getGroupDetailsById(groupId);
        if(emp.isPresent()) {
            GroupDetailsDto groupDto = (GroupDetailsDto) mappingService.convertToDto(emp.get(), new GroupDetailsDto());
            Link linkSelf = createGroupSelfLink(groupId);
            groupDto.add(linkSelf);
            return new ResponseEntity<>(groupDto,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{groupId}/students")
    public ResponseEntity<List<DTOEntity>> getStudentsByGroupId(@PathVariable long groupId) {
        List<Student> students = groupRepository.findStudentsByGroupId(groupId);
        List<DTOEntity> result = students.stream()
                .map(el -> mappingService.convertToDto(el, new StudentDto()))
                .toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity saveNewGroup(@RequestBody StudentsGroupDto
                                                  group) {
        StudentsGroup entity = (StudentsGroup) mappingService.convertToEntity(new Employee(), group);
        groupRepository.save(entity);
        HttpHeaders headers = new HttpHeaders();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        headers.add("Location", location.toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }


    @PutMapping("/{groupId}")
    public ResponseEntity updateGroup(@PathVariable Long
                                                 groupId, @RequestBody GroupDetailsDto groupDto) {
        Optional<StudentsGroup> currentGroup =
                groupRepository.findById(groupId);
        if(currentGroup.isPresent()) {
            groupDto.setId(groupId);
            StudentsGroup entity = (StudentsGroup) mappingService.convertToEntity(new StudentsGroup(), groupDto);
            groupRepository.save(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity deleteGroup(@PathVariable Long groupId)
    {
        groupRepository.deleteById(groupId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private Link createGroupSelfLink(Long groupId) {
        Link linkSelf =  linkTo(methodOn(GroupController.class).getGroupDetailsById(groupId)).withSelfRel();
        return linkSelf;
    }

    private Link createGroupStudentsLink(Long groupId) {
        Link linkSelf =  linkTo(methodOn(GroupController.class).getStudentsByGroupId(groupId)).withSelfRel();
        return linkSelf;
    }
}