package gr.hua.dit.kvdb.kvdb.controller;

import gr.hua.dit.kvdb.kvdb.domain.service.MunicipalService;
import gr.hua.dit.kvdb.kvdb.domain.service.WorkingHour;
import gr.hua.dit.kvdb.kvdb.dto.DashboardStatsDTO;
import gr.hua.dit.kvdb.kvdb.dto.MunicipalServiceDTO;
import gr.hua.dit.kvdb.kvdb.dto.WorkingHourDTO;
import gr.hua.dit.kvdb.kvdb.service.AdminService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")

public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    //ΔΗΜΙΟΥΡΓΙΑ ΥΠΗΡΕΣΙΑΣ
    @PostMapping("/services")
    public ResponseEntity<MunicipalService> createService(@RequestBody MunicipalServiceDTO dto) {
        return ResponseEntity.ok(adminService.createService(dto));
    }

    //ΕΝΕΡΓΟΠΟΙΗΣΗ&ΑΠΕΝΕΡΓΟΠΟΙΗΣΗ
    @PutMapping("/services/{id}/activation")
    public ResponseEntity<MunicipalService> toggleService(@PathVariable Long id,@RequestParam boolean active) {
        return ResponseEntity.ok(adminService.toggleServiceActive(id, active));
    }

    //ΟΡΙΣΜΟΣ ΟΡΑΡΙΟΥ
    @PostMapping("/working-hours")
    public ResponseEntity<String> addWorkingHour(@RequestBody WorkingHourDTO dto) {
        adminService.addWorkingHour(dto);
        return ResponseEntity.ok("Working hour added successfully");
    }

    //ΠΡΟΒΟΛΗ ΣΤΑΤΙΣΤΙΚΩΝ
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/services")
    public ResponseEntity<List<MunicipalService>> getAllServices() {
        List<MunicipalService> services = adminService.getAllServices();
        return ResponseEntity.ok(services);
    }


}
