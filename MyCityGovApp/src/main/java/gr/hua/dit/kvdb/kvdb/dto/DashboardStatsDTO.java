package gr.hua.dit.kvdb.kvdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDTO {

    //ΓΕΝΙΚΑ ΣΤΟΙΧΕΙΑ
    private long totalCitizens;

    // ΑΙΤΗΜΑΤΑ (REQUESTS)
    private long totalRequests;
    private long pendingRequests;
    private long completedRequests;
    private long rejectedRequests;

    //SLA METRICS
    private long overdueRequests;

    //ΡΑΝΤΕΒΟΥ (APPOINTMENTS)
    private long totalAppointments;
    private long upcomingAppointments;


}