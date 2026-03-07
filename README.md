📦 MyInventory API - Sistem Manajemen Inventori & Transaksi

MyInventory API adalah sistem backend RESTful untuk mengelola inventori produk, siklus pemesanan, dan transaksi pembayaran. Proyek ini menggunakan Java Spring Boot dengan fokus utama pada integritas data, keamanan, dan analisis bisnis.

✨ Fitur Utama

🔐 Keamanan & Autentikasi

Enterprise JWT Security: Implementasi JWT yang disimpan di dalam HttpOnly Cookie untuk mencegah serangan XSS (Cross-Site Scripting).
Role-Based Access Control (RBAC): Pemisahan akses antara ADMIN (manajemen data & verifikasi) dan USER (belanja & riwayat transaksi).

🛒 Sistem Transaksi & Inventori

Transactional Stock Handling: Penggunaan @Transactional untuk memastikan stok dipotong secara akurat saat checkout dan dikembalikan otomatis (auto-restock) jika pesanan dibatalkan atau ditolak.
Multipart Image Upload: Integrasi pengunggahan gambar produk dan bukti pembayaran langsung ke sistem penyimpanan server.
Soft Delete Architecture: Data tidak dihapus secara fisik dari database guna menjaga jejak audit (Audit Trail) untuk kepentingan akuntansi.

📊 Dashboard & Analisis (Admin Only)

Executive Reporting: Rekapitulasi total pendapatan dari pesanan yang berhasil.
Inventory Alerts: Peringatan otomatis untuk produk dengan stok rendah di bawah 10 unit.
Top Selling Analytics: Identifikasi 5 produk paling laris menggunakan JPA Projections untuk performa query yang optimal.

🛠️ Tech Stack

Framework: Java Spring Boot 3.x
Database: MySQL 8.0
ORM: Spring Data JPA (Hibernate)
Security: Spring Security & JWT
Build Tool: Maven
Documentation: Postman API Documentation
Other: Lombok, Java Stream API, Jakarta Validation

📁 Struktur Proyek

src/main/java/com/project/myinventory/

├── config/         # Konfigurasi Security, JWT, & CORS

├── controller/     # REST API Endpoints

├── service/        # Logika Bisnis & Transaksi

├── repository/     # Akses Database (JPA Repository)

├── model/

│   ├── entity/     # Database Tables (JPA Entities)

│   └── enums/      # Status (Order, Payment, Role)

├── dto/            # Data Transfer Objects & Projections

├── exception/      # Global Exception Handling

└── util/           # Helper Classes



🔌 API Endpoints (Ringkasan)

/api/v1/auth/login

/api/v1/products

/api/v1/orders

/api/v1/payments/{id}/upload-proof

/api/v1/admin/payments/{id}/approve

/api/v1/admin/reports



📈 Alur Bisnis (Business Flow)

Admin mengelola produk dan stok.

User melakukan checkout -> Sistem memotong stok secara otomatis.

User mengunggah bukti pembayaran.

Admin melakukan verifikasi:

Jika Approve: Pesanan selesai, dana masuk ke pendapatan.

Jika Reject: Pesanan batal, stok dikembalikan ke inventori secara otomatis.

Admin memantau performa melalui laporan Top Selling dan Low Stock.


