package gr.hua.dit.kvdb.kvdb.service;

import gr.hua.dit.kvdb.kvdb.domain.appointment.AppointmentStatus;
import gr.hua.dit.kvdb.kvdb.domain.request.Request;
import gr.hua.dit.kvdb.kvdb.domain.request.RequestStatus;
import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.domain.service.WorkingHour;
import gr.hua.dit.kvdb.kvdb.dto.DashboardStatsDTO;
import gr.hua.dit.kvdb.kvdb.dto.MunicipalServiceDTO;
import gr.hua.dit.kvdb.kvdb.dto.WorkingHourDTO;
import gr.hua.dit.kvdb.kvdb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    private final MunicipalServiceRepository serviceRepository;
    private final WorkingHourRepository workingHourRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    public AdminService(MunicipalServiceRepository serviceRepository,
                        WorkingHourRepository workingHourRepository,
                        RequestRepository requestRepository,
                        UserRepository userRepository,
                        AppointmentRepository appointmentRepository)
    {
        this.serviceRepository = serviceRepository;
        this.workingHourRepository = workingHourRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    //ΠΡΟΣΘΗΚΗ ΑΙΤΗΜΑΤΟΣ&ΥΠΗΡΕΣΙΑΣ//
    public MunicipalService createService(MunicipalServiceDTO dto) {
        MunicipalService service = new MunicipalService();
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setSlaDays(dto.getSlaDays());
        service.setActive(true);

        return serviceRepository.save(service);


    }

    //ΕΝΕΡΓΟΠΟΙΗΣΗ&ΑΠΕΝΕΡΓΟΠΟΙΗΣΗ ΑΙΤΗΜΑΤΟΣ&ΥΠΗΡΕΣΙΑΣ//
    //(δεν διαγραφουμε υπηρεσιες γιατι θα χαθει ιστορικο αιτηματων)//
    @Transactional
    public MunicipalService toggleServiceActive(Long servideId, boolean active) {
        MunicipalService service = serviceRepository.findById(servideId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        service.setActive(active);
        return serviceRepository.save(service);
    }

    //Ορισμος ωραριου//
    @Transactional
    public void addWorkingHour(WorkingHourDTO dto) {
        MunicipalService service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));


        WorkingHour workingHour = new WorkingHour(
                null,
                dto.getDayOfWeek(),
                dto.getStartTime(),
                dto.getEndTime(),
                service
        );
        workingHourRepository.save(workingHour);
    }

    //ΣΤΑΤΙΣΤΙΚΑ(Dashboard)//
    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {

        //ΓΕΝΙΚΑ ΣΤΑΤΙΣΤΙΚΑ//
        long totalCitizens = userRepository.count();
        long totalRequests = requestRepository.count();
        long totalAppointments = appointmentRepository.count();

        List<Request> allRequests = requestRepository.findAll();

        //STATUSES//
        long pending = allRequests.stream()
                .filter(r -> r.getStatus() == RequestStatus.SUBMITTED
                        || r.getStatus() == RequestStatus.IN_PROGRESS
                        || r.getStatus() == RequestStatus.RECEIVED)
                .count();





        long completed = allRequests.stream()
                .filter(r -> r.getStatus() == RequestStatus.COMPLETED).count();


        long rejected = allRequests.stream()
                .filter(r -> r.getStatus() == RequestStatus.REJECTED).count();

        //SLA//
        long overdue = allRequests.stream().filter(Request::isOverdue).count();

        //ΑΝΑΕΡΧΟΜΕΝΑ ΡΑΝΤΕΒΟΥ//
        long upcoming = appointmentRepository.findAll().stream()
                .filter(a -> a.getAppointmentDate().isAfter(LocalDateTime.now()))
                .filter(a -> a.getAppointmentStatus() != AppointmentStatus.CANCELLED)
                .count();



        return new DashboardStatsDTO(
                totalCitizens,
                totalRequests,
                pending,
                completed,
                rejected,
                overdue,
                totalAppointments,
                upcoming
        );

    }

    // Μέθοδος για να φέρνει όλες τις υπηρεσίες
    @Transactional(readOnly = true)
    public List<MunicipalService> getAllServices() {
        return serviceRepository.findAll(); // επιστρέφει όλες τις υπηρεσίες
    }


}
