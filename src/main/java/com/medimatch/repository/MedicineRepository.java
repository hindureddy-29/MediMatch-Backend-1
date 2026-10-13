package com.medimatch.repository;

import com.medimatch.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    @Query("SELECT m FROM Medicine m WHERE " +
           "LOWER(m.brandName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.genericName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.activeIngredient) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.category) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Medicine> searchMedicines(@Param("query") String query);

    List<Medicine> findByCategoryIgnoreCase(String category);

    Optional<Medicine> findByBrandNameIgnoreCase(String brandName);

    Optional<Medicine> findByGenericNameIgnoreCase(String genericName);

    List<Medicine> findByActiveIngredientIgnoreCase(String activeIngredient);

    @Query("SELECT DISTINCT m.category FROM Medicine m WHERE m.category IS NOT NULL ORDER BY m.category")
    List<String> findDistinctCategories();

    long countByVerifiedTrue();
}
