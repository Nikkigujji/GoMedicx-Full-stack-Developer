package com.project.controller.administrator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.dao.LoginDao;
import com.project.dao.administrator.AddEmployeeDao;
import com.project.entity.Address;
import com.project.entity.Employee;
import com.project.entity.Name;

/**
 * Controller for handling employee-related operations within the administrator module.
 */
@Controller
public class AddEmployeeController {

    // Autowiring DAOs for dependency injection  

	// Without Dependency Injection (DI): In a traditional approach, if a class needs to use another 
	//                                    class (a dependency), it usually creates an instance of that 
    //                                    class within itself. For example, it might have statements like 
    //                                    AddEmployeeDao dao = new AddEmployeeDao(); 
	//                                    LoginDao infoLog = new LoginDao(); inside the class.
    @Autowired
    AddEmployeeDao dao; // dao : data access object 
	// Autowired : Dependency injection allows you to "inject" those dependencies(instances) from the 
	// outside.

    @Autowired
    LoginDao infoLog;

    /**
     * Displays the Add Employee View.
     *
     * @return ModelAndView for displaying/rendering the Add Employee View.
     */

    @RequestMapping("/addEmployeeView.html")

    // public ModelAndView view(): This is the method signature. It returns a 
    //                             object of ModelAndView class, which is used to store both the data 
    //                             to be displayed and the view name (the HTML page to render).
    public ModelAndView view() {
        try {
            // Create and configure the ModelAndView for rendering the view
            ModelAndView mv = new ModelAndView();
            mv.setViewName("administrator/AddEmployeeView");
            return mv;
        } catch (Exception e) {
            // Record/Log and redirect to a failure page with error information
            infoLog.logActivities("in AddEmployeeController-view: " + e);
            ModelAndView mv = new ModelAndView();
            mv.setViewName("failure");
            mv.addObject("error", e);
            return mv;
        }
    }

    /**
     * Processes the form submission for adding an employee.
     *
     * @param firstName          First name of the employee.
     * @param middleName         Middle name of the employee.
     * @param lastName           Last name of the employee.
     * @param birthdate          Birthdate of the employee.
     * @param gender             Gender of the employee.
     * @param email              Email of the employee.
     * @param mobileNo           Mobile number of the employee.
     * @param adharNo            Aadhar number of the employee.
     * @param country            Country of residence of the employee.
     * @param state              State of residence of the employee.
     * @param city               City of residence of the employee.
     * @param residentialAddress Residential address of the employee.
     * @param permanentAddress   Permanent address of the employee.
     * @param role               Role of the employee.
     * @param qualification      Qualification of the employee.
     * @param specialization     Specialization of the employee.
     * @return ModelAndView indicating success or failure of the operation.
     */
    @RequestMapping(value = "/addEmployee.html", method = RequestMethod.POST)
    public ModelAndView add(
            @RequestParam("firstName") String firstName,
            @RequestParam("middleName") String middleName,
            @RequestParam("lastName") String lastName,
            @RequestParam("birthdate") String birthdate,
            @RequestParam("gender") String gender,
            @RequestParam("email") String email,
            @RequestParam("mobileNo") Long mobileNo,
            @RequestParam("adharNo") Long adharNo,
            @RequestParam("country") String country,
            @RequestParam("state") String state,
            @RequestParam("city") String city,
            @RequestParam("residentialAddress") String residentialAddress,
            @RequestParam("permanentAddress") String permanentAddress,
            @RequestParam("role") String role,
            @RequestParam("qualification") String qualification,
            @RequestParam("specialization") String specialization) {
        try {
            // Create Name and Address objects for the Employee
            Name n1 = new Name(firstName, middleName, lastName);
            Address a1 = new Address(residentialAddress, permanentAddress);

            // Log the received data for debugging purposes
            infoLog.logActivities("in AddEmployeeController-add: got= " + n1 + " " + birthdate + " " + gender + " "
                    + email + " " + mobileNo + " " + adharNo + " " + country + " " + state + " " + city + " " + a1
                    + " " + role + " " + qualification + " " + specialization);

            // Create Employee object
            Employee e1 = new Employee(null, n1, birthdate, gender, email, mobileNo, adharNo, country, state, city, a1,
                    role, qualification, specialization);

            // Add the employee using the DAO
            boolean b = dao.add(e1);
            infoLog.logActivities("returned to AddEmployeeController-add: got=" + b);

            // Check the result and redirect to an appropriate view
            if (b) {
                ModelAndView mv = new ModelAndView();
                mv.setViewName("successPage");
                return mv;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            // Log exception and redirect to a failure page with error information
            infoLog.logActivities("in AddEmployeeController-add: " + e);
            ModelAndView mv = new ModelAndView();
            mv.setViewName("failure");
            mv.addObject("error", e);
            return mv;
        }
    }
}
