package com.employee_service.controller;


import com.employee_service.dto.EmployeeSearchDto;
import com.employee_service.dto.ResponseDto;
import com.employee_service.model.Employee;
import com.employee_service.model.EmployeeFilterDTO;
import com.employee_service.service.EmployeeService;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/data/upload")
    public ResponseEntity<ResponseDto> uploadEmployeeData(@RequestHeader(name="token") String token, @RequestParam("file") MultipartFile file) throws CsvValidationException, IOException {
        ResponseDto response = employeeService.saveEmployeeData(file);
        // Save or process the list of employees as required
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/fetch/{employeeId}")
    public ResponseEntity<ResponseDto> fetchEmployeeData(@RequestHeader(name="token") String token,@PathVariable Integer employeeId) {
        ResponseDto response = employeeService.fetchEmployee(employeeId);
        // Save or process the list of employees as required
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/modify")
    public ResponseEntity<ResponseDto> modifyEmployeeData(@RequestHeader(name="token") String token, @Valid @RequestBody Employee employeeData) {
        ResponseDto response = employeeService.modifyEmployee(employeeData);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/search/employeeRecord")
    public ResponseEntity<ResponseDto> searchEmployeeData(@RequestHeader(name="token") String token, @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                          @RequestParam(value = "recordsPerPage", required = false) Integer recordsPerPage, @Valid @RequestBody EmployeeFilterDTO employeeFilterDTO)
    {
        ResponseDto filteredResponse = employeeService.searchEmployee(pageNo,recordsPerPage,employeeFilterDTO);
        return ResponseEntity.ok().body(filteredResponse);
    }

    @PostMapping("/exportToCSV")
    public ResponseEntity<byte[]> exportToCSV(@RequestHeader(name="token")String token, @Valid @RequestBody EmployeeSearchDto employeeSearchDto) throws IOException {
        byte[] csvBytes = employeeService.generateCSV(employeeSearchDto.getEmployeeList());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + generateFilename());
        return ResponseEntity.ok().headers(headers).body(csvBytes);

    }

    private String generateFilename() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return "employee_record_" + now.format(formatter) + ".csv";
    }

}



