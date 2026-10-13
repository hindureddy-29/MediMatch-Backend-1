package com.medimatch.controller;

import com.medimatch.dto.ApiResponse;
import com.medimatch.dto.MedicineRequest;
import com.medimatch.dto.MedicineResponse;
import com.medimatch.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    @Autowired
    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicineResponse>>> getAllMedicines(
            @RequestParam(value = "search", required = false) String search) {
        List<MedicineResponse> list = medicineService.searchMedicines(search);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicineResponse>> getMedicineById(@PathVariable Long id) {
        MedicineResponse res = medicineService.getMedicineById(id);
        return ResponseEntity.ok(ApiResponse.ok(res));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MedicineResponse>> createMedicine(@Valid @RequestBody MedicineRequest req) {
        MedicineResponse created = medicineService.createMedicine(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Medicine created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicineResponse>> updateMedicine(
            @PathVariable Long id,
            @Valid @RequestBody MedicineRequest req) {
        MedicineResponse updated = medicineService.updateMedicine(id, req);
        return ResponseEntity.ok(ApiResponse.ok("Medicine updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.ok(ApiResponse.ok("Medicine deleted successfully", null));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.ok(medicineService.getCategories()));
    }
}
