package br.com.services;

import br.com.entities.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    public Student addStudent(Student student);
    public List<Student> getStudents();
    public Student getStundentById(Long id);
}
