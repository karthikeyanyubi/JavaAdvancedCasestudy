package com.employee_service.service;


import com.employee_service.dto.ResponseDto;
import com.employee_service.model.Employee;
import com.employee_service.model.EmployeeFilterDTO;
import com.employee_service.repository.EmployeeRepository;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private CSVParser csvParser;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager entityManager;

    public ResponseDto saveEmployeeData(MultipartFile file) throws CsvValidationException, IOException {
      List<Employee> employeeList = csvParser.readCsv(file);
      employeeRepository.saveAll(employeeList);
      String message= "File uploaded and data saved successfully";
      ResponseDto response = ResponseDto.builder().message(message).build();
      return response;
    }

    @Override
    public ResponseDto fetchEmployee(Integer employeeId) {
        Optional<Employee> fetchedEmployee = employeeRepository.findByEmployeeId(employeeId);
        if(fetchedEmployee.isEmpty())
        {
            throw new RuntimeException("Employee details not found");
        }
        else
        {

            Employee employeeResult = fetchedEmployee.get();
            String message= "Employee details fetched successfully";
            ResponseDto result = ResponseDto.builder().employee(List.of(employeeResult)).message(message).build();
            return result;
        }
    }

    @Override
    public ResponseDto modifyEmployee(Employee employeeData) {
        Optional<Employee> fetchedEmployee = employeeRepository.findById(employeeData.getEmployeeId());
        if(fetchedEmployee.isEmpty())
        {
            throw new RuntimeException("Employee details not found");
        }
        else
        {
            Employee employeeResult = fetchedEmployee.get();
            employeeResult.setFirstName(employeeData.getFirstName());
            employeeResult.setLastName(employeeData.getLastName());
            employeeResult.setDateOfJoining(employeeData.getDateOfJoining());
            if(employeeData.getGrade()!=null && !employeeData.getGrade().isBlank())
                employeeResult.setGrade(employeeData.getGrade());
            if(employeeData.getDateOfBirth()!=null)
                employeeResult.setDateOfBirth(employeeData.getDateOfBirth());
            Employee modifiedEmployee = employeeRepository.save(employeeResult);
            String message= "Employee details modified successfully";
            ResponseDto result = ResponseDto.builder().employee(List.of(modifiedEmployee)).message(message).build();
            return result;
        }
    }

    @Override
    public ResponseDto searchEmployee(Integer pageNo, Integer pageSize, EmployeeFilterDTO filterDTO) {
        if (filterDTO == null || (filterDTO.getFirstName() == null && filterDTO.getLastName() == null && filterDTO.getEmployeeId() == null)) {
            throw new IllegalArgumentException("At least one of the following fields is required: firstName, lastName, employeeId");
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);

        // Predicates
        Predicate predicate = criteriaBuilder.conjunction();

        if (filterDTO.getEmployeeId() != null) {
            Predicate  employeeIdPredicate = criteriaBuilder.equal(root.get("employeeId"), filterDTO.getEmployeeId());
            predicate = criteriaBuilder.and(predicate, employeeIdPredicate);
        }

        if (filterDTO.getFirstName() != null && !filterDTO.getFirstName().isEmpty()) {
            Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), filterDTO.getFirstName().toLowerCase() + "%");
            predicate = criteriaBuilder.and(predicate, firstNamePredicate);
        }


        if (filterDTO.getLastName() != null && !filterDTO.getLastName().isEmpty()) {
            Predicate lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), filterDTO.getLastName().toLowerCase() + "%");
            predicate = criteriaBuilder.and(predicate, lastNamePredicate);
        }


        if (filterDTO.getGrade() != null && !filterDTO.getGrade().isEmpty()) {
            Predicate gradePredicate = criteriaBuilder.equal(root.get("grade"), filterDTO.getGrade());
            predicate = criteriaBuilder.and(predicate, gradePredicate);
        }

        if (filterDTO.getDojCondition() != null && filterDTO.getDateOfJoining() != null) {
            Predicate dojPredicate = null;
            LocalDate doj = filterDTO.getDateOfJoining();
            switch (filterDTO.getDojCondition()) {
                case "gt":
                    dojPredicate = criteriaBuilder.greaterThan(root.get("dateOfJoining"), doj);
                    break;
                case "eq":
                    dojPredicate = criteriaBuilder.equal(root.get("dateOfJoining"), doj);
                    break;
                case "lt":
                    dojPredicate = criteriaBuilder.lessThan(root.get("dateOfJoining"), doj);
                    break;
            }
            if (dojPredicate != null) {
                predicate = criteriaBuilder.and(predicate, dojPredicate);
            }
        }

        if (filterDTO.getDobCondition() != null && filterDTO.getDateOfBirth() != null) {
            Predicate dobPredicate = null;
            LocalDate dob = filterDTO.getDateOfBirth();
            switch (filterDTO.getDobCondition()) {
                case "gt":
                    dobPredicate = criteriaBuilder.greaterThan(root.get("dateOfBirth"), dob);
                    break;
                case "eq":
                    dobPredicate = criteriaBuilder.equal(root.get("dateOfBirth"), dob);
                    break;
                case "lt":
                    dobPredicate = criteriaBuilder.lessThan(root.get("dateOfBirth"), dob);
                    break;
            }
            if (dobPredicate != null) {
                predicate = criteriaBuilder.and(predicate, dobPredicate);
            }
        }

        criteriaQuery.where(predicate);

        // Order by employeeId
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("employeeId")));

        TypedQuery<Employee> query = entityManager.createQuery(criteriaQuery);

        // Pagination
        if (pageNo != null && pageSize != null) {
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            query.setFirstResult((int) pageable.getOffset());;
            query.setMaxResults(pageSize);
            Integer total = query.getResultList().size();

            List<Employee> resultList = query.getResultList();
            return ResponseDto.builder()
                    .totalRecords((int) total)
                    .employee(resultList)
                    .message("Filtered employees fetched successfully")
                    .build();
        } else {
            List<Employee> result = query.getResultList();
            return ResponseDto.builder()
                    .totalRecords((int) result.size())
                    .employee(result)
                    .message("Filtered employees fetched successfully")
                    .build();
        }
    }

    @Override
    public byte[] generateCSV(List<Employee> employees) throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(getHeaders()));
        for (Employee employee : employees) {
            csvPrinter.printRecord(
                    employee.getEmployeeId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getDateOfBirth(),
                    employee.getDateOfJoining(),
                    employee.getGrade()
            );
        }
        csvPrinter.flush();
        csvPrinter.close();
        return writer.toString().getBytes();
    }

    private String[] getHeaders() {
        Field[] fields = Employee.class.getDeclaredFields();
        String[] headers = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            headers[i] = fields[i].getName();
        }
        return headers;
    }
}
