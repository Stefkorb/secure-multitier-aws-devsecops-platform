package gr.hua.dit.kvdb.kvdb.repository;

import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.domain.service.WorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface MunicipalServiceRepository
        extends JpaRepository<MunicipalService, Long> {



    Optional<MunicipalService> findByName(String name);
    List<MunicipalService> findByActiveTrue();




}

