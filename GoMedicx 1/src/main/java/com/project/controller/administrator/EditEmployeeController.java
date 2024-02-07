package com.project.controller.administrator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.dao.LoginDao;
import com.project.dao.administrator.EditEmployeeDao;
import com.project.dao.administrator.EmployeeDetailsDao;
import com.project.dao.receptionist.PatientPrescriptionDao;
import com.project.entity.Address;
import com.project.entity.Employee;
import com.project.entity.Login;
import com.project.entity.Name;

@Controller (for HTTP requests)
public class EditEmployeeController {

    // Autowiring necessary DAOs for database operations
    @Autowired
    PatientPrescriptionDao dao;
    @Autowired
    EmployeeDetailsDao dao1;
    @Autowired
    EditEmployeeDao dao2;
    @Autowired
    LoginDao infoLog;

    // Handler method for displaying the edit personal details view
    @RequestMapping(value = "/editPersonalDetailsView.html")
    public ModelAndView editView(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            Login l = (Login) session.getAttribute("userInfo");

            // Get the employee ID from the logged-in user
            String eid = l.getId();

            // Fetch employee details using the EmployeeDetailsDao
            Employee e = dao1.show(eid);
            infoLog.logActivities("returned to EditEmployeeController-editView: got= " + e);

            // Check if employee details are retrieved successfully
            if (!e.getEid().equals(null)) {
                ModelAndView mv = new ModelAndView();
                mv.setViewName("EditPersonalDetails");
                mv.addObject("prescriptionsCount", dao.prescriptionPrintCount()); // for receptionist only
                mv.addObject("employee", e);
                return mv;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            infoLog.logActivities("in EditEmployeeController-editView: " + e);
            ModelAndView mv = new ModelAndView();
            mv.setViewName("failure");
            mv.addObject("error", e);
            return mv;
        }
    }

    // Handler method for displaying the edit employee details view
    @RequestMapping(value = "/editEmployeeView.html", method = RequestMethod.POST)
    public ModelAndView view(@RequestParam("eid") String eid) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("administrator/EditEmployeeDetailsView");
        mv.addObject("employee", dao1.show(eid));
        return mv;
    }

    // Handler method for editing employee details
    @RequestMapping(value = "/editEmployee.html", method = RequestMethod.POST)
    public ModelAndView edit(@RequestParam("eid") String eid, @RequestParam("firstName") String firstName,
            @RequestParam("middleName") String middleName, @RequestParam("lastName") String lastName,
            @RequestParam("birthdate") String birthdate, @RequestParam("gender") String gender,
            @RequestParam("email") String email, @RequestParam("mobileNo") Long mobileNo,
            @RequestParam("adharNo") Long adharNo, @RequestParam("country") String country,
            @RequestParam("state") String state, @RequestParam("city") String city,
            @RequestParam("residentialAddress") String residentialAddress,
            @RequestParam("permanentAddress") String permanentAddress, @RequestParam("role") String role,
            @RequestParam("qualification") String qualification, @RequestParam("specialization") String specialization) {
        try {
            // Create Name and Address objects to represent employee's name and address
            Name n1 = new Name(firstName, middleName, lastName);
            Address a1 = new Address(residentialAddress, permanentAddress);
            infoLog.logActivities("in EditEmployeeController-edit: got= " + eid + " " + n1 + " " + birthdate + " "
                    + gender + " " + email + " " + mobileNo + " " + adharNo + " " + country + " " + state + " " + city
                    + " " + a1 + " " + role + " " + qualification + " " + specialization);

            // Call the EditEmployeeDao to perform the edit operation
            int res = dao2.edit(eid, n1, birthdate, gender, email, mobileNo, adharNo, country, state, city, a1, role,
                    qualification, specialization);
            infoLog.logActivities("returned to EditEmployeeController-edit: got= " + res);

            // Check the result of the edit operation
            if (res == 1) {
                ModelAndView mv = new ModelAndView();
                mv.setViewName("successPage");
                mv.addObject("prescriptionsCount", dao.prescriptionPrintCount()); // for receptionist only
                return mv;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            infoLog.logActivities("in EditEmployeeController-edit: " + e);
            ModelAndView mv = new ModelAndView();
            mv.setViewName("failure");
            mv.addObject("error", e);
            return mv;
        }
    }
}
