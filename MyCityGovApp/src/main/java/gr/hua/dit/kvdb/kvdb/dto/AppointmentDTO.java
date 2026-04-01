package gr.hua.dit.kvdb.kvdb.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentDTO {

    private Long id;
    private Long citizenId;
    private String citizenName;
    private Long serviceId;
    private String serviceName;
    private LocalDateTime appointmentDate;
    private String appointmentStatus;


}
