package gr.hua.dit.kvdb.kvdb.controller;


import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.dto.MunicipalServiceDTO;
import gr.hua.dit.kvdb.kvdb.service.MunicipalServiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test/admin/services")
public class AdminServiceController {

    private final MunicipalServiceService service;

    public AdminServiceController(MunicipalServiceService service) {
        this.service = service;
    }

    @PostMapping
    public MunicipalService create(@RequestBody MunicipalServiceDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<MunicipalService> all() {
        return service.findAll();
    }
}

