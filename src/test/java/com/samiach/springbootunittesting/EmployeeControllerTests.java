package com.samiach.springbootunittesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samiach.springbootunittesting.model.Employee;
import com.samiach.springbootunittesting.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * JUNIT TEST FOR 'CREATE EMPLOYEE' REST API CALL
     * @throws Exception
     */
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // GIVEN - PRECONDITION OR SETUP
        Employee employee = Employee.builder()
                .firstName("Chasith")
                .lastName("Samarasena")
                .email("Chasith.Samarasena@gmail.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // WHEN - ACTION OR BEHAVIOUR THAT WE ARE GOING TO TEST
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // THEN - VERIFY THE RESULT OR OUTPUT USING ASSERT STATEMENTS
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    /**
     * JUNIT TEST FOR 'GET ALL EMPLOYEES' REST API CALL
     *
     * @throws Exception
     */
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        // GIVEN - PRECONDITION OR SETUP
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Chasith").lastName("Samarasena").email("Chasith.Samarasena@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        // WHEN - ACTION OR BEHAVIOUR THAT WE ARE GOING TO TEST
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // THEN - VERIFY THE OUTPUT
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    /**
     * JUNIT TEST FOR 'GET EMPLOYEE BY ID' REST API CALL [POSITIVE SCENARIO - VALID EMPLOYEE ID]
     *
     * @throws Exception
     */
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // GIVEN - PRECONDITION OR SETUP
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Chasith")
                .lastName("Samarasena")
                .email("Chasith.Samarasena@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        // WHEN - ACTION OR BEHAVIOUR THAT WE ARE GOING TO TEST
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // THEN - VERIFY THE OUTPUT
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    /**
     * JUNIT TEST FOR 'GET EMPLOYEE BY ID' REST API CALL [NEGATIVE SCENARIO - VALID EMPLOYEE ID]
     *
     * @throws Exception
     */
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        // GIVEN - PRECONDITION OR SETUP
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Chasith")
                .lastName("Samarasena")
                .email("Chasith.Samarasena@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        // WHEN - ACTION OR BEHAVIOUR THAT WE ARE GOING TO TEST
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // THEN - VERIFY THE OUTPUT
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * JUNIT TEST FOR 'UPDATE EMPLOYEE' REST API CALL [POSITIVE SCENARIO]
     *
     * @throws Exception
     */
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        // GIVEN - PRECONDITION OR SETUP
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Chasith")
                .lastName("Samarasena")
                .email("Chasith.Samarasena@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Induruwe Acharige")
                .lastName("Chasith Hasarel Samarasena")
                .email("samarasena.iach@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // WHEN - ACTION OR BEHAVIOUR THAT WE ARE GOING TO TEST
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // THEN - VERIFY THE OUTPUT
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    /**
     * JUNIT TEST FOR 'UPDATE EMPLOYEE' REST API CALL [NEGATIVE SCENARIO]
     *
     * @throws Exception
     */
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        // GIVEN - PRECONDITION OR SETUP
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Chasith")
                .lastName("Samarasena")
                .email("Chasith.Samarasena@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Induruwe Acharige")
                .lastName("Chasith Hasarel Samarasena")
                .email("samarasena.iach@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // WHEN - ACTION OR BEHAVIOUR THAT WE ARE GOING TO TEST
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // THEN - VERIFY THE OUTPUT
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * JUNIT TEST FOR 'DELETE EMPLOYEE' REST API CALL
     *
     * @throws Exception
     */
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // GIVEN - PRECONDITION OR SETUP
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // WHEN - ACTION OR BEHAVIOUR THAT WE ARE GOING TO TEST
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        // THEN - VERIFY THE OUTPUT
        response.andExpect(status().isOk())
                .andDo(print());
    }

}
