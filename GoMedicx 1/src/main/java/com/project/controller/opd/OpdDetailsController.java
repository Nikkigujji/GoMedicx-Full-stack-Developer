// Import necessary packages and classes
package com.project.controller.opd;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.project.dao.LoginDao;
import com.project.dao.opd.OpdDetailsDao;
import com.project.dao.receptionist.PatientPrescriptionDao;
import com.project.entity._OpdRecord;

/**
 * This is a controller class responsible for handling requests related to the display of the OPD (Outpatient Department) queue view.
 */
@Controller
public class OpdDetailsController {

    // Autowire (inject) Data Access Objects (DAOs) for handling database operations
    @Autowired
    OpdDetailsDao dao; // DAO for retrieving OPD details
    
    @Autowired
    PatientPrescriptionDao dao1; // DAO for patient prescriptions
    
    @Autowired
    LoginDao infoLog; // DAO for logging activities

    /**
     * Handles requests to view the OPD queue.
     *
     * @return ModelAndView: An object that holds both the model and view information for rendering the OPD queue view.
     */
    @RequestMapping(value = "/opdQueueView.html", method = RequestMethod.GET)
    public ModelAndView view() {
        try {
            // record/Log the activity
            infoLog.logActivities("in OpdDetailsController-view:");

            // Retrieve the OPD queue from the database
            ArrayList<_OpdRecord> opdQ = dao.opdQueue();
            
            // Save/Log the retrieved OPD records
            infoLog.logActivities("returned to OpdDetailsController-view: got= ");
            for (_OpdRecord r : opdQ) {
                infoLog.logActivities("" + r);
            }

            // Check if the retrieved OPD queue is not null
            if (!opdQ.equals(null)) {
                // Create and configure the ModelAndView for rendering the OPD queue view
                ModelAndView mv = new ModelAndView();
                mv.setViewName("receptionist/opdQueueView");
                mv.addObject("opdQueue", opdQ);
                mv.addObject("prescriptionsCount", dao1.prescriptionPrintCount()); // For receptionist only
                return mv;
            } else {
                // If the retrieved OPD queue is null, throw an exception to handle it
                throw new Exception();
            }
        } catch (Exception e) {
            // Log the exception and redirect to a failure page with error information
            infoLog.logActivities("in OpdDetailsController-view: " + e);
            ModelAndView mv = new ModelAndView();
            mv.setViewName("failure");
            mv.addObject("error", e);
            return mv;
        }
    }
}
