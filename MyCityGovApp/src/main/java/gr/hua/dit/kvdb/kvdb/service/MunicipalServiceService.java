package gr.hua.dit.kvdb.kvdb.service;

import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.dto.MunicipalServiceDTO;
import gr.hua.dit.kvdb.kvdb.repository.MunicipalServiceRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipalServiceService {

    private final MunicipalServiceRepository repository;

    public MunicipalServiceService(MunicipalServiceRepository repository) {
        this.repository = repository;
    }

    public MunicipalService create(MunicipalServiceDTO dto) {
        MunicipalService service = new MunicipalService();
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setSlaDays(dto.getSlaDays());
        service.setActive(true);
        return repository.save(service);
    }

    public List<MunicipalService> findAll() {
        return repository.findAll();
    }

    //δείχνει μόνο τις active υπηρεσίες στον πολίτη
    public List<MunicipalService> getActiveServicesForCitizens() {
        return repository.findByActiveTrue();
    }

}
