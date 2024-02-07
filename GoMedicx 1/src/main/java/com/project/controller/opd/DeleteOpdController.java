// Define the class and specify that it is a Spring MVC Controller
@Controller
public class DeleteOpdController {

    // Autowire (inject) Data Access Objects (DAOs) for handling database operations
    @Autowired
    DeleteOpdDao dao1; // DAO for deleting OPD records

    @Autowired
    OpdDetailsDao dao2; // DAO for retrieving OPD details

    @Autowired
    PatientPrescriptionDao dao3; // DAO for patient prescriptions
	
    @Autowired
    LoginDao infoLog; // DAO for logging activities

    // Handle requests to delete a patient from the OPD queue
    @RequestMapping(value = "/deleteOpd.html", method = RequestMethod.POST)
    public ModelAndView delete(@RequestParam("pid") String pid) {
        try {
            // Log the activity with the received patient ID
            infoLog.logActivities("in DeleteOpdController-delete: got= " + pid);

            // Call the delete method in DeleteOpdDao to remove the patient from the OPD queue
            int i = dao1.delete(pid);
            
            // Log the result of the delete operation
            infoLog.logActivities("returned to DeleteOpdController-delete: got= " + i);

            // Check the result of the delete operation
            if (i == 1) {
                // If successful, create and configure ModelAndView for rendering the OPD queue view
                ModelAndView mv = new ModelAndView();
                mv.setViewName("receptionist/opdQueueView");
                mv.addObject("prescriptionsCount", dao3.prescriptionPrintCount()); // For receptionist only
                mv.addObject("opdQueue", dao2.opdQueue());
                return mv;
            } else {
                // If not successful, throw an exception to handle it
                throw new Exception();
            }
        } catch (Exception e) {
            // Log exception and redirect to a failure page with error information
            infoLog.logActivities("in DeleteOpdController-delete: " + e);
            ModelAndView mv = new ModelAndView();
            mv.setViewName("failure");
            mv.addObject("error", e);
            return mv;
        }
    }
}
