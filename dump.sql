-- Tạo cơ sở dữ liệu demojsp với mã hóa utf8mb4
CREATE DATABASE IF NOT EXISTS jsp3 CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE jsp3;

-- Đảm bảo kết nối sử dụng utf8mb4
SET NAMES utf8mb4;

-- Tạo bảng provinces (các tỉnh/thành) với mã hóa utf8mb4
CREATE TABLE `provinces` (
                             `idProvince` INT AUTO_INCREMENT PRIMARY KEY,
                             `nameProvince` VARCHAR(100) NOT NULL,
                             `note` VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Thêm dữ liệu mẫu vào bảng provinces
INSERT INTO `provinces` (nameProvince, note) VALUES
                                                 ('An Giang', 'Tỉnh'),
                                                 ('Bà Rịa - Vũng Tàu', 'Tỉnh'),
                                                 ('Bắc Giang', 'Tỉnh'),
                                                 ('Bắc Kạn', 'Tỉnh'),
                                                 ('Bạc Liêu', 'Tỉnh'),
                                                 ('Bắc Ninh', 'Tỉnh'),
                                                 ('Bến Tre', 'Tỉnh'),
                                                 ('Bình Định', 'Tỉnh'),
                                                 ('Bình Dương', 'Tỉnh'),
                                                 ('Bình Phước', 'Tỉnh'),
                                                 ('Bình Thuận', 'Tỉnh'),
                                                 ('Cà Mau', 'Tỉnh'),
                                                 ('Cần Thơ', 'Thành phố trực thuộc trung ương'),
                                                 ('Cao Bằng', 'Tỉnh'),
                                                 ('Đà Nẵng', 'Thành phố trực thuộc trung ương'),
                                                 ('Đắk Lắk', 'Tỉnh'),
                                                 ('Đắk Nông', 'Tỉnh'),
                                                 ('Điện Biên', 'Tỉnh'),
                                                 ('Đồng Nai', 'Tỉnh'),
                                                 ('Đồng Tháp', 'Tỉnh'),
                                                 ('Gia Lai', 'Tỉnh'),
                                                 ('Hà Giang', 'Tỉnh'),
                                                 ('Hà Nam', 'Tỉnh'),
                                                 ('Hà Nội', 'Thành phố trực thuộc trung ương'),
                                                 ('Hà Tĩnh', 'Tỉnh'),
                                                 ('Hải Dương', 'Tỉnh'),
                                                 ('Hải Phòng', 'Thành phố trực thuộc trung ương'),
                                                 ('Hậu Giang', 'Tỉnh'),
                                                 ('Hòa Bình', 'Tỉnh'),
                                                 ('Hưng Yên', 'Tỉnh'),
                                                 ('Khánh Hòa', 'Tỉnh'),
                                                 ('Kiên Giang', 'Tỉnh'),
                                                 ('Kon Tum', 'Tỉnh'),
                                                 ('Lai Châu', 'Tỉnh'),
                                                 ('Lâm Đồng', 'Tỉnh'),
                                                 ('Lạng Sơn', 'Tỉnh'),
                                                 ('Lào Cai', 'Tỉnh'),
                                                 ('Long An', 'Tỉnh'),
                                                 ('Nam Định', 'Tỉnh'),
                                                 ('Nghệ An', 'Tỉnh'),
                                                 ('Ninh Bình', 'Tỉnh'),
                                                 ('Ninh Thuận', 'Tỉnh'),
                                                 ('Phú Thọ', 'Tỉnh'),
                                                 ('Phú Yên', 'Tỉnh'),
                                                 ('Quảng Bình', 'Tỉnh'),
                                                 ('Quảng Nam', 'Tỉnh'),
                                                 ('Quảng Ngãi', 'Tỉnh'),
                                                 ('Quảng Ninh', 'Tỉnh'),
                                                 ('Quảng Trị', 'Tỉnh'),
                                                 ('Sóc Trăng', 'Tỉnh'),
                                                 ('Sơn La', 'Tỉnh'),
                                                 ('Tây Ninh', 'Tỉnh'),
                                                 ('Thái Bình', 'Tỉnh'),
                                                 ('Thái Nguyên', 'Tỉnh'),
                                                 ('Thanh Hóa', 'Tỉnh'),
                                                 ('Thừa Thiên Huế', 'Tỉnh'),
                                                 ('Tiền Giang', 'Tỉnh'),
                                                 ('TP Hồ Chí Minh', 'Thành phố trực thuộc trung ương'),
                                                 ('Trà Vinh', 'Tỉnh'),
                                                 ('Tuyên Quang', 'Tỉnh'),
                                                 ('Vĩnh Long', 'Tỉnh'),
                                                 ('Vĩnh Phúc', 'Tỉnh'),
                                                 ('Yên Bái', 'Tỉnh');

-- Tạo bảng users (cập nhật thêm các trường email, ngày sinh, nơi ở và avatar)
CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `created_at` datetime(6) DEFAULT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         `role` varchar(255) DEFAULT NULL,
                         `username` varchar(255) DEFAULT NULL,
                         `email` varchar(255) NOT NULL UNIQUE,
                         `birth_date` DATE NOT NULL,
                         `province_id` INT NOT NULL,
                         `avatar` VARCHAR(255),
                         PRIMARY KEY (`id`),
                         FOREIGN KEY (`province_id`) REFERENCES `provinces`(`idProvince`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Thêm dữ liệu mẫu vào bảng users
INSERT INTO `users` (`id`, `created_at`, `password`, `role`, `username`, `email`, `birth_date`, `province_id`, `avatar`) VALUES
                                                                                                                             (1, NOW(6), '0123456789', 'ADMIN', 'admin', 'admin@example.com', '2000-01-01', 1, 'admin.jpg'),
                                                                                                                             (2, NOW(6), '0123456789', 'USER', 'admin1', 'admin1@example.com', '2005-05-15', 2, 'admin1.jpg'),
                                                                                                                             (3, NOW(6), '0123456789', 'USER', 'admin2', 'admin2@example.com', '1990-07-20', 3, 'admin2.jpg'),
                                                                                                                             (4, NOW(6), '0123456789', 'USER', 'admin3', 'admin3@example.com', '1988-09-10', 4, 'admin3.jpg'),
                                                                                                                             (5, NOW(6), '0123456789', 'USER', 'admin4', 'admin4@example.com', '1995-12-30', 5, 'admin4.jpg'),
                                                                                                                             (6, NOW(6), '0123456789', 'USER', 'admin5', 'admin5@example.com', '2003-03-25', 6, 'admin5.jpg'),
                                                                                                                             (7, NOW(6), '0123456789', 'USER', 'admin6', 'admin6@example.com', '2004-07-19', 7, 'admin6.jpg'),
                                                                                                                             (8, NOW(6), '0123456789', 'USER', 'admin7', 'admin7@example.com', '1992-04-15', 8, 'admin7.jpg'),
                                                                                                                             (9, NOW(6), '0123456789', 'USER', 'admin8', 'admin8@example.com', '1998-01-10', 9, 'admin8.jpg'),
                                                                                                                             (10, NOW(6), '0123456789', 'USER', 'admin9', 'admin9@example.com', '2000-02-14', 10, 'admin9.jpg');

-- Tạo bảng posts
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `body` varchar(255) DEFAULT NULL,
                         `created_at` datetime(6) DEFAULT NULL,
                         `deleted_at` datetime(6) DEFAULT NULL,
                         `status` varchar(255) DEFAULT NULL,
                         `title` varchar(255) DEFAULT NULL,
                         `updated_at` datetime(6) DEFAULT NULL,
                         `user_id` bigint NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `FK5lidm6cqbc7u4xhqpxm898qme` (`user_id`),
                         CONSTRAINT `FK5lidm6cqbc7u4xhqpxm898qme` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Thêm dữ liệu mẫu vào bảng posts
INSERT INTO `posts` (`id`, `body`, `created_at`, `deleted_at`, `status`, `title`, `updated_at`, `user_id`) VALUES
                                                                                                               (1, 'hhhhhhhhhhhhhhhhhhhh', '2025-02-26 22:25:25.205013', '2025-02-27 09:30:40.157793', 'DELETED', 'hhhhhhhhhhhhhhhhh', '2025-02-26 22:25:25.205013', 1),
                                                                                                               (2, 'da fix', '2025-02-26 22:35:23.121498', '2025-02-27 09:30:36.962457', 'DELETED', 'da fix', '2025-02-26 22:35:23.121498', 4),
                                                                                                               (3, 'sua lai loi thanh cong', '2025-02-27 10:05:04.958183', '2025-02-27 10:33:59.964648', 'DELETED', 'da fix', '2025-02-27 10:33:59.964648', 3),
                                                                                                               (4, 'fix dc chuc nang dang nhap', '2025-02-27 10:05:39.168257', '2025-02-27 22:13:09.760769', 'DELETED', 'can fix', '2025-02-27 10:05:39.168348', 4),
                                                                                                               (5, 'da xong', '2025-02-27 10:06:55.736285', '2025-02-27 10:08:02.794805', 'DELETED', 'da xong ', '2025-02-27 10:08:02.794805', 6),
                                                                                                               (6, 'dasdas', '2025-02-27 10:33:42.159741', '2025-02-27 10:34:01.770829', 'DELETED', 'da xong ', '2025-02-27 10:34:01.770829', 3),
                                                                                                               (7, 'dada', '2025-02-27 10:34:12.710531', '2025-02-27 22:13:06.655070', 'DELETED', 'da fix', '2025-02-27 10:34:12.710531', 3),
                                                                                                               (8, 'dad', '2025-02-27 10:34:38.448945', '2025-02-27 10:35:22.355868', 'DELETED', 'da', '2025-02-27 10:35:22.355868', 7),
                                                                                                               (9, 'dasdas', '2025-02-27 11:18:25.262401', '2025-02-27 22:13:04.265086', 'DELETED', 'hhhhhhhhhhhhhhhhh', '2025-02-27 11:18:25.262401', 8),
                                                                                                               (10, 'sdads', '2025-02-27 22:12:39.867258', '2025-02-27 22:13:00.932564', 'DELETED', 'hhhhhhhhhhhhhhhhh', '2025-02-27 22:12:39.867258', 7),
                                                                                                               (11, 'xin chao day la tesst', '2025-02-28 20:41:17.166991', NULL, 'ACTIVE', 'iu riot', '2025-03-09 22:29:38.275403', 3),
                                                                                                               (12, 'ddd', '2025-02-28 20:42:53.373566', NULL, 'ACTIVE', 'da xong ', '2025-02-28 20:42:53.373566', 7),
                                                                                                               (13, 'gckcyvyvuvugbibbiuyuiuvuviuvub', '2025-03-11 14:32:04.911135', NULL, 'ACTIVE', 'hhhhhhhhhhhhhhhhh', '2025-03-11 14:32:12.998142', 7),
                                                                                                               (14, 'hhhhhhhhhhhhhhhhh', '2025-03-11 14:34:33.587073', NULL, 'ACTIVE', 'hhhhhhhhhhhhhhhhh', '2025-03-11 14:34:33.587073', 4),
                                                                                                               (15, 'cdcddxcx', '2025-03-11 14:35:06.879623', NULL, 'ACTIVE', 'da fix', '2025-03-11 14:35:06.879623', 3),
                                                                                                               (16, 'dfdvfdcscscscscfsc', '2025-03-11 14:35:18.667987', NULL, 'ACTIVE', 'hhhhhhhhhhhhhhhhh', '2025-03-11 14:35:18.667987', 3),
                                                                                                               (17, 'dadaddas', '2025-03-19 22:54:10.401403', NULL, 'ACTIVE', 'da xong ', '2025-03-19 22:54:10.401403', 7),
                                                                                                               (19, 'da', '2025-03-30 11:18:51.957026', NULL, 'ACTIVE', 'xin chao ngay moi', '2025-03-30 11:18:51.957026', 3),
                                                                                                               (20, 'da', '2025-03-30 17:05:45.891928', NULL, 'ACTIVE', 'da', '2025-03-30 17:05:45.891928', 7),
                                                                                                               (21, '123', '2025-04-14 23:36:54.003967', NULL, 'ACTIVE', 'de', '2025-04-14 23:36:54.003967', 1),
                                                                                                               (22, 'da', '2025-04-15 20:28:55.201662', NULL, 'ACTIVE', 'de', '2025-04-15 20:28:55.201662', 2);

-- Tạo bảng follows (quan hệ theo dõi giữa người dùng)
DROP TABLE IF EXISTS `follows`;
CREATE TABLE `follows` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `follower_id` bigint DEFAULT NULL,
                           `following_id` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKqnkw0cwwh6572nyhvdjqlr163` (`follower_id`),
                           KEY `FKonkdkae2ngtx70jqhsh7ol6uq` (`following_id`),
                           CONSTRAINT `FKonkdkae2ngtx70jqhsh7ol6uq` FOREIGN KEY (`following_id`) REFERENCES `users` (`id`),
                           CONSTRAINT `FKqnkw0cwwh6572nyhvdjqlr163` FOREIGN KEY (`follower_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Thêm dữ liệu mẫu vào bảng follows
INSERT INTO `follows` (follower_id, following_id) VALUES
                                                      (3,2),  -- admin1 (id=2) được theo dõi bởi id=3
                                                      (1,2),  -- admin1 được theo dõi bởi id=1
                                                      (4,2),  -- admin1 được theo dõi bởi id=4
                                                      (5,2),  -- admin1 được theo dõi bởi id=5
                                                      (6,2),  -- admin1 được theo dõi bởi id=6
                                                      (7,2),  -- admin1 được theo dõi bởi id=7
                                                      (6,5),  -- Giữ một số bản ghi khác
                                                      (7,6),
                                                      (8,8),
                                                      (9,9),
                                                      (10,9);