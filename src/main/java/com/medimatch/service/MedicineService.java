package com.medimatch.service;

import com.medimatch.dto.MedicineRequest;
import com.medimatch.dto.MedicineResponse;
import com.medimatch.entity.Medicine;
import com.medimatch.exception.ResourceNotFoundException;
import com.medimatch.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicineService {

    private final MedicineRepository medicineRepository;

    @Autowired
    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Transactional(readOnly = true)
    public List<MedicineResponse> getAllMedicines() {
        return medicineRepository.findAll()
                .stream()
                .map(MedicineResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicineResponse getMedicineById(Long id) {
        Medicine m = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
        return MedicineResponse.fromEntity(m);
    }

    @Transactional(readOnly = true)
    public List<MedicineResponse> searchMedicines(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllMedicines();
        }
        return medicineRepository.searchMedicines(query.trim())
                .stream()
                .map(MedicineResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public MedicineResponse createMedicine(MedicineRequest req) {
        Medicine m = new Medicine(
                req.getBrandName().trim(),
                req.getGenericName().trim(),
                req.getActiveIngredient().trim(),
                req.getStrength() != null ? req.getStrength().trim() : "",
                req.getDosageForm() != null ? req.getDosageForm().trim() : "",
                req.getManufacturer() != null ? req.getManufacturer().trim() : "",
                req.getCategory() != null ? req.getCategory().trim() : "General",
                req.getVerified() != null ? req.getVerified() : true,
                req.getDescription() != null ? req.getDescription().trim() : ""
        );
        Medicine saved = medicineRepository.save(m);
        return MedicineResponse.fromEntity(saved);
    }

    public MedicineResponse updateMedicine(Long id, MedicineRequest req) {
        Medicine m = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        m.setBrandName(req.getBrandName().trim());
        m.setGenericName(req.getGenericName().trim());
        m.setActiveIngredient(req.getActiveIngredient().trim());
        m.setStrength(req.getStrength() != null ? req.getStrength().trim() : "");
        m.setDosageForm(req.getDosageForm() != null ? req.getDosageForm().trim() : "");
        m.setManufacturer(req.getManufacturer() != null ? req.getManufacturer().trim() : "");
        m.setCategory(req.getCategory() != null ? req.getCategory().trim() : "General");
        if (req.getVerified() != null) {
            m.setVerified(req.getVerified());
        }
        if (req.getDescription() != null) {
            m.setDescription(req.getDescription().trim());
        }

        Medicine updated = medicineRepository.save(m);
        return MedicineResponse.fromEntity(updated);
    }

    public void deleteMedicine(Long id) {
        if (!medicineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medicine not found with id: " + id);
        }
        medicineRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<String> getCategories() {
        return medicineRepository.findDistinctCategories();
    }
}
