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
**Bước 1: Clone dự án**
```bash
git clone [https://github.com/TenGitHubCuaBan/QuanLyKTX.git](https://github.com/TenGitHubCuaBan/QuanLyKTX.git)
**Bước 2: Cấu hình Cơ sở dữ liệu
Mở phpMyAdmin hoặc MySQL Workbench.
Tạo database mới tên là: quanlyktx.
Import file quanlyktx.sql (nằm trong thư mục gốc dự án).
**Bước 3: Cấu hình kết nối Java
Mở file src/com/quanlyktx/util/DatabaseHelper.java.
Cập nhật thông tin kết nối (nếu cần): Java
String url = "jdbc:mysql://localhost:3306/quanlyktx";
String user = "root";
String password = ""; // Mật khẩu MySQL của bạn
**Bước 4: Chạy ứng dụng
Tìm file Main.java hoặc DangNhapView.java và chọn Run.
Tài khoản Admin mặc định:
User: admin
Pass: 123.
1. Dashboard Tổng quan
Giao diện chính hiển thị thống kê phòng trống, sinh viên và doanh thu.
<img width="1669" height="941" alt="Screenshot 2026-01-28 123113" src="https://github.com/user-attachments/assets/62722aff-47fb-48ef-99d4-3aa96381a084" />

2. Quản lý Hợp đồng & Xếp phòng
Chức năng lập hợp đồng với logic kiểm tra điều kiện chặt chẽ.
<img width="1669" height="939" alt="Screenshot 2026-01-28 123122" src="https://github.com/user-attachments/assets/5e2cd524-8d00-4910-bdb6-3749dcd3c65c" />
<img width="1671" height="944" alt="Screenshot 2026-01-28 123131" src="https://github.com/user-attachments/assets/032f1cc0-4b39-4358-a987-d9f812ca3a68" />
<img width="1666" height="938" alt="Screenshot 2026-01-28 123140" src="https://github.com/user-attachments/assets/f7474184-c28d-4516-90f0-ebd135f51d29" />
<img width="1671" height="944" alt="Screenshot 2026-01-28 123150" src="https://github.com/user-attachments/assets/82d7d102-5189-481a-9f0a-1052046fcfe7" />
<img width="1669" height="943" alt="Screenshot 2026-01-28 123159" src="https://github.com/user-attachments/assets/fb3b163a-c036-45c2-bcec-1bc86432b57b" />

3. Quản lý Sự cố
Ghi nhận và cập nhật trạng thái sửa chữa cơ sở vật chất.
<img width="1675" height="946" alt="Screenshot 2026-01-28 123232" src="https://github.com/user-attachments/assets/d19f2d7b-04b0-4993-b6d9-d71d7d7059e8" />
<img width="1666" height="944" alt="Screenshot 2026-01-28 123239" src="https://github.com/user-attachments/assets/bfee5211-811d-48fc-ad0c-b3dd8728662a" />
<img width="1666" height="944" alt="Screenshot 2026-01-28 123239" src="https://github.com/user-attachments/assets/a6562cd3-a4a2-4cca-b791-45dcc145ee3f" />
<img width="1672" height="945" alt="Screenshot 2026-01-28 123246" src="https://github.com/user-attachments/assets/40638075-8972-46fa-af1d-093c0a76e152" />

4. Hóa đơn Điện nước
Tính toán tiền điện nước, gửi xe và xuất hóa đơn.
<img width="1669" height="936" alt="Screenshot 2026-01-28 123206" src="https://github.com/user-attachments/assets/fdd2fe22-4e74-4f81-bd43-d896f88b6548" />
<img width="1668" height="936" alt="Screenshot 2026-01-28 123215" src="https://github.com/user-attachments/assets/61f7bfd9-df9b-44e9-be87-f2912013b75f" />
<img width="1669" height="941" alt="Screenshot 2026-01-28 123224" src="https://github.com/user-attachments/assets/84370cd0-7b8f-4538-8b63-1ebc3da16576" />
<img width="1669" height="942" alt="Screenshot 2026-01-28 123254" src="https://github.com/user-attachments/assets/13251151-1516-42ac-a860-afe542dc1dd6" />
5. Quản lý tài khoản
<img width="1676" height="951" alt="Screenshot 2026-01-28 123301" src="https://github.com/user-attachments/assets/b08cf560-ffc7-432e-9002-3bb65851b4a1" />
👨‍💻 Tác giả
Trần Huỳnh Sang (Developer Chính)
Email: sang123567tqs@gmail.com
GitHub: [github.com/HuynhSang-IT]


