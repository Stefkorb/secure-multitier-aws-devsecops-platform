package gr.hua.dit.kvdb.kvdb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {

    private Long id;
    private String protocolNumber;
    private String citizenUsername;
    private String status;
    private int slaDays;

    private Long citizenId;
    @NotBlank
    private String title;
    private String description;
    private String comments;
    private String resolutionComments;

    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime submittedAt;
    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdatedAt;

    @NotNull
    private Long serviceId;

    //!
    @Getter
    @Setter
    private boolean overdue;

    private String attachedFileName;
    private String fileUrl;

//    private String attachedFileName;  // Αν υπάρχει επισύναψη
//    private String fileUrl;           // URL για download
//    private String employeeComments;  // Σχόλια υπαλλήλου

}
