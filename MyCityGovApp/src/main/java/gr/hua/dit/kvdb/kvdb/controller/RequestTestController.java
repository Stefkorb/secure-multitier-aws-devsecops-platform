package gr.hua.dit.kvdb.kvdb.controller;

import gr.hua.dit.kvdb.kvdb.dto.RequestDTO;
import gr.hua.dit.kvdb.kvdb.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test/requests")
public class RequestTestController {

    private final RequestService requestService;

    public RequestTestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public RequestDTO createRequest(@RequestBody @Valid RequestDTO dto) {
        return requestService.createRequest(dto);
    }

    @GetMapping("/citizen/{citizenId}")
    //κρατάει το session ανοιχτο όσο τρέχει η μέθοδος
    @Transactional
    public List<RequestDTO> getRequestsForCitizen(@PathVariable Long citizenId) {
        return requestService.getRequestsForCitizen(citizenId);
    }
}

