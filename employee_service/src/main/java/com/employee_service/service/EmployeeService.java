package com.employee_service.service;

import com.employee_service.dto.ResponseDto;
import com.employee_service.model.Employee;
import com.employee_service.model.EmployeeFilterDTO;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {

    public ResponseDto saveEmployeeData(MultipartFile file) throws CsvValidationException, IOException;

    public ResponseDto fetchEmployee(Integer employeeId);

    public ResponseDto modifyEmployee(Employee employeeData);

    public ResponseDto searchEmployee(Integer pageNo, Integer pageSize, EmployeeFilterDTO filterDTO);

    public byte[] generateCSV(List<Employee> employeeList) throws IOException;
}
