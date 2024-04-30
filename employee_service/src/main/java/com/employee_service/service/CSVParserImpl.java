package com.employee_service.service;


import com.employee_service.model.Employee;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVParserImpl implements CSVParser {

    private LocalDate parseDate(String dateString) {
        // Parse date in mm/dd/yyyy format
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    private boolean isValidGrade(String grade) {
        // Check if grade is in the format M1 to M6
        return grade.matches("^M[1-6]$");
    }

    @Override
    public List<Employee> readCsv(MultipartFile file) throws IOException, CsvValidationException {
        List<Employee> employees = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVReader csvReader = new CSVReader(reader);
        String[] headers = csvReader.readNext();
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
            boolean skipRow = false; // Flag to skip row if any required field is missing
            Employee employee = new Employee();
            for (int i = 0; i < headers.length; i++) {
                switch (headers[i].toLowerCase().trim()) {
                    case "firstname":
                        if (nextRecord[i] == null || nextRecord[i].isEmpty()) {
                            skipRow = true;
                            break;
                        }
                        employee.setFirstName(nextRecord[i]);
                        break;
                    case "lastname":
                        if (nextRecord[i] == null || nextRecord[i].isEmpty()) {
                            skipRow = true;
                            break;
                        }
                        employee.setLastName(nextRecord[i]);
                        break;
                    case "dateofbirth":
                        if (nextRecord[i] == null || nextRecord[i].isEmpty()) {
                            skipRow = true;
                            break;
                        }
                        employee.setDateOfBirth(parseDate(nextRecord[i]));
                        break;
                    case "dateofjoining":
                        if (nextRecord[i] == null || nextRecord[i].isEmpty()) {
                            skipRow = true;
                            break;
                        }
                        employee.setDateOfJoining(parseDate(nextRecord[i]));
                        break;
                    case "grade":
                        if (nextRecord[i] == null || nextRecord[i].isEmpty() || (!isValidGrade(nextRecord[i].trim()))) {
                            skipRow = true;
                            break;
                        }
                        employee.setGrade(nextRecord[i].trim());
                        break;
                    default:
                        // Ignore unknown headers
                        break;
                }
            }
            if (!skipRow) {
                employees.add(employee);
            }
        }
        return employees;
    }
}
