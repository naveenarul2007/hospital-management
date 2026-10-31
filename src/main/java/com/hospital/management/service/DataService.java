package com.hospital.management.service;

import com.hospital.management.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class DataService {

    // In-memory storage using ArrayList and HashMap
    private final List<Patient> patients = new ArrayList<>();
    private final List<Doctor> doctors = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();
    private final List<MedicalRecord> medicalRecords = new ArrayList<>();
    private final List<Medicine> medicines = new ArrayList<>();
    private final List<LabTest> labTests = new ArrayList<>();
    private final List<Bill> bills = new ArrayList<>();
    private final List<Staff> staffList = new ArrayList<>();
    private final List<Notification> notifications = new ArrayList<>();

    // ID counters
    private final AtomicInteger patientCounter = new AtomicInteger(1000);
    private final AtomicInteger doctorCounter = new AtomicInteger(100);
    private final AtomicInteger appointmentCounter = new AtomicInteger(5000);
    private final AtomicInteger recordCounter = new AtomicInteger(200);
    private final AtomicInteger medicineCounter = new AtomicInteger(300);
    private final AtomicInteger labCounter = new AtomicInteger(400);
    private final AtomicInteger billCounter = new AtomicInteger(600);
    private final AtomicInteger staffCounter = new AtomicInteger(700);
    private final AtomicInteger notifCounter = new AtomicInteger(800);

    public DataService() {
        initializeSampleData();
    }

    // ======================== ID GENERATORS ========================
    public String generatePatientId() { return "PAT-" + patientCounter.incrementAndGet(); }
    public String generateDoctorId() { return "DOC-" + doctorCounter.incrementAndGet(); }
    public String generateAppointmentId() { return "APT-" + appointmentCounter.incrementAndGet(); }
    public String generateRecordId() { return "REC-" + recordCounter.incrementAndGet(); }
    public String generateMedicineId() { return "MED-" + medicineCounter.incrementAndGet(); }
    public String generateLabId() { return "LAB-" + labCounter.incrementAndGet(); }
    public String generateBillId() { return "BIL-" + billCounter.incrementAndGet(); }
    public String generateStaffId() { return "STF-" + staffCounter.incrementAndGet(); }
    public String generateNotifId() { return "NOT-" + notifCounter.incrementAndGet(); }

    // ======================== PATIENT CRUD ========================
    public List<Patient> getAllPatients() { return new ArrayList<>(patients); }

    public Optional<Patient> findPatientById(String id) {
        return patients.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Patient savePatient(Patient patient) {
        if (patient.getId() == null || patient.getId().isEmpty()) {
            patient.setId(generatePatientId());
            patient.setRegisteredAt(LocalDateTime.now());
            patients.add(patient);
            addNotification("New Patient Registered", patient.getFullName() + " has been registered.", "INFO", "ALL");
        } else {
            patients.removeIf(p -> p.getId().equals(patient.getId()));
            patients.add(patient);
        }
        return patient;
    }

    public boolean deletePatient(String id) {
        return patients.removeIf(p -> p.getId().equals(id));
    }

    public List<Patient> searchPatients(String query) {
        String q = query.toLowerCase();
        return patients.stream()
                .filter(p -> p.getFullName().toLowerCase().contains(q) ||
                        p.getId().toLowerCase().contains(q) ||
                        p.getEmail().toLowerCase().contains(q) ||
                        p.getPhone().contains(q))
                .collect(Collectors.toList());
    }

    // ======================== DOCTOR CRUD ========================
    public List<Doctor> getAllDoctors() { return new ArrayList<>(doctors); }

    public Optional<Doctor> findDoctorById(String id) {
        return doctors.stream().filter(d -> d.getId().equals(id)).findFirst();
    }

    public Doctor saveDoctor(Doctor doctor) {
        if (doctor.getId() == null || doctor.getId().isEmpty()) {
            doctor.setId(generateDoctorId());
            doctor.setJoinedAt(LocalDateTime.now());
            doctors.add(doctor);
        } else {
            doctors.removeIf(d -> d.getId().equals(doctor.getId()));
            doctors.add(doctor);
        }
        return doctor;
    }

    public boolean deleteDoctor(String id) {
        return doctors.removeIf(d -> d.getId().equals(id));
    }

    public List<Doctor> searchDoctors(String query) {
        String q = query.toLowerCase();
        return doctors.stream()
                .filter(d -> d.getFullName().toLowerCase().contains(q) ||
                        d.getSpecialization().toLowerCase().contains(q) ||
                        d.getDepartment().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Doctor> getDoctorsByDepartment(String department) {
        return doctors.stream()
                .filter(d -> d.getDepartment().equalsIgnoreCase(department) && "ACTIVE".equals(d.getStatus()))
                .collect(Collectors.toList());
    }

    // ======================== APPOINTMENT CRUD ========================
    public List<Appointment> getAllAppointments() { return new ArrayList<>(appointments); }

    public Optional<Appointment> findAppointmentById(String id) {
        return appointments.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    public Appointment saveAppointment(Appointment appointment) {
        if (appointment.getId() == null || appointment.getId().isEmpty()) {
            appointment.setId(generateAppointmentId());
            appointment.setCreatedAt(LocalDateTime.now());
            appointments.add(appointment);
            addNotification("New Appointment Booked",
                    appointment.getPatientName() + " booked with " + appointment.getDoctorName(), "INFO", "ALL");
        } else {
            appointments.removeIf(a -> a.getId().equals(appointment.getId()));
            appointments.add(appointment);
        }
        return appointment;
    }

    public boolean deleteAppointment(String id) {
        return appointments.removeIf(a -> a.getId().equals(id));
    }

    public boolean isSlotAvailable(String doctorId, LocalDate date, LocalTime time) {
        return appointments.stream()
                .noneMatch(a -> a.getDoctorId().equals(doctorId) &&
                        a.getAppointmentDate().equals(date) &&
                        a.getAppointmentTime().equals(time) &&
                        !a.getStatus().equals("CANCELLED"));
    }

    public List<Appointment> getTodayAppointments() {
        LocalDate today = LocalDate.now();
        return appointments.stream()
                .filter(a -> a.getAppointmentDate().equals(today))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointments.stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointments.stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    // ======================== MEDICAL RECORDS CRUD ========================
    public List<MedicalRecord> getAllMedicalRecords() { return new ArrayList<>(medicalRecords); }

    public Optional<MedicalRecord> findRecordById(String id) {
        return medicalRecords.stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    public MedicalRecord saveMedicalRecord(MedicalRecord record) {
        if (record.getId() == null || record.getId().isEmpty()) {
            record.setId(generateRecordId());
            record.setRecordedAt(LocalDateTime.now());
            medicalRecords.add(record);
        } else {
            medicalRecords.removeIf(r -> r.getId().equals(record.getId()));
            medicalRecords.add(record);
        }
        return record;
    }

    public boolean deleteMedicalRecord(String id) {
        return medicalRecords.removeIf(r -> r.getId().equals(id));
    }

    public List<MedicalRecord> getRecordsByPatient(String patientId) {
        return medicalRecords.stream()
                .filter(r -> r.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    // ======================== MEDICINE CRUD ========================
    public List<Medicine> getAllMedicines() { return new ArrayList<>(medicines); }

    public Optional<Medicine> findMedicineById(String id) {
        return medicines.stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    public Medicine saveMedicine(Medicine medicine) {
        if (medicine.getId() == null || medicine.getId().isEmpty()) {
            medicine.setId(generateMedicineId());
            medicine.setAddedAt(LocalDateTime.now());
            medicines.add(medicine);
        } else {
            medicines.removeIf(m -> m.getId().equals(medicine.getId()));
            medicines.add(medicine);
        }
        checkLowStockAlerts();
        return medicine;
    }

    public boolean deleteMedicine(String id) {
        return medicines.removeIf(m -> m.getId().equals(id));
    }

    public List<Medicine> getLowStockMedicines() {
        return medicines.stream()
                .filter(Medicine::isLowStock)
                .collect(Collectors.toList());
    }

    private void checkLowStockAlerts() {
        medicines.stream()
                .filter(Medicine::isLowStock)
                .forEach(m -> addNotification("Low Stock Alert",
                        m.getName() + " is running low. Current stock: " + m.getQuantity(),
                        "WARNING", "ADMIN"));
    }

    // ======================== LAB TESTS CRUD ========================
    public List<LabTest> getAllLabTests() { return new ArrayList<>(labTests); }

    public Optional<LabTest> findLabTestById(String id) {
        return labTests.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public LabTest saveLabTest(LabTest test) {
        if (test.getId() == null || test.getId().isEmpty()) {
            test.setId(generateLabId());
            test.setRequestedAt(LocalDateTime.now());
            labTests.add(test);
        } else {
            labTests.removeIf(t -> t.getId().equals(test.getId()));
            labTests.add(test);
        }
        return test;
    }

    public boolean deleteLabTest(String id) {
        return labTests.removeIf(t -> t.getId().equals(id));
    }

    public List<LabTest> getLabTestsByPatient(String patientId) {
        return labTests.stream()
                .filter(t -> t.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    // ======================== BILL CRUD ========================
    public List<Bill> getAllBills() { return new ArrayList<>(bills); }

    public Optional<Bill> findBillById(String id) {
        return bills.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    public Bill saveBill(Bill bill) {
        if (bill.getId() == null || bill.getId().isEmpty()) {
            bill.setId(generateBillId());
            bill.setGeneratedAt(LocalDateTime.now());
            bills.add(bill);
        } else {
            bills.removeIf(b -> b.getId().equals(bill.getId()));
            bills.add(bill);
        }
        return bill;
    }

    public boolean deleteBill(String id) {
        return bills.removeIf(b -> b.getId().equals(id));
    }

    public List<Bill> getBillsByPatient(String patientId) {
        return bills.stream()
                .filter(b -> b.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public double getTotalRevenue() {
        return bills.stream()
                .filter(b -> "PAID".equals(b.getPaymentStatus()))
                .mapToDouble(Bill::getPaidAmount)
                .sum();
    }

    // ======================== STAFF CRUD ========================
    public List<Staff> getAllStaff() { return new ArrayList<>(staffList); }

    public Optional<Staff> findStaffById(String id) {
        return staffList.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public Staff saveStaff(Staff staff) {
        if (staff.getId() == null || staff.getId().isEmpty()) {
            staff.setId(generateStaffId());
            staff.setJoinedAt(LocalDateTime.now());
            staffList.add(staff);
        } else {
            staffList.removeIf(s -> s.getId().equals(staff.getId()));
            staffList.add(staff);
        }
        return staff;
    }

    public boolean deleteStaff(String id) {
        return staffList.removeIf(s -> s.getId().equals(id));
    }

    // ======================== NOTIFICATIONS ========================
    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notifications);
    }

    public List<Notification> getUnreadNotifications() {
        return notifications.stream().filter(n -> !n.isRead()).collect(Collectors.toList());
    }

    public void addNotification(String title, String message, String type, String targetRole) {
        Notification n = new Notification(generateNotifId(), title, message, type, targetRole);
        notifications.add(0, n); // Add to front
        if (notifications.size() > 50) {
            notifications.remove(notifications.size() - 1); // Keep max 50
        }
    }

    public void markNotificationRead(String id) {
        notifications.stream()
                .filter(n -> n.getId().equals(id))
                .forEach(n -> n.setRead(true));
    }

    public void markAllNotificationsRead() {
        notifications.forEach(n -> n.setRead(true));
    }

    // ======================== DASHBOARD STATS ========================
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPatients", patients.size());
        stats.put("totalDoctors", doctors.size());
        stats.put("totalStaff", staffList.size());
        stats.put("todayAppointments", getTodayAppointments().size());
        stats.put("pendingAppointments", appointments.stream()
                .filter(a -> "PENDING".equals(a.getStatus())).count());
        stats.put("completedAppointments", appointments.stream()
                .filter(a -> "COMPLETED".equals(a.getStatus())).count());
        stats.put("totalRevenue", getTotalRevenue());
        stats.put("pendingBills", bills.stream()
                .filter(b -> "PENDING".equals(b.getPaymentStatus())).count());
        stats.put("lowStockMedicines", getLowStockMedicines().size());
        stats.put("pendingLabTests", labTests.stream()
                .filter(t -> "REQUESTED".equals(t.getStatus())).count());
        stats.put("unreadNotifications", getUnreadNotifications().size());
        return stats;
    }

    // ======================== DEPARTMENTS ========================
    public List<String> getDepartments() {
        return List.of("Cardiology", "Neurology", "Orthopedics", "Pediatrics",
                "Gynecology", "Ophthalmology", "Dermatology", "ENT",
                "Gastroenterology", "Pulmonology", "Urology", "Psychiatry",
                "Oncology", "Radiology", "Emergency", "General Medicine");
    }

    // ======================== SAMPLE DATA INITIALIZATION ========================
    private void initializeSampleData() {
        // Sample Doctors
        Doctor d1 = new Doctor("DOC-101", "Rajesh", "Kumar", "rajesh.kumar@hospital.com",
                "9876543210", "Cardiology", "MD, DM Cardiology", "Cardiology",
                1500.0, "12 years", "Mon-Sat", "09:00-17:00", "Male");
        d1.setLicenseNumber("MCI-12345");
        d1.setBio("Expert cardiologist with 12 years of experience in treating heart diseases.");
        doctors.add(d1);

        Doctor d2 = new Doctor("DOC-102", "Priya", "Sharma", "priya.sharma@hospital.com",
                "9876543211", "Neurology", "MD, DM Neurology", "Neurology",
                1800.0, "10 years", "Mon-Fri", "10:00-18:00", "Female");
        d2.setLicenseNumber("MCI-12346");
        doctors.add(d2);

        Doctor d3 = new Doctor("DOC-103", "Arjun", "Patel", "arjun.patel@hospital.com",
                "9876543212", "Orthopedics", "MS Orthopedics", "Orthopedics",
                1200.0, "8 years", "Mon-Sat", "08:00-16:00", "Male");
        d3.setLicenseNumber("MCI-12347");
        doctors.add(d3);

        Doctor d4 = new Doctor("DOC-104", "Meena", "Reddy", "meena.reddy@hospital.com",
                "9876543213", "Pediatrics", "MD Pediatrics", "Pediatrics",
                1000.0, "6 years", "Mon-Fri", "09:00-17:00", "Female");
        d4.setLicenseNumber("MCI-12348");
        doctors.add(d4);

        Doctor d5 = new Doctor("DOC-105", "Suresh", "Nair", "suresh.nair@hospital.com",
                "9876543214", "General Medicine", "MBBS, MD", "General Medicine",
                800.0, "15 years", "Mon-Sun", "08:00-20:00", "Male");
        doctors.add(d5);

        // Sample Patients
        Patient p1 = new Patient("PAT-1001", "Ananya", "Krishnan", "ananya.k@email.com",
                "9123456789", "Female", LocalDate.of(1990, 5, 15),
                "B+", "123 Main St, Chennai", "Ravi Krishnan", "9123456700");
        p1.setAllergies("Penicillin");
        patients.add(p1);

        Patient p2 = new Patient("PAT-1002", "Vikram", "Singh", "vikram.s@email.com",
                "9123456790", "Male", LocalDate.of(1985, 8, 22),
                "O+", "456 Park Ave, Mumbai", "Priya Singh", "9123456701");
        patients.add(p2);

        Patient p3 = new Patient("PAT-1003", "Lakshmi", "Venkat", "lakshmi.v@email.com",
                "9123456791", "Female", LocalDate.of(1975, 3, 10),
                "A-", "789 Lake Road, Bangalore", "Raju Venkat", "9123456702");
        patients.add(p3);

        Patient p4 = new Patient("PAT-1004", "Mohammed", "Ali", "mohammed.a@email.com",
                "9123456792", "Male", LocalDate.of(1995, 11, 30),
                "AB+", "321 Hill Street, Hyderabad", "Fatima Ali", "9123456703");
        patients.add(p4);

        Patient p5 = new Patient("PAT-1005", "Deepa", "Menon", "deepa.m@email.com",
                "9123456793", "Female", LocalDate.of(1988, 7, 4),
                "B-", "654 Garden Lane, Kochi", "Sunil Menon", "9123456704");
        patients.add(p5);

        // Sample Appointments
        LocalDate today = LocalDate.now();
        Appointment a1 = new Appointment("APT-5001", "PAT-1001", "Ananya Krishnan",
                "DOC-101", "Dr. Rajesh Kumar", "Cardiology",
                today, LocalTime.of(10, 0), "Chest pain checkup", 1500.0);
        a1.setStatus("CONFIRMED");
        appointments.add(a1);

        Appointment a2 = new Appointment("APT-5002", "PAT-1002", "Vikram Singh",
                "DOC-102", "Dr. Priya Sharma", "Neurology",
                today, LocalTime.of(11, 0), "Headache and dizziness", 1800.0);
        a2.setStatus("PENDING");
        appointments.add(a2);

        Appointment a3 = new Appointment("APT-5003", "PAT-1003", "Lakshmi Venkat",
                "DOC-103", "Dr. Arjun Patel", "Orthopedics",
                today.minusDays(1), LocalTime.of(9, 0), "Knee pain", 1200.0);
        a3.setStatus("COMPLETED");
        appointments.add(a3);

        Appointment a4 = new Appointment("APT-5004", "PAT-1004", "Mohammed Ali",
                "DOC-104", "Dr. Meena Reddy", "Pediatrics",
                today.plusDays(1), LocalTime.of(14, 0), "Child fever", 1000.0);
        appointments.add(a4);

        // Sample Medicines
        Medicine m1 = new Medicine("MED-301", "Paracetamol 500mg", "Acetaminophen",
                "Tablet", "Sun Pharma", 500, 50, 2.5, LocalDate.now().plusMonths(18));
        medicines.add(m1);

        Medicine m2 = new Medicine("MED-302", "Amoxicillin 250mg", "Amoxicillin",
                "Capsule", "Cipla", 30, 50, 8.0, LocalDate.now().plusMonths(12));
        m2.updateStatus();
        medicines.add(m2);

        Medicine m3 = new Medicine("MED-303", "Metformin 500mg", "Metformin HCl",
                "Tablet", "Dr. Reddy's", 200, 30, 5.0, LocalDate.now().plusMonths(24));
        medicines.add(m3);

        Medicine m4 = new Medicine("MED-304", "Atorvastatin 10mg", "Atorvastatin",
                "Tablet", "Ranbaxy", 150, 20, 12.0, LocalDate.now().plusMonths(20));
        medicines.add(m4);

        Medicine m5 = new Medicine("MED-305", "Cough Syrup 100ml", "Dextromethorphan",
                "Syrup", "GSK", 45, 30, 85.0, LocalDate.now().plusMonths(10));
        medicines.add(m5);

        // Sample Lab Tests
        LabTest l1 = new LabTest("LAB-401", "PAT-1001", "Ananya Krishnan",
                "DOC-101", "Dr. Rajesh Kumar", "Complete Blood Count", "BLOOD", 350.0);
        l1.setStatus("COMPLETED");
        l1.setResult("Normal");
        l1.setCompletedAt(LocalDateTime.now().minusDays(1));
        labTests.add(l1);

        LabTest l2 = new LabTest("LAB-402", "PAT-1002", "Vikram Singh",
                "DOC-102", "Dr. Priya Sharma", "MRI Brain", "MRI", 3500.0);
        l2.setStatus("IN_PROGRESS");
        labTests.add(l2);

        // Sample Bills
        Bill b1 = new Bill("BIL-601", "PAT-1003", "Lakshmi Venkat", "APT-5003");
        b1.setConsultationCharge(1200.0);
        b1.setLabCharge(350.0);
        b1.setMedicineCharge(150.0);
        b1.setTotalAmount(1700.0);
        b1.setPaidAmount(1700.0);
        b1.setPaymentStatus("PAID");
        b1.setPaymentMethod("CARD");
        b1.setPaidAt(LocalDateTime.now().minusDays(1));
        bills.add(b1);

        // Sample Staff
        Staff s1 = new Staff("STF-701", "Kavitha", "Rajan", "kavitha.r@hospital.com",
                "9555551111", "NURSE", "General Medicine", "B.Sc Nursing", "MORNING", 35000, "Female");
        staffList.add(s1);

        Staff s2 = new Staff("STF-702", "Ramesh", "Babu", "ramesh.b@hospital.com",
                "9555552222", "RECEPTIONIST", "Front Desk", "B.Com", "MORNING", 25000, "Male");
        staffList.add(s2);

        Staff s3 = new Staff("STF-703", "Preethi", "Das", "preethi.d@hospital.com",
                "9555553333", "LAB_TECHNICIAN", "Laboratory", "B.Sc MLT", "AFTERNOON", 30000, "Female");
        staffList.add(s3);

        // Sample Medical Records
        MedicalRecord r1 = new MedicalRecord("REC-201", "PAT-1003", "Lakshmi Venkat",
                "DOC-103", "Dr. Arjun Patel", "APT-5003",
                "Osteoarthritis of the knee", "Knee pain, swelling, stiffness",
                "Moderate inflammation observed, X-ray shows joint space narrowing",
                "Tab Diclofenac 50mg twice daily, Tab Pantoprazole 40mg once daily",
                "Physiotherapy 3x weekly, Ice pack application");
        r1.setFollowUpDate("2024-02-15");
        medicalRecords.add(r1);

        // Sample Notifications
        addNotification("System Started", "Hospital Management System is ready.", "INFO", "ALL");
        addNotification("Low Stock Alert", "Amoxicillin 250mg stock is critically low.", "WARNING", "ADMIN");
        addNotification("Appointment Confirmed", "APT-5001 confirmed for Ananya Krishnan.", "SUCCESS", "ALL");
    }
}
