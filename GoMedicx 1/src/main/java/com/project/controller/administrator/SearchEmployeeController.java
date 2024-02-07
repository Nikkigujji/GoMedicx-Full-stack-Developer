package com.project.controller.administrator;

import java.io.FileOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.dao.LoginDao;
import com.project.dao.administrator.SearchEmployeeDao;
import com.project.entity.Employee;

@Controller
public class SearchEmployeeController {
    
    @Autowired
    SearchEmployeeDao dao;
    
    @Autowired
    LoginDao infoLog;

    // Handle requests to view the search page
    @RequestMapping("/searchEmployeeView.html")
    public ModelAndView view() {
        // Create a new ModelAndView object to manage the response

		// ModelAndView is a class that combines both the model data and the view information. 
		// It is used to pass data from a controller to a view and specify which view should be rendered.
        ModelAndView mv = new ModelAndView(); 
        
		// View Configuration: Creates a ModelAndView object (mv) to manage the response.
		// Sets the view name to "administrator/SearchEmployeeView", specifying the view page 
		// to be rendered.
        mv.setViewName("administrator/SearchEmployeeView");
        
        // Model Data: Adds a "status" attribute to the response model, set to "true". 
		// This can be used in the view page.
        mv.addObject("status", "true");
        
        // Return the ModelAndView object
        return mv;
    }

    // Handle requests to search employees by first and last name
    @RequestMapping(value = "/searchEmployeeByName.html", method = RequestMethod.POST)
    public ModelAndView searchName(
            @RequestParam("firstName") String firstName, 
            @RequestParam("lastName") String lastName) {
        
        // Search for employee by name using the SearchEmployeeDao
        Employee e1 = dao.searchName(firstName, lastName);
        
        // Log the activity using the LoginDao
        infoLog.logActivities("searchName: " + e1);

        try {
            // If an employee is found, prepare to show details
            if (e1.getEid() != null) {
                // Create a new ModelAndView object for handling the response
                ModelAndView mv = new ModelAndView();
                
                // Set the view name to "administrator/EmployeeDetailsView"
                mv.setViewName("administrator/EmployeeDetailsView");
                
                // Add the found employee details to the response model
                mv.addObject("employee", e1);
                
                // Return the ModelAndView object
                return mv;
            }
        } catch (NullPointerException e) {
            // If no employee is found, handle it gracefully
            infoLog.logActivities("No employee found: " + e);
            
            // Create a new ModelAndView object for handling the response
            ModelAndView mv = new ModelAndView();
            
            // Set the view name to "administrator/SearchEmployeeView"
            mv.setViewName("administrator/SearchEmployeeView");
            
            // Add a status attribute to the response model indicating no employee found
            mv.addObject("status", "false");
            
            // Return the ModelAndView object
            return mv;
        }
        return null;  // This should be avoided; consider returning an appropriate response
    }

    // Similar methods for searching employees by ID, mobile number, and Aadhar number
    // ...

}
