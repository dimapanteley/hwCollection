package com.example.hwcollectionz;
import exceptions.EmployeeStorageIsFullException;
import exceptions.EmployeeNotFoundException;
import exceptions.EmployeeAlreadyAddedException;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = "/add")
    public String addEmployee(@RequestParam("firstName") String firstName,
                              @RequestParam("lastName") String lastName
    ) {

        Employee employee = new Employee(firstName, lastName);
        try {
            employeeService.add(employee);
        } catch (EmployeeStorageIsFullException e) {
            throw new RuntimeException("Коллекция сотрудников переполнена");
        } catch (EmployeeAlreadyAddedException e) {
            throw new RuntimeException("В коллекции уже есть такой сотрудник");
        }
        return "сотрудник " + firstName + " " + lastName + " добавлен";
    }

    @GetMapping(path = "/find")
    public String findEmployee(@RequestParam(value = "firstName") String firstName,
                               @RequestParam(value = "lastName") String lastName
    ) {
        Employee employee = new Employee(firstName, lastName);
        try {
            employeeService.find(employee);
        } catch (EmployeeNotFoundException e) {
            throw new RuntimeException("\u001B[38;2;255;165;0m Сотрудников с таким именем не найдено. \u001B[0m");

        }
        return "Сотрудник найден: " + employeeService.find(employee);
    }

    @GetMapping(path = "/remove")
    public String removeEmployee(@RequestParam("firstName") String firstName,
                                 @RequestParam("lastName") String lastName
    ) {
        Employee employee = new Employee(firstName, lastName);
        try {
            employeeService.remove(employee);
        } catch (EmployeeNotFoundException e) {
            throw new RuntimeException("Удаление не выполнено, поскольку такого сотрудника не существует");
        }
        return "сотрудник " + employee.getFirstName() + " " + employee.getLastName() + " удален";
    }

    @GetMapping("/allEmployees")
    public String allEmployees(){
        return employeeService.getEmployeeList().toString();
    }
}