package gr.hua.dit.kvdb.kvdb.repository;

import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.domain.service.WorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;


public interface WorkingHourRepository extends JpaRepository<WorkingHour, Long> {

    List<WorkingHour> findByMunicipalServiceId(Long serviceId);
    List<WorkingHour> findByMunicipalServiceAndDayOfWeek(MunicipalService service, DayOfWeek dayOfWeek);

}
