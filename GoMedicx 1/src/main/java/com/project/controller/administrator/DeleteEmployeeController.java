package com.project.controller.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.dao.LoginDao;
import com.project.dao.administrator.DeleteEmployeeDao;

/**
 * Controller for handling employee deletion operations within the administrator module.
  
 
 * The @Controller part is like telling the computer, "Hey, this class (EmployeeController) 
 * is the one that knows how to manage and handle requests on the website."
 */
@Controller
public class DeleteEmployeeController {

    // Autowiring DAOs for dependency injection
    @Autowired
    DeleteEmployeeDao dao;

    @Autowired
    LoginDao infoLog;

    /**
     * Handles the HTTP request to delete an employee.
     *
     * @param eid Employee ID passed as a request parameter.
     * @return mv i.e. ModelAndView indicating success or failure of the operation.
     */
    @RequestMapping("/deleteEmployee.html")
    public ModelAndView delete(@RequestParam("eid") String eid) {
        try {

            // infoLog.logActivities(): Record/Log the received employee ID for debugging purposes.

            // In simpler terms, it's like the program saying, "Hey, I just received an employee ID in 
            // the DeleteEmployeeController's delete() method, and I'm noting it down so that if there's 
            // any problem or if a developer wants to understand what's happening, they can look at these
            //  notes." It's a way to keep track of what's going on behind the scenes.

            // + eid adds the actual employee ID to the message. It's like saying,
            //  "I got this employee ID: [actual ID]."

            infoLog.logActivities("in DeleteEmployeeController-delete: got= " + eid);

            // Attempt to delete the employee using the DAO
            int res = dao.delete(eid);
            
            // Log the result of the deletion operation
            // "returned to DeleteEmployeeController-delete: got= " is a message indicating that the 
            // program has completed a certain action (deleting an employee in this case) and is now 
            // noting down the result.

            // + res adds the actual result (a number indicating how many employees were deleted) to the msg. 
            // It's like saying, "After trying to delete an employee, here's what happened: [actual result]."
           
           // In simpler terms, it's similar to the program saying, "Okay, I tried to delete an employee,
           // and here's the result. I deleted [number of employees], and I'm letting you know so that 
           // you can understand if everything went smoothly or if there were any issues." It's a way to keep 
           // track of the program's actions and outcomes.
            infoLog.logActivities("returned to DeleteEmployeeController-delete: got= " + res);

            // Check the result and redirect to an appropriate view
            if (res == 1) {
                ModelAndView mv = new ModelAndView();
                mv.setViewName("successPage");
                return mv;
            } else {
                // If deletion is not successful, throw an exception
                throw new Exception();
            }
        } catch (Exception e) {
            // Log the exception and redirect to a failure page with error information
            infoLog.logActivities("in DeleteEmployeeController-delete: " + e);
            ModelAndView mv = new ModelAndView();
            mv.setViewName("failure");
            mv.addObject("error", e);
            return mv;
            // Handling the exception: If there was an exception (something unexpected occurred), catch 
                                    // that exception and do the following: 
                                    //  1. Log the details of the exception. This is like writing down a note saying, "Something 
                                    // went wrong, and here are the details." 
                                    
                                    // 2. Create a special page (a ModelAndView) named "failure." 
                                    
                                    // 3. Add information about the error to this page. Return the failure 
                                    // page, essentially saying, "There was a problem. Show the failure 
                                    // page with details about what went wrong."
        }
    }
}
