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

## 🌐 Live Online Hosting Links

This project supports both **Instant Static Web Hosting** (GitHub Pages / Vercel) and **Full Java Spring Boot Backend Hosting** (Render / Railway / Docker).

### 🚀 Instant Web Demo (GitHub Pages & Vercel)
- **GitHub Pages**: `https://naveenarul2007.github.io/hospital-management/`
- **Vercel**: `https://hospitalmanagementsystem-snowy.vercel.app`

*(Powered by `index.html` at repository root with complete interactive features and local storage persistence)*

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

---

## 🌐 How to Host Live from GitHub (Free Cloud Deployment)

Because this is a dynamic **Java / Spring Boot** application (not a static website), it cannot be hosted on GitHub Pages alone (which only serves static HTML/JS/CSS). However, you can host it **100% free** directly linked to your GitHub repository using any of the following cloud platforms:

### Option 1: Render.com (Recommended - 1-Click Auto-Deploy)
1. Sign up / Log in to [Render.com](https://render.com) using your GitHub account.
2. Click **New +** ➔ **Web Service**.
3. Select your repository: `naveenarul2007/hospital-management`.
4. Choose **Docker** as the Runtime (or select the repository's `render.yaml`).
5. Choose the **Free** instance plan.
6. Click **Create Web Service**. Render will automatically build the Docker container and give you a public URL (e.g. `https://medicare-hospital.onrender.com`). Every time you push to `main`, it will auto-deploy!

### Option 2: Railway.app
1. Go to [Railway.app](https://railway.app) and log in with GitHub.
2. Click **New Project** ➔ **Deploy from GitHub repo**.
3. Select `hospital-management`.
4. Railway will automatically detect the `Dockerfile` and deploy your app with a public domain!

### Option 3: Koyeb
1. Go to [Koyeb.com](https://www.koyeb.com) and link your GitHub account.
2. Create a new service from your GitHub repo.
3. Koyeb will build the Dockerfile and deploy to their global edge network.

