package com.prepmaster.demo.department;

import com.prepmaster.demo.admin.Admin;
import com.prepmaster.demo.admin.AdminService;
import com.prepmaster.demo.exception.NotFoundException;
import com.prepmaster.demo.teacher.Teacher;
import com.prepmaster.demo.teacher.TeacherService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor //LOMBOK SEE VIDEO
@Slf4j // so we can use log variable
public class DepartmentService {
    private  final DepartmentRepository departmentRepository;
    private final AdminService adminService;
    private final TeacherService teacherService;

    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }
    public Department getDepartment(Long id) {
        return departmentRepository
                .findById(id)
                .orElseThrow(
                        () -> {
                            NotFoundException notFoundException = new NotFoundException("Department with ID " + id + " not found");
                            log.error("error department {} not found", id , notFoundException);
                            return notFoundException;
                        }
                );
    }
    public void createNewDepartment(DepartmentRequestBody departmentRequestBody) {
        Department department = departmentRequestBody.getDepartment();
        log.info("Creating department {}", department);
        Admin admin = adminService.getAdmin(departmentRequestBody.getDepartmentHeadId());
        Teacher departmentHead = teacherService.getTeacher(departmentRequestBody.getAdminId());
        department.setAdmin(admin);
        department.setDepartmentHead(departmentHead);
        departmentRepository.save(department);
        log.info("Created department {} successfully", department.getId());
    }
    public void updateDepartment(DepartmentRequestBody departmentRequestBody) {
        Department department = departmentRequestBody.getDepartment();
        log.info("Creating department {}", department);
        Admin admin = adminService.getAdmin(departmentRequestBody.getDepartmentHeadId());
        Teacher departmentHead = teacherService.getTeacher(departmentRequestBody.getAdminId());
        department.setAdmin(admin);
        department.setDepartmentHead(departmentHead);
        if (!departmentRepository.existsById(department.getId())) {
            NotFoundException notFoundException = new NotFoundException("department with ID " + department.getId() + " not found");
            log.error("error department {} not found could not update a non existing tuple", department.getId() , notFoundException);
            return;
        }
        departmentRepository.save(department);
        log.info("Updated course {} successfully", department.getId());
    }

    public void deleteDepartment(Long id) {
        log.info("Deleting department {}", id);
        if (!departmentRepository.existsById(id)) {
            NotFoundException notFoundException = new NotFoundException("department with ID " + id + " not found");
            log.error("error department {} not found could not delete a non existing tuple", id , notFoundException);
            return;
        }
        departmentRepository.deleteById(id);
        log.info("Deleted department {} successfully", id);
    }
}
