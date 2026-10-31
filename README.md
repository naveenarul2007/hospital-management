# 🏥 MediCare - Hospital Management System (No Database Required)

> A modern, full-featured Hospital Management System built with **Java 21**, **Spring Boot 3**, **Thymeleaf**, and **Bootstrap 5**, operating completely independently using the **Java Collections Framework** (In-Memory Data Storage). No external database installation (MySQL, PostgreSQL, MongoDB, etc.) is needed!

---

## 🌟 Key Highlights

- **Zero-Database Architecture**: Complete application runtime state maintained using Java Collections (`ArrayList`, `HashMap`) and atomic sequences for automated ID generation.
- **Role-Based Security**: Built with Spring Security 6 featuring demo credentials autofill on the login screen.
- **Enterprise UI/UX**: Designed with a responsive sidebar layout, stat cards, Chart.js visual charts, clean data tables, and print-ready stylesheets.
- **End-to-End Workflows**: Preloaded with realistic sample data across all hospital departments.

---

## 🚀 Modules & Features

### 1. 📊 Executive Dashboard
- Live statistical indicators (Total Patients, Active Doctors, Today's Appointments, Total Revenue).
- Interactive Chart.js appointment distribution doughnut chart.
- Today's appointment agenda and instant low-stock drug warning banners.
- Quick navigation shortcuts.

### 2. 👥 Patient Management
- Complete CRUD operations with automated ID generation (`PAT-1001`, `PAT-1002`, ...).
- Comprehensive patient profile views with tabbed sections for:
  - Appointment history
  - Electronic Health Records (EHR) & clinical notes
  - Laboratory diagnostic orders & findings
  - Billing invoices and payment receipts
- Live search by patient name or phone number.

### 3. 👨‍⚕️ Doctor Management
- Specialist registry with medical credentials, qualification, and departments.
- Consultation fee management and weekly availability schedules.
- Departmental filtering and doctor profile views.

### 4. 📅 Appointment Scheduling
- Appointment booking with real-time doctor slot availability validation.
- Appointment lifecycle tracking: `PENDING` ➔ `CONFIRMED` ➔ `COMPLETED` / `CANCELLED`.
- Filter consultations by status or view today's schedule.

### 5. 📋 Electronic Health Records (EHR)
- Clinical assessments, symptoms, vitals, and physician observations.
- Prescription (Rx) management and recommended laboratory orders.
- Printable official consultation sheet & prescription layout.

### 6. 💳 Billing & Invoicing
- Itemized hospital invoices (consultation, diagnostics, medications, nursing).
- Discount calculation and automated net total computation.
- Payment recording (Cash, UPI, Card, Insurance) with partial/full settlement.
- Printable formal hospital invoice receipt.

### 7. 💊 Pharmacy Inventory
- Pharmaceutical stock inventory with batch expiry dates.
- Minimum stock reorder thresholds and automatic `LOW_STOCK` / `OUT_OF_STOCK` flags.
- Instant 1-click modal restock mechanism.

### 8. 🧪 Clinical Laboratory
- Diagnostic investigation requests (Blood pathology, Radiology, Urine analysis, CT/MRI).
- Phased workflow: `REQUESTED` ➔ `SAMPLE_COLLECTED` ➔ `IN_PROGRESS` ➔ `COMPLETED`.
- Result entry modal with unit of measurement and reference ranges.

### 9. 🩺 Staff Directory
- Directory for nurses, receptionists, pharmacists, technicians, and administrators.
- Shift allocation (Morning, Afternoon, Night, General) and salary tracking.

### 10. 📈 Analytics & Reports
- Departmental patient consultation volume bar chart.
- Appointment status breakdown.
- Financial overview (Collected Revenue vs. Pending Receivables).

---

## 🔑 Demo Login Accounts

| Role | Username | Password | Access Privileges |
|---|---|---|---|
| **Administrator** | `admin` | `admin123` | Full system access to all modules |
| **Doctor** | `doctor` | `doctor123` | Patients, Appointments, EHR, Lab |
| **Receptionist** | `receptionist` | `recep123` | Patients, Appointments, Billing |
| **Patient** | `patient` | `patient123` | Patient portal & profile |

*(Tip: You can click the demo credential buttons on the login page for 1-click login!)*

---

## 🛠️ Technology Stack

- **Backend**: Java 21, Spring Boot 3.2.0, Spring MVC, Spring Security 6
- **Storage**: Java Collections Framework (`ArrayList`, `HashMap`, `AtomicInteger`)
- **Frontend**: Thymeleaf 3, Bootstrap 5.3, FontAwesome 6, Chart.js, Google Fonts (Inter)
- **Build Tool**: Apache Maven

---

## 💻 How to Run Locally

### Prerequisites
- Java 17 or Java 21 installed (`java -version`)
- Maven 3.8+ installed (`mvn -version`)

### Run with Maven:
```bash
# Clone the repository
git clone https://github.com/naveenarul2007/hospital-management.git
cd hospital-management

# Run Spring Boot
mvn spring-boot:run
```

Once started, open your web browser and navigate to:
```
http://localhost:8080
```
