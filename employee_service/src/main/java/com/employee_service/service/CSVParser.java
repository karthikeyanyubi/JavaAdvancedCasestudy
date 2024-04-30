package com.employee_service.service;

import com.employee_service.model.Employee;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CSVParser {

    public List<Employee> readCsv(MultipartFile file) throws IOException, CsvValidationException;
}
