package com.project.controller.opd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.dao.LoginDao;
import com.project.dao.opd.AddOpdDao;
import com.project.dao.receptionist.PatientPrescriptionDao;
import com.project.entity.Opd;

@Controller
public class AddOpdController {

    @Autowired
    AddOpdDao dao;

    @Autowired
    PatientPrescriptionDao dao1;

    @Autowired
    LoginDao infoLog;

    /**
     * Handles the addition of a patient to the OPD queue.
     *
     * @param pid The patient ID.
     * @return ModelAndView indicating success or failure of the operation.
     */
    @RequestMapping(value = "/addOpd.html", method = RequestMethod.POST)
    public ModelAndView add(@RequestParam("pid") String pid) {
        try {
            // Log the activity with the received patient ID
            infoLog.logActivities("in AddOpdController-add: got= " + pid);

            // Get the doctor ID for the patient
            String doctorid = dao.getDoctorId(pid);
            infoLog.logActivities("returned to AddOpdController-add: got= " + doctorid);

            // Check if doctor ID is found
            if (!doctorid.equals(null)) {
                // Create Opd object with PENDING status
                Opd q1 = new Opd(pid, doctorid, Opd.PENDING);

                // Add the patient to the OPD queue using AddOpdDao
                int b = dao.add(q1);
                infoLog.logActivities("returned to AddOpdController-add: got= " + b);

                // Check the result and redirect to an appropriate view
                if (b == 1) {
                    ModelAndView mv = new ModelAndView();
                    mv.setViewName("successPage");
                    mv.addObject("prescriptionsCount", dao1.prescriptionPrintCount()); // for receptionist only
                    return mv;
                } else if (b == 2) {
                    // Patient is already added to the OPD queue
                    infoLog.logActivities("in AddOpdController-add: ");
                    ModelAndView mv = new ModelAndView();
                    mv.setViewName("failure");
                    mv.addObject("error", "<b>patient is already added in OPD queue</b>");
                    return mv;
                } else if (b == 3) {
                    // Assigned doctor is not available, prompt to choose another doctor
                    infoLog.logActivities("in AddOpdController-add: ");
                    ModelAndView mv = new ModelAndView();
                    mv.setViewName("failure");
                    mv.addObject("error", "<b>Your assigned doctor is not available...plz choose another doctor and then try again</b>");
                    return mv;
                } else {
                    // Unknown error occurred
                    throw new Exception();
                }
            } else {
                // Doctor ID not found for the patient
                throw new Exception();
            }
        } catch (Exception e) {
            // Log exception and redirect to a failure page with error information
            infoLog.logActivities("in AddOpdController-add: " + e);
            ModelAndView mv = new ModelAndView();
            mv.setViewName("failure");
            mv.addObject("error", e);
            return mv;
        }
    }
}
