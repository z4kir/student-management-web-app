package com.example.studentmanagementsystem.service.imp;

import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.entity.User;
import com.example.studentmanagementsystem.repository.StudentRepository;
import com.example.studentmanagementsystem.service.StudentService;
import com.example.studentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImp implements StudentService {
    private final StudentRepository repository;

    @Autowired
    private UserService userService;

    public StudentServiceImp(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void setId(Student student) {
        Long i = 1L;
        if (repository.existsById(i)) {
            while (repository.existsById(i)) {
                i++;
                student.setId(i);
            }
        } else {
            student.setId(i);
        }

    }

    @Override
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @Override
    public Student addStudent(Student student) {
        return repository.save(student);
    }

    @Override
    public Student getById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public Student updateStudent(Student student) {

        return repository.save(student);

    }

    @Override
    public void deleteStudent(Long id) {
        repository.deleteById(id);
        Long i = id;
        while (repository.existsById(i + 1L)) {
            Student student = new Student();
            student.setId(i);
            student.setFirstName(repository.getById(i + 1L).getFirstName());
            student.setLastName(repository.getById(i + 1L).getLastName());
            student.setEmail(repository.getById(i + 1L).getEmail());
            student.setRollNo(repository.getById(i + 1L).getRollNo());
            student.setUser(repository.getById(i + 1L).getUser());
            repository.deleteById(i + 1L);
            repository.save(student);
            i++;
        }

    }
//    paginated table

    @Override
    public Page<Student> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.findAll(pageable);
    }


//    Searching from student data and show in search result


    @Override
    public List<Student> findByFirstNameAndLastName(String firstName, String lastName) {
        return repository.findByFirstNameContainingAndLastNameContaining(firstName, lastName);
    }

    @Override
    public List<Student> findByFirstName(String firstName) {
        return repository.findByFirstNameContaining(firstName);
    }

    @Override
    public List<Student> findByFirstNameFromGivenUser(String firstName, User user) {
        return repository.findByFirstNameContainingAndUser(firstName, user);
    }

    @Override
    public List<Student> findByFirstNameAndLastNameFromGivenUser(String firstName, String lastName, User user) {
        return repository.findByFirstNameContainingAndLastNameContainingAndUser(firstName, lastName, user);
    }

    @Override
    public List<Student> findByRollNoFromGivenUser(String rollNo, User user) {
        return repository.findByRollNoAndUser(rollNo, user);
    }
}
