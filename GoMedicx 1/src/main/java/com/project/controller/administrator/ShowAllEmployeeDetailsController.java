package com.project.controller.administrator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.dao.LoginDao;
import com.project.dao.administrator.AllEmployeeDetailsDao;
import com.project.dao.administrator.EmployeeDetailsDao;
import com.project.entity.Employee;

@Controller
public class ShowAllEmployeeDetailsController 
{
    // Autowire DAOs for dependency injection
    @Autowired
    AllEmployeeDetailsDao dao1;
    
    @Autowired
    EmployeeDetailsDao dao2;
    
    @Autowired
    LoginDao infoLog;

    // Handle requests to view all employee details
    @RequestMapping("/allEmployeesView.html")
	// Return Type (ModelAndView): This specifies the type of value 
	// that the method will return. In this case, the method returns 
	// an object of type ModelAndView. A ModelAndView object is commonly 
	// used in Spring MVC to encapsulate both the model data and the view name.
    public ModelAndView view()    
    {
        // Create a new ModelAndView object to manage the response
        ModelAndView mv = new ModelAndView();
        
        // Set the view name to "administrator/AllEmployeeDetailsView"
        mv.setViewName("administrator/AllEmployeeDetailsView");
        
        // Add the list of all employees to the response model
        mv.addObject("employees", dao1.getAllEmployees());
        
        // Return the ModelAndView object
        return mv;
    }

    // Handle requests to view details of a specific employee
    @RequestMapping(value = "/viewEmployee.html", method = RequestMethod.POST)
    public ModelAndView showEmployeeDetailsViewMethod(@RequestParam("eid")String eid)
    {
        try {
            // Log the activity with the received employee ID
            infoLog.logActivities("in ShowAllEmployeeDetailsController-showEmployeeDetailsViewMethod: got "+eid);
            
            // Retrieve employee details using the EmployeeDetailsDao
            Employee l=(Employee) dao2.show(eid);
            
            // Check if the employee is found
            if(! l.equals(null))
            {
                // Create a new ModelAndView object for handling the response
                ModelAndView mv = new ModelAndView();
                
                // Set the view name to "administrator/EmployeeDetailsView"
                mv.setViewName("administrator/EmployeeDetailsView");
                
                // Add the found employee details to the response model
                mv.addObject("employee",l);
                
                // Return the ModelAndView object
                return mv;
            }
            else
            {
                // If no employee is found, throw an exception
                throw new Exception();
            }
        }
        catch(Exception e)
        {
            // Log exception and redirect to a failure page with error information
            infoLog.logActivities("in ShowAllEmployeeDetailsController-showEmployeeDetailsViewMethod: "+e);
            ModelAndView mv= new ModelAndView();
            mv.setViewName("failure");
            mv.addObject("error",e);
            return mv;
        }
    }
}
