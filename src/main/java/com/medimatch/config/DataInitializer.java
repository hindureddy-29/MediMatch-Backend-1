package com.medimatch.config;

import com.medimatch.entity.Medicine;
import com.medimatch.entity.User;
import com.medimatch.repository.MedicineRepository;
import com.medimatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(MedicineRepository medicineRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.medicineRepository = medicineRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Seed default admin and test user if not present
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", "admin@medimatch.com", passwordEncoder.encode("admin123"), "ROLE_ADMIN");
            userRepository.save(admin);
        }
        if (!userRepository.existsByUsername("pharmacist")) {
            User demo = new User("pharmacist", "demo@medimatch.com", passwordEncoder.encode("demo123"), "ROLE_USER");
            userRepository.save(demo);
        }

        // Seed verified medicines catalog if empty
        if (medicineRepository.count() == 0) {
            List<Medicine> verifiedMedicines = Arrays.asList(
                // Analgesics & Antipyretics
                new Medicine("Tylenol", "Paracetamol", "Paracetamol", "500 mg", "Tablet", "McNeil Healthcare", "Analgesic / Antipyretic", true, "Oral pain reliever and antipyretic."),
                new Medicine("Panadol", "Paracetamol", "Paracetamol", "500 mg", "Tablet", "GSK", "Analgesic / Antipyretic", true, "Standard pain and fever formulation."),
                new Medicine("Calpol", "Paracetamol", "Paracetamol", "500 mg", "Tablet", "GSK", "Rapid antipyretic and analgesic."),
                new Medicine("Dolo 650", "Paracetamol", "Paracetamol", "650 mg", "Tablet", "Micro Labs", "Analgesic / Antipyretic", true, "High-strength fever and pain relief tablet."),
                new Medicine("Crocin", "Paracetamol", "Paracetamol", "500 mg", "Tablet", "GSK", "Analgesic / Antipyretic", true, "Fast acting antipyretic formulation."),
                new Medicine("Advil", "Ibuprofen", "Ibuprofen", "200 mg", "Capsule", "Pfizer", "NSAID", true, "Non-steroidal anti-inflammatory and pain reliever."),
                new Medicine("Motrin", "Ibuprofen", "Ibuprofen", "200 mg", "Tablet", "Johnson & Johnson", "NSAID", true, "NSAID anti-inflammatory for acute pain."),
                new Medicine("Nurofen", "Ibuprofen", "Ibuprofen", "400 mg", "Tablet", "Reckitt Benckiser", "NSAID", true, "Higher dose NSAID analgesic."),
                new Medicine("Brufen", "Ibuprofen", "Ibuprofen", "400 mg", "Tablet", "Abbott", "NSAID", true, "Anti-inflammatory and antirheumatic agent."),
                new Medicine("Aleve", "Naproxen", "Naproxen", "220 mg", "Tablet", "Bayer", "NSAID", true, "Long-acting nonsteroidal anti-inflammatory."),
                new Medicine("Naprosyn", "Naproxen", "Naproxen", "250 mg", "Tablet", "Roche", "NSAID", true, "Analgesic and anti-inflammatory formulation."),
                new Medicine("Aspirin Protect", "Acetylsalicylic Acid", "Acetylsalicylic Acid", "100 mg", "Tablet", "Bayer", "Antiplatelet / NSAID", true, "Cardiovascular antiplatelet and antithrombotic dose."),
                new Medicine("Disprin", "Acetylsalicylic Acid", "Acetylsalicylic Acid", "350 mg", "Tablet", "Reckitt Benckiser", "Analgesic / Antipyretic", true, "Effervescent aspirin analgesic."),
                new Medicine("Voltaren", "Diclofenac Sodium", "Diclofenac Sodium", "50 mg", "Tablet", "Novartis", "NSAID", true, "Potent NSAID for muscular and joint pain."),
                new Medicine("Voveran", "Diclofenac Sodium", "Diclofenac Sodium", "50 mg", "Tablet", "Novartis", "NSAID", true, "Prescription NSAID for acute inflammatory pain."),

                // Statins & Cardiovascular
                new Medicine("Lipitor", "Atorvastatin", "Atorvastatin", "20 mg", "Tablet", "Pfizer", "Lipid-Lowering", true, "HMG-CoA reductase inhibitor for hypercholesterolemia."),
                new Medicine("Storvas", "Atorvastatin", "Atorvastatin", "10 mg", "Tablet", "Sun Pharma", "Lipid-Lowering", true, "Cholesterol-lowering statin therapy."),
                new Medicine("Atorva", "Atorvastatin", "Atorvastatin", "20 mg", "Tablet", "Zydus Cadila", "Lipid-Lowering", true, "Lipid management therapy."),
                new Medicine("Crestor", "Rosuvastatin", "Rosuvastatin", "10 mg", "Tablet", "AstraZeneca", "Lipid-Lowering", true, "High-intensity statin for dyslipidemia."),
                new Medicine("Rosuvas", "Rosuvastatin", "Rosuvastatin", "10 mg", "Tablet", "Sun Pharma", "Lipid-Lowering", true, "Cardiovascular risk reduction statin."),
                new Medicine("Norvasc", "Amlodipine", "Amlodipine", "5 mg", "Tablet", "Pfizer", "Antihypertensive", true, "Calcium channel blocker for arterial hypertension."),
                new Medicine("Amlopres", "Amlodipine", "Amlodipine", "5 mg", "Tablet", "Cipla", "Antihypertensive", true, "Vasodilator for blood pressure management."),
                new Medicine("Plavix", "Clopidogrel", "Clopidogrel", "75 mg", "Tablet", "Sanofi", "Antiplatelet", true, "P2Y12 inhibitor preventing secondary thrombosis."),
                new Medicine("Deplatt", "Clopidogrel", "Clopidogrel", "75 mg", "Tablet", "Torrent Pharma", "Antiplatelet", true, "Cardiovascular platelet aggregation inhibitor."),
                new Medicine("Tenormin", "Atenolol", "Atenolol", "50 mg", "Tablet", "AstraZeneca", "Beta Blocker", true, "Cardioselective beta-1 adrenergic antagonist."),
                new Medicine("Aten", "Atenolol", "Atenolol", "50 mg", "Tablet", "Zydus Cadila", "Beta Blocker", true, "Hypertension and angina beta blocker."),
                new Medicine("Lopressor", "Metoprolol Tartrate", "Metoprolol Tartrate", "50 mg", "Tablet", "Novartis", "Beta Blocker", true, "Short-acting beta adrenergic blocker."),
                new Medicine("Metolar", "Metoprolol Tartrate", "Metoprolol Tartrate", "50 mg", "Tablet", "Cipla", "Beta Blocker", true, "Antianginal and antihypertensive agent."),
                new Medicine("Lasix", "Furosemide", "Furosemide", "40 mg", "Tablet", "Sanofi", "Diuretic", true, "Loop diuretic for edema and fluid retention."),

                // Antidiabetic
                new Medicine("Glucophage", "Metformin", "Metformin", "500 mg", "Tablet", "Merck", "Antidiabetic", true, "Biguanide first-line type 2 diabetes medication."),
                new Medicine("Glycomet", "Metformin", "Metformin", "500 mg", "Tablet", "USV", "Antidiabetic", true, "First-line oral glycemic control."),
                new Medicine("Fortamet", "Metformin", "Metformin", "1000 mg", "Tablet", "Shionogi", "Antidiabetic", true, "Extended release metformin biguanide."),
                new Medicine("Januvia", "Sitagliptin", "Sitagliptin", "100 mg", "Tablet", "Merck Sharp & Dohme", "Antidiabetic", true, "DPP-4 inhibitor preserving incretin hormones."),
                new Medicine("Istavel", "Sitagliptin", "Sitagliptin", "100 mg", "Tablet", "Sun Pharma", "Antidiabetic", true, "Incretin enhancer for type 2 diabetes mellitus."),
                new Medicine("Amaryl", "Glimepiride", "Glimepiride", "2 mg", "Tablet", "Sanofi", "Antidiabetic", true, "Sulfonylurea stimulating pancreatic beta cells."),
                new Medicine("Glimisave", "Glimepiride", "Glimepiride", "2 mg", "Tablet", "Eris Lifesciences", "Antidiabetic", true, "Second-generation sulfonylurea agent."),
                new Medicine("Jardiance", "Empagliflozin", "Empagliflozin", "10 mg", "Tablet", "Boehringer Ingelheim", "Antidiabetic", true, "SGLT-2 inhibitor promoting renal glucose excretion."),
                new Medicine("Gibtulio", "Empagliflozin", "Empagliflozin", "10 mg", "Tablet", "Lupin", "Antidiabetic", true, "SGLT-2 oral antidiabetic therapy."),

                // Antibiotics
                new Medicine("Augmentin", "Amoxicillin + Clavulanic Acid", "Amoxicillin + Clavulanic Acid", "625 mg", "Tablet", "GSK", "Antibiotic", true, "Beta-lactam with beta-lactamase inhibitor."),
                new Medicine("Clavam", "Amoxicillin + Clavulanic Acid", "Amoxicillin + Clavulanic Acid", "625 mg", "Tablet", "Alkem", "Antibiotic", true, "Broad-spectrum co-amoxiclav formulation."),
                new Medicine("Amoxil", "Amoxicillin", "Amoxicillin", "500 mg", "Capsule", "GSK", "Antibiotic", true, "Aminopenicillin for respiratory and soft tissue infections."),
                new Medicine("Mox", "Amoxicillin", "Amoxicillin", "500 mg", "Capsule", "Ranbaxy", "Antibiotic", true, "Monotherapy aminopenicillin antibiotic."),
                new Medicine("Zithromax", "Azithromycin", "Azithromycin", "500 mg", "Tablet", "Pfizer", "Antibiotic", true, "Macrolide antibiotic with long tissue half-life."),
                new Medicine("Azee", "Azithromycin", "Azithromycin", "500 mg", "Tablet", "Cipla", "Antibiotic", true, "Once-daily macrolide antibiotic."),
                new Medicine("Azithral", "Azithromycin", "Azithromycin", "500 mg", "Tablet", "Alembic", "Antibiotic", true, "Macrolide antibiotic formulation."),
                new Medicine("Cipro", "Ciprofloxacin", "Ciprofloxacin", "500 mg", "Tablet", "Bayer", "Antibiotic", true, "Fluoroquinolone broad-spectrum antimicrobial."),
                new Medicine("Ciplox", "Ciprofloxacin", "Ciprofloxacin", "500 mg", "Tablet", "Cipla", "Antibiotic", true, "Broad-spectrum fluoroquinolone."),
                new Medicine("Flagyl", "Metronidazole", "Metronidazole", "400 mg", "Tablet", "Sanofi", "Antiprotozoal / Antibacterial", true, "Nitroimidazole for anaerobic and protozoal infections."),
                new Medicine("Metrogyl", "Metronidazole", "Metronidazole", "400 mg", "Tablet", "J.B. Chemicals", "Antiprotozoal / Antibacterial", true, "Anaerobic bactericidal agent."),

                // Proton Pump Inhibitors (PPIs)
                new Medicine("Nexium", "Esomeprazole", "Esomeprazole", "40 mg", "Capsule", "AstraZeneca", "Proton Pump Inhibitor", true, "S-enantiomer of omeprazole for GERD."),
                new Medicine("Esomac", "Esomeprazole", "Esomeprazole", "40 mg", "Tablet", "Cipla", "Proton Pump Inhibitor", true, "Acid-suppressive gastric antisecretory drug."),
                new Medicine("Prilosec", "Omeprazole", "Omeprazole", "20 mg", "Capsule", "Procter & Gamble", "Proton Pump Inhibitor", true, "Gastric parietal cell H+/K+ ATPase inhibitor."),
                new Medicine("Omez", "Omeprazole", "Omeprazole", "20 mg", "Capsule", "Dr. Reddy's", "Proton Pump Inhibitor", true, "Widely prescribed omeprazole brand."),
                new Medicine("Pantocid", "Pantoprazole", "Pantoprazole", "40 mg", "Tablet", "Sun Pharma", "Proton Pump Inhibitor", true, "Delayed-release pantoprazole for reflux and ulceration."),
                new Medicine("Protonix", "Pantoprazole", "Pantoprazole", "40 mg", "Tablet", "Pfizer", "Proton Pump Inhibitor", true, "Gastric antisecretory PPI formulation."),

                // Respiratory & Antiallergic
                new Medicine("Ventolin", "Salbutamol", "Salbutamol", "100 mcg", "Inhaler", "GSK", "Bronchodilator", true, "Short-acting beta-2 agonist for acute bronchospasm."),
                new Medicine("Asthalin", "Salbutamol", "Salbutamol", "100 mcg", "Inhaler", "Cipla", "Bronchodilator", true, "Rapid-acting bronchodilator aerosol."),
                new Medicine("ProAir", "Albuterol", "Albuterol", "90 mcg", "Inhaler", "Teva", "Bronchodilator", true, "Inhaled rescue therapy for asthma."),
                new Medicine("Zyrtec", "Cetirizine", "Cetirizine", "10 mg", "Tablet", "Johnson & Johnson", "Antihistamine", true, "Second-generation selective H1 antagonist."),
                new Medicine("Cetzine", "Cetirizine", "Cetirizine", "10 mg", "Tablet", "Dr. Reddy's", "Antihistamine", true, "Non-sedating allergic rhinitis and urticaria relief."),
                new Medicine("Claritin", "Loratadine", "Loratadine", "10 mg", "Tablet", "Bayer", "Antihistamine", true, "Non-drowsy H1 receptor antagonist."),
                new Medicine("Allegra", "Fexofenadine", "Fexofenadine", "120 mg", "Tablet", "Sanofi", "Antihistamine", true, "Third-generation non-sedating antihistamine."),

                // CNS & Antidepressants
                new Medicine("Zoloft", "Sertraline", "Sertraline", "50 mg", "Tablet", "Pfizer", "SSRI Antidepressant", true, "Selective serotonin reuptake inhibitor."),
                new Medicine("Daxid", "Sertraline", "Sertraline", "50 mg", "Tablet", "Pfizer", "SSRI Antidepressant", true, "Prescription SSRI for depression and anxiety."),
                new Medicine("Prozac", "Fluoxetine", "Fluoxetine", "20 mg", "Capsule", "Eli Lilly", "SSRI Antidepressant", true, "Prototypical selective serotonin reuptake inhibitor."),
                new Medicine("Lexapro", "Escitalopram", "Escitalopram", "10 mg", "Tablet", "AbbVie", "SSRI Antidepressant", true, "Pure S-enantiomer SSRI for major depressive disorder.")
            );

            medicineRepository.saveAll(verifiedMedicines);
        }
    }
}
