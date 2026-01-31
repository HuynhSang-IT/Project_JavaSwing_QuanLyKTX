-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th1 25, 2026 lúc 11:08 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `quanlyktx`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chuyenphong`
--

CREATE TABLE `chuyenphong` (
  `MaCP` int(11) NOT NULL,
  `MaSV` varchar(20) NOT NULL,
  `PhongCu` varchar(10) DEFAULT NULL,
  `PhongMoi` varchar(10) NOT NULL,
  `LyDo` text DEFAULT NULL,
  `NgayChuyen` date DEFAULT NULL,
  `NguoiThucHien` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chuyenphong`
--

INSERT INTO `chuyenphong` (`MaCP`, `MaSV`, `PhongCu`, `PhongMoi`, `LyDo`, `NgayChuyen`, `NguoiThucHien`) VALUES
(1, '220614', 'Phòng 101', 'Phòng 102', 'Khó ở', '2026-01-25', 'Quản Trị Viên');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `diennuoc`
--

CREATE TABLE `diennuoc` (
  `MaHD` int(11) NOT NULL,
  `MaPhong` varchar(10) NOT NULL,
  `ThangNam` varchar(10) NOT NULL,
  `TienPhong` decimal(15,0) DEFAULT 0,
  `TienDien` decimal(15,0) DEFAULT 0,
  `TienNuoc` decimal(15,0) DEFAULT 0,
  `SoDienCu` int(11) DEFAULT 0,
  `SoDienMoi` int(11) DEFAULT 0,
  `SoNuocCu` int(11) DEFAULT 0,
  `SoNuocMoi` int(11) DEFAULT 0,
  `ThanhTien` decimal(15,0) DEFAULT 0,
  `TrangThai` varchar(20) DEFAULT 'Chưa thu',
  `HinhThucTT` varchar(50) DEFAULT 'Chưa thanh toán'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `diennuoc`
--

INSERT INTO `diennuoc` (`MaHD`, `MaPhong`, `ThangNam`, `TienPhong`, `TienDien`, `TienNuoc`, `SoDienCu`, `SoDienMoi`, `SoNuocCu`, `SoNuocMoi`, `ThanhTien`, `TrangThai`, `HinhThucTT`) VALUES
(1, '102', '01/2026', 500000, 350000, 60000, 0, 100, 0, 10, 910000, 'Đã thu', 'Tiền mặt');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `guixe`
--

CREATE TABLE `guixe` (
  `MaVe` int(11) NOT NULL,
  `MaSV` varchar(20) DEFAULT NULL,
  `BienSo` varchar(20) DEFAULT NULL,
  `LoaiXe` varchar(20) DEFAULT NULL,
  `MauXe` varchar(50) DEFAULT NULL,
  `NgayDangKy` date DEFAULT NULL,
  `NgayHetHan` date DEFAULT NULL,
  `PhiThang` double DEFAULT NULL,
  `TrangThai` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hopdong`
--

CREATE TABLE `hopdong` (
  `MaHopDong` int(11) NOT NULL,
  `MaSV` varchar(20) DEFAULT NULL,
  `MaPhong` varchar(10) DEFAULT NULL,
  `NgayBatDau` date DEFAULT NULL,
  `NgayKetThuc` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `hopdong`
--

INSERT INTO `hopdong` (`MaHopDong`, `MaSV`, `MaPhong`, `NgayBatDau`, `NgayKetThuc`) VALUES
(1, '220614', '102', '2026-01-01', '2026-06-01'),
(2, '223344', '201', '2026-01-01', '2026-06-01');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `kyluat`
--

CREATE TABLE `kyluat` (
  `MaKL` int(11) NOT NULL,
  `MaSV` varchar(20) NOT NULL,
  `NoiDungVP` text NOT NULL,
  `HinhThucXuLy` varchar(100) DEFAULT NULL,
  `NgayViPham` date NOT NULL,
  `SoTienPhat` decimal(15,0) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaiphong`
--

CREATE TABLE `loaiphong` (
  `MaLoaiPhong` varchar(10) NOT NULL,
  `TenLoai` varchar(50) NOT NULL,
  `DonGia` decimal(10,0) DEFAULT 0,
  `SoNguoiToiDa` int(11) DEFAULT 8
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `loaiphong`
--

INSERT INTO `loaiphong` (`MaLoaiPhong`, `TenLoai`, `DonGia`, `SoNguoiToiDa`) VALUES
('LP01', 'Phòng Thường', 500000, 8),
('LP02', 'Phòng Dịch Vụ (VIP)', 1200000, 4);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhanvien`
--

CREATE TABLE `nhanvien` (
  `TenDangNhap` varchar(50) NOT NULL,
  `MatKhau` varchar(50) NOT NULL,
  `HoTen` varchar(50) NOT NULL,
  `Quyen` varchar(20) DEFAULT 'NV',
  `SDT` varchar(20) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nhanvien`
--

INSERT INTO `nhanvien` (`TenDangNhap`, `MatKhau`, `HoTen`, `Quyen`, `SDT`) VALUES
('admin', '123', 'Quản Trị Viên', 'Admin', '0901111111'),
('baove', '123', 'Bác Bảo Vệ', 'BaoVe', '0903333333'),
('nhanvien', '123', 'Nguyễn Văn A', 'NhanVien', '0902222222');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phong`
--

CREATE TABLE `phong` (
  `MaPhong` varchar(50) NOT NULL,
  `TenPhong` varchar(100) DEFAULT NULL,
  `LoaiPhong` varchar(50) DEFAULT NULL,
  `MaTang` int(11) DEFAULT NULL,
  `SoNguoiDangO` int(11) DEFAULT 0,
  `SoLuongMax` int(11) DEFAULT 8,
  `GiaPhong` decimal(15,0) DEFAULT NULL,
  `TrangThai` varchar(50) DEFAULT 'Còn trống'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `phong`
--

INSERT INTO `phong` (`MaPhong`, `TenPhong`, `LoaiPhong`, `MaTang`, `SoNguoiDangO`, `SoLuongMax`, `GiaPhong`, `TrangThai`) VALUES
('101', 'Phòng 101', 'Thường', 1, 0, 8, 500000, 'Đang ở'),
('102', 'Phòng 102', 'Thường', 1, 1, 8, 500000, 'Đang ở'),
('201', 'Phòng 201', 'Thường', 2, 1, 6, 500000, 'Đang ở');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sinhvien`
--

CREATE TABLE `sinhvien` (
  `MaSV` varchar(20) NOT NULL,
  `HoTen` varchar(50) NOT NULL,
  `MaPhong` varchar(50) DEFAULT NULL,
  `CMND` varchar(20) NOT NULL,
  `GioiTinh` varchar(10) DEFAULT NULL,
  `SDT` varchar(15) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `QueQuan` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sinhvien`
--

INSERT INTO `sinhvien` (`MaSV`, `HoTen`, `MaPhong`, `CMND`, `GioiTinh`, `SDT`, `Email`, `QueQuan`) VALUES
('220614', 'Trần Huỳnh Sang', '102', '0912265214', 'Nam', '0944924860', 'sang@gmail.com', 'Kiên Giang'),
('223344', 'Lê Thị P', '201', '0123465787', 'Nữ', '092876654', 'P@gmail.com', 'Hà Nội'),
('225566', 'Nguyễn Văn C', NULL, '098765422', 'Nam', '09876123', 'C@gmail.com', 'TP.HCM');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `suco`
--

CREATE TABLE `suco` (
  `MaSuCo` int(11) NOT NULL,
  `MaPhong` varchar(10) NOT NULL,
  `NguoiBao` varchar(100) DEFAULT NULL,
  `MoTa` text NOT NULL,
  `NgayBao` date NOT NULL,
  `TrangThai` varchar(50) DEFAULT 'Chưa xử lý',
  `ChiPhi` decimal(15,0) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `taisan`
--

CREATE TABLE `taisan` (
  `MaTS` int(11) NOT NULL,
  `MaPhong` varchar(10) NOT NULL,
  `TenTaiSan` varchar(100) NOT NULL,
  `SoLuong` int(11) DEFAULT 1,
  `TinhTrang` varchar(50) DEFAULT 'Tốt',
  `GhiChu` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tang`
--

CREATE TABLE `tang` (
  `MaTang` int(11) NOT NULL,
  `TenTang` varchar(50) NOT NULL,
  `GhiChu` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `tang`
--

INSERT INTO `tang` (`MaTang`, `TenTang`, `GhiChu`) VALUES
(1, 'Tầng 1', 'Tối đa 8 người'),
(2, 'Tầng 2', 'Tối đa 6 người');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chuyenphong`
--
ALTER TABLE `chuyenphong`
  ADD PRIMARY KEY (`MaCP`),
  ADD KEY `fk_chuyenphong_sv` (`MaSV`);

--
-- Chỉ mục cho bảng `diennuoc`
--
ALTER TABLE `diennuoc`
  ADD PRIMARY KEY (`MaHD`),
  ADD KEY `MaPhong` (`MaPhong`);

--
-- Chỉ mục cho bảng `guixe`
--
ALTER TABLE `guixe`
  ADD PRIMARY KEY (`MaVe`),
  ADD KEY `MaSV` (`MaSV`);

--
-- Chỉ mục cho bảng `hopdong`
--
ALTER TABLE `hopdong`
  ADD PRIMARY KEY (`MaHopDong`),
  ADD KEY `MaSV` (`MaSV`),
  ADD KEY `MaPhong` (`MaPhong`);

--
-- Chỉ mục cho bảng `kyluat`
--
ALTER TABLE `kyluat`
  ADD PRIMARY KEY (`MaKL`),
  ADD KEY `MaSV` (`MaSV`);

--
-- Chỉ mục cho bảng `loaiphong`
--
ALTER TABLE `loaiphong`
  ADD PRIMARY KEY (`MaLoaiPhong`);

--
-- Chỉ mục cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`TenDangNhap`);

--
-- Chỉ mục cho bảng `phong`
--
ALTER TABLE `phong`
  ADD PRIMARY KEY (`MaPhong`),
  ADD KEY `fk_phong_tang` (`MaTang`);

--
-- Chỉ mục cho bảng `sinhvien`
--
ALTER TABLE `sinhvien`
  ADD PRIMARY KEY (`MaSV`),
  ADD UNIQUE KEY `CMND` (`CMND`),
  ADD KEY `fk_sv_phong` (`MaPhong`);

--
-- Chỉ mục cho bảng `suco`
--
ALTER TABLE `suco`
  ADD PRIMARY KEY (`MaSuCo`),
  ADD KEY `MaPhong` (`MaPhong`);

--
-- Chỉ mục cho bảng `taisan`
--
ALTER TABLE `taisan`
  ADD PRIMARY KEY (`MaTS`),
  ADD KEY `MaPhong` (`MaPhong`);

--
-- Chỉ mục cho bảng `tang`
--
ALTER TABLE `tang`
  ADD PRIMARY KEY (`MaTang`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `chuyenphong`
--
ALTER TABLE `chuyenphong`
  MODIFY `MaCP` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `diennuoc`
--
ALTER TABLE `diennuoc`
  MODIFY `MaHD` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `guixe`
--
ALTER TABLE `guixe`
  MODIFY `MaVe` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `hopdong`
--
ALTER TABLE `hopdong`
  MODIFY `MaHopDong` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `kyluat`
--
ALTER TABLE `kyluat`
  MODIFY `MaKL` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `suco`
--
ALTER TABLE `suco`
  MODIFY `MaSuCo` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `taisan`
--
ALTER TABLE `taisan`
  MODIFY `MaTS` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `tang`
--
ALTER TABLE `tang`
  MODIFY `MaTang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chuyenphong`
--
ALTER TABLE `chuyenphong`
  ADD CONSTRAINT `fk_chuyenphong_sv` FOREIGN KEY (`MaSV`) REFERENCES `sinhvien` (`MaSV`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `diennuoc`
--
ALTER TABLE `diennuoc`
  ADD CONSTRAINT `diennuoc_ibfk_1` FOREIGN KEY (`MaPhong`) REFERENCES `phong` (`MaPhong`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `guixe`
--
ALTER TABLE `guixe`
  ADD CONSTRAINT `guixe_ibfk_1` FOREIGN KEY (`MaSV`) REFERENCES `sinhvien` (`MaSV`);

--
-- Các ràng buộc cho bảng `hopdong`
--
ALTER TABLE `hopdong`
  ADD CONSTRAINT `hopdong_ibfk_1` FOREIGN KEY (`MaSV`) REFERENCES `sinhvien` (`MaSV`) ON DELETE CASCADE,
  ADD CONSTRAINT `hopdong_ibfk_2` FOREIGN KEY (`MaPhong`) REFERENCES `phong` (`MaPhong`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `kyluat`
--
ALTER TABLE `kyluat`
  ADD CONSTRAINT `kyluat_ibfk_1` FOREIGN KEY (`MaSV`) REFERENCES `sinhvien` (`MaSV`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `phong`
--
ALTER TABLE `phong`
  ADD CONSTRAINT `fk_phong_tang` FOREIGN KEY (`MaTang`) REFERENCES `tang` (`MaTang`) ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `sinhvien`
--
ALTER TABLE `sinhvien`
  ADD CONSTRAINT `fk_sv_phong` FOREIGN KEY (`MaPhong`) REFERENCES `phong` (`MaPhong`) ON DELETE SET NULL;

--
-- Các ràng buộc cho bảng `suco`
--
ALTER TABLE `suco`
  ADD CONSTRAINT `suco_ibfk_1` FOREIGN KEY (`MaPhong`) REFERENCES `phong` (`MaPhong`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `taisan`
--
ALTER TABLE `taisan`
  ADD CONSTRAINT `taisan_ibfk_1` FOREIGN KEY (`MaPhong`) REFERENCES `phong` (`MaPhong`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
