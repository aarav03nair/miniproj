package com.example.miniproj.controller;


import com.example.miniproj.model.Student;
import com.example.miniproj.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String adminPage(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "admin";
    }

    @PostMapping("/students")
    public String addStudent(@RequestParam String username, @RequestParam String password) {
        Student student = new Student();
        student.setUsername(username);
        student.setPassword(password);
        studentService.addStudent(student);
        return "redirect:/admin";
    }

    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id, @RequestParam String password) {

        Optional<Student> studentOptional = studentService.getStudentById(id);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setPassword(password);
            studentService.updateStudent(id, student);
        } else {
            // Handle case where student with given id is not found
            throw new IllegalArgumentException("Student with ID " + id + " not found");
        }

        return "redirect:/admin";
    }
    @PostMapping("/students/{id}/delete")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/admin";
    }
}