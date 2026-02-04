# 🏢 DORMITORY MANAGEMENT SYSTEM (QUẢN LÝ KÝ TÚC XÁ)

![Java](https://img.shields.io/badge/Java-JDK_17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/Database-MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java_Swing-orange?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)

> **Đồ án môn học: Lập trình Java Nâng cao**
>
> Ứng dụng Desktop giúp số hóa quy trình quản lý Ký túc xá, từ việc xếp phòng, quản lý sinh viên đến tính toán điện nước và doanh thu.

---

## 📑 Mục lục
1. [Giới thiệu](#-giới-thiệu)
2. [Chức năng chính](#-chức-năng-chính)
3. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
4. [Cơ sở dữ liệu](#-cơ-sở-dữ-liệu)
5. [Cài đặt & Hướng dẫn chạy](#-cài-đặt--hướng-dẫn-chạy)
6. [Hình ảnh Demo](#-hình-ảnh-demo)
7. [Tác giả](#-tác-giả)

---

## 📖 Giới thiệu
Dự án được xây dựng nhằm giải quyết các bất cập trong quản lý thủ công tại các khu KTX. Hệ thống cung cấp giải pháp toàn diện để quản lý hồ sơ sinh viên, hợp đồng thuê phòng, theo dõi sự cố cơ sở vật chất và tự động hóa việc tính toán hóa đơn dịch vụ hàng tháng.

Ứng dụng được thiết kế theo mô hình **3-Layer (MVC)**, đảm bảo tính tách biệt giữa Giao diện (View), Nghiệp vụ (BUS) và Truy xuất dữ liệu (DAO).

---

## 🚀 Chức năng chính

### 1. Quản lý Lưu trú (Core Features)
* **Quản lý Phòng & Tầng:** Theo dõi trạng thái phòng (Trống/Đầy/Đang sửa), phân loại phòng VIP/Thường.
* **Xếp phòng thông minh:** Tự động kiểm tra sức chứa và phân loại giới tính (Nam tầng lẻ, Nữ tầng chẵn).
* **Hợp đồng:** Lập hợp đồng lưu trú, gia hạn hoặc thanh lý.
* **Chuyển phòng:** Hỗ trợ sinh viên chuyển phòng, tự động cập nhật sĩ số phòng cũ/mới.

### 2. Quản lý Dịch vụ & Tài chính
* **Điện - Nước:** Ghi chỉ số hàng tháng, tự động tính tiền theo đơn giá và xuất hóa đơn.
* **Gửi xe:** Quản lý đăng ký vé xe tháng cho sinh viên.
* **Thống kê Doanh thu:** Báo cáo tổng thu theo tháng/quý.

### 3. Quản lý Vận hành
* **Sự cố & Sửa chữa:** Ghi nhận báo hỏng từ sinh viên, theo dõi tiến độ sửa chữa và chi phí bảo trì.
* **Kỷ luật:** Ghi nhận vi phạm nội quy, hình thức xử lý (Cảnh cáo/Phạt tiền).
* **Tài sản:** Quản lý trang thiết bị trong từng phòng.

### 4. Hệ thống
* **Phân quyền:** Admin (Toàn quyền), Nhân viên (Tác vụ cơ bản), Bảo vệ (Xem sự cố/Xe).
* **Bảo mật:** Đăng nhập xác thực, mã hóa mật khẩu (tùy chọn).

---

## 🛠 Công nghệ sử dụng
| Thành phần | Công nghệ / Thư viện |
| :--- | :--- |
| **Ngôn ngữ** | Java (JDK 17) |
| **Giao diện** | Java Swing (JFrame, JPanel, JTable, CardLayout) |
| **Cơ sở dữ liệu** | MySQL |
| **Kết nối DB** | JDBC (Java Database Connectivity) |
| **Tiện ích** | JCalendar (Chọn ngày), iText (Xuất PDF), JFreeChart (Biểu đồ) |
| **IDE Phát triển** | Eclipse / IntelliJ IDEA / NetBeans |

---

## 💾 Cơ sở dữ liệu
Hệ thống sử dụng **MySQL** với các bảng chính (tham khảo file `quanlyktx.sql`):
* `sinhvien`: Hồ sơ sinh viên.
* `phong`, `tang`, `loaiphong`: Cấu trúc khu nhà.
* `hopdong`: Quản lý thuê phòng.
* `diennuoc`: Hóa đơn dịch vụ.
* `suco`: Theo dõi báo hỏng.
* `kyluat`: Theo dõi vi phạm.
* ... và các bảng khác.

---

## ⚙ Cài đặt & Hướng dẫn chạy

### Yêu cầu hệ thống
* Java Development Kit (JDK) 8 trở lên.
* MySQL Server (Khuyên dùng XAMPP hoặc MySQL Workbench).

### Các bước thực hiện
### Bước 1: Clone dự án
git clone [https://github.com/HuynhSang-IT/QuanLyKTX.git](https://github.com/HuynhSang-IT/QuanLyKTX.git).

Bước 2: Cấu hình Cơ sở dữ liệu
1. Mở phpMyAdmin hoặc MySQL Workbench.
2. Tạo database mới tên là: quanlyktx.
3. Import file quanlyktx.sql (nằm trong thư mục gốc dự án).

Bước 3: Cấu hình kết nối Java
1. Mở file src/com/quanlyktx/util/DatabaseHelper.java.
2. Cập nhật thông tin kết nối (nếu cần):
String url = "jdbc:mysql://localhost:3306/quanlyktx";
String user = "root";
String password = ""; // Điền mật khẩu MySQL của bạn vào đây

Bước 4: Chạy ứng dụng
1. Tìm file Main.java hoặc DangNhapView.java.
2. Chọn Run.
3. Tài khoản Admin mặc định:
User: admin
Pass: 123

📸 Hình ảnh Demo
1. Dashboard Tổng quan
Giao diện chính hiển thị thống kê phòng trống, sinh viên và doanh thu.
<img width="1669" height="941" alt="Screenshot 2026-01-28 123113" src="https://github.com/user-attachments/assets/73704a10-bcc7-43ee-a615-06841f54f33e" />


3. Quản lý Hợp đồng & Xếp phòng
Chức năng lập hợp đồng với logic kiểm tra điều kiện chặt chẽ (Giới tính, Sức chứa).

4. Quản lý Sự cố
Ghi nhận và cập nhật trạng thái sửa chữa cơ sở vật chất.

5. Hóa đơn Điện nước & Gửi xe
Tính toán tiền điện nước, quản lý gửi xe và xuất hóa đơn.

6. Quản lý Tài khoản
Quản lý danh sách nhân viên và phân quyền hệ thống.

