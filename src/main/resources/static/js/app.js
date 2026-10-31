// MediCare HMS - Main JavaScript

// Toggle sidebar
function toggleSidebar() {
    document.body.classList.toggle('sidebar-collapsed');
    localStorage.setItem('sidebarCollapsed', document.body.classList.contains('sidebar-collapsed'));
}

// Restore sidebar state
document.addEventListener('DOMContentLoaded', function() {
    if (localStorage.getItem('sidebarCollapsed') === 'true') {
        document.body.classList.add('sidebar-collapsed');
    }

    // Auto dismiss alerts
    setTimeout(() => {
        document.querySelectorAll('.alert.fade.show').forEach(alert => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);

    // Confirm delete buttons
    document.querySelectorAll('form[data-confirm]').forEach(form => {
        form.addEventListener('submit', function(e) {
            const msg = this.getAttribute('data-confirm') || 'Are you sure?';
            if (!confirm(msg)) e.preventDefault();
        });
    });

    // Active nav link detection
    const currentPath = window.location.pathname;
    document.querySelectorAll('.nav-link').forEach(link => {
        const href = link.getAttribute('href');
        if (href && href !== '/dashboard' && currentPath.startsWith(href)) {
            link.classList.add('active');
        } else if (href === '/dashboard' && (currentPath === '/' || currentPath === '/dashboard')) {
            link.classList.add('active');
        }
    });

    // Animate stat cards on page load
    const cards = document.querySelectorAll('.stat-card, .mini-stat, .card-modern');
    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.05}s`;
    });
});

// Mark all notifications as read
function markAllRead() {
    fetch('/api/notifications/read-all', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            document.querySelectorAll('.notif-badge').forEach(b => b.remove());
            document.querySelectorAll('.notif-item.unread').forEach(i => i.classList.remove('unread'));
        }
    })
    .catch(err => console.error('Error:', err));
}

// Load doctors by department
function loadDoctorsByDepartment(dept) {
    if (!dept) return;
    fetch(`/api/doctors/by-department/${encodeURIComponent(dept)}`)
        .then(r => r.json())
        .then(doctors => {
            const select = document.getElementById('doctorId');
            const feeField = document.getElementById('consultationFee');
            if (!select) return;
            select.innerHTML = '<option value="">-- Select Doctor --</option>';
            doctors.forEach(d => {
                const opt = document.createElement('option');
                opt.value = d.id;
                opt.text = d.name + ' (' + d.specialization + ')';
                opt.dataset.fee = d.fee;
                select.appendChild(opt);
            });
            if (feeField) feeField.value = '';
        })
        .catch(err => console.error('Error loading doctors:', err));
}

// Update fee when doctor selected
function updateFee(select) {
    const feeField = document.getElementById('consultationFee');
    if (feeField && select.selectedOptions[0]) {
        feeField.value = select.selectedOptions[0].dataset.fee || '';
    }
}

// Calculate bill total
function calculateBillTotal() {
    const consultation = parseFloat(document.getElementById('consultationCharge')?.value || 0);
    const lab = parseFloat(document.getElementById('labCharge')?.value || 0);
    const medicine = parseFloat(document.getElementById('medicineCharge')?.value || 0);
    const other = parseFloat(document.getElementById('otherCharges')?.value || 0);
    const discount = parseFloat(document.getElementById('discountAmount')?.value || 0);
    const total = consultation + lab + medicine + other - discount;
    const totalEl = document.getElementById('totalAmount');
    if (totalEl) totalEl.value = total.toFixed(2);
}

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(amount);
}

// Print invoice
function printInvoice() {
    window.print();
}

// Confirm delete modal
function confirmDelete(formId) {
    if (confirm('Are you sure you want to delete this record? This action cannot be undone.')) {
        document.getElementById(formId).submit();
    }
}

// Check slot availability
function checkSlotAvailability() {
    const doctorId = document.getElementById('doctorId')?.value;
    const date = document.getElementById('appointmentDate')?.value;
    const time = document.getElementById('appointmentTime')?.value;
    const indicator = document.getElementById('slotIndicator');

    if (!doctorId || !date || !time || !indicator) return;

    fetch(`/api/check-slot?doctorId=${doctorId}&date=${date}&time=${time}`)
        .then(r => r.json())
        .then(data => {
            indicator.style.display = 'block';
            if (data.available) {
                indicator.className = 'alert alert-success py-2 px-3 mt-2';
                indicator.innerHTML = '<i class="fas fa-check-circle me-1"></i> Slot is available!';
            } else {
                indicator.className = 'alert alert-danger py-2 px-3 mt-2';
                indicator.innerHTML = '<i class="fas fa-times-circle me-1"></i> This slot is already booked. Please choose another time.';
            }
        })
        .catch(() => { if(indicator) indicator.style.display = 'none'; });
}
