/*
 Navicat Premium Dump SQL

 Source Server         : WAMP-MySQL
 Source Server Type    : MySQL
 Source Server Version : 80407 (8.4.7)
 Source Host           : localhost:3306
 Source Schema         : webdacsanvungmienvn

 Target Server Type    : MySQL
 Target Server Version : 80407 (8.4.7)
 File Encoding         : 65001

 Date: 15/06/2026 21:35:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for banners
-- ----------------------------
DROP TABLE IF EXISTS `banners`;
CREATE TABLE `banners`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of banners
-- ----------------------------
INSERT INTO `banners` VALUES (7, 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779388571/banners/deadkookzcsqmp2mkw7p.png');
INSERT INTO `banners` VALUES (8, 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779388580/banners/h2hbxqqxdllhsh9cpxll.webp');
INSERT INTO `banners` VALUES (9, 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779388589/banners/lc8rspfolfmnpj6aztsv.webp');

-- ----------------------------
-- Table structure for brands
-- ----------------------------
DROP TABLE IF EXISTS `brands`;
CREATE TABLE `brands`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of brands
-- ----------------------------
INSERT INTO `brands` VALUES (1, 'Vinamit', 'Trái cây sấy Việt Nam');
INSERT INTO `brands` VALUES (2, 'Trung Nguyên', 'Cà phê Việt Nam');
INSERT INTO `brands` VALUES (3, 'Vinacafe', 'Cà phê hòa tan');
INSERT INTO `brands` VALUES (4, 'Bibica', 'Bánh kẹo Việt Nam');
INSERT INTO `brands` VALUES (5, 'Kinh Đô', 'Bánh kẹo KIDO');
INSERT INTO `brands` VALUES (6, 'Lương Quới', 'Đặc sản Bến Tre');
INSERT INTO `brands` VALUES (7, 'Bích Chi', 'Đặc sản Đồng Tháp');
INSERT INTO `brands` VALUES (8, 'Vissan', 'Thực phẩm chế biến');
INSERT INTO `brands` VALUES (9, 'Cholimex', 'Gia vị Việt Nam');
INSERT INTO `brands` VALUES (10, 'Nam Ngư', 'Nước mắm Masan');
INSERT INTO `brands` VALUES (11, 'Hạ Long', 'Đồ hộp Hạ Long');
INSERT INTO `brands` VALUES (12, 'Phú Quốc Khải Hoàn', 'Nước mắm Phú Quốc');
INSERT INTO `brands` VALUES (13, 'Miliket', 'Mì ăn liền 2 con tôm');
INSERT INTO `brands` VALUES (14, 'Acecook', 'Mì Hảo Hảo');
INSERT INTO `brands` VALUES (15, 'Tân Huê Viên', 'Bánh pía Sóc Trăng');

-- ----------------------------
-- Table structure for category_discounts
-- ----------------------------
DROP TABLE IF EXISTS `category_discounts`;
CREATE TABLE `category_discounts`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `discount_percentage` int NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  CONSTRAINT `category_discounts_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `product_categories` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `category_discounts_chk_1` CHECK ((`discount_percentage` >= 0) and (`discount_percentage` <= 100))
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category_discounts
-- ----------------------------

-- ----------------------------
-- Table structure for email_verification_tokens
-- ----------------------------
DROP TABLE IF EXISTS `email_verification_tokens`;
CREATE TABLE `email_verification_tokens`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `expiry_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `token`(`token` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `email_verification_tokens_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of email_verification_tokens
-- ----------------------------

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `author` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `published_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of news
-- ----------------------------

-- ----------------------------
-- Table structure for order_items
-- ----------------------------
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `product_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `product_quantity` int NOT NULL DEFAULT 1,
  `product_price` decimal(10, 0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id`(`order_id` ASC) USING BTREE,
  INDEX `product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `order_items_chk_1` CHECK (`product_quantity` > 0),
  CONSTRAINT `order_items_chk_2` CHECK (`product_price` >= 0)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_items
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `status` enum('Đang xử lý','Đang giao','Đã giao','Đã hủy') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Đang xử lý',
  `payment_method` enum('COD','Online') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'COD',
  `full_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `phone_number` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total` decimal(10, 0) NOT NULL DEFAULT 0,
  `note` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `signature` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `key_id` int NULL DEFAULT NULL,
  `signed_at` datetime NULL DEFAULT NULL,
  `signature_algorithm` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SHA256withDSA',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_orders_key_id`(`key_id` ASC) USING BTREE,
  CONSTRAINT `fk_orders_key_id` FOREIGN KEY (`key_id`) REFERENCES `user_keys` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for password_reset_tokens
-- ----------------------------
DROP TABLE IF EXISTS `password_reset_tokens`;
CREATE TABLE `password_reset_tokens`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `expiry_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `token`(`token` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `password_reset_tokens_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of password_reset_tokens
-- ----------------------------

-- ----------------------------
-- Table structure for product_categories
-- ----------------------------
DROP TABLE IF EXISTS `product_categories`;
CREATE TABLE `product_categories`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_categories
-- ----------------------------
INSERT INTO `product_categories` VALUES (1, 'Trái cây sấy');
INSERT INTO `product_categories` VALUES (2, 'Bánh kẹo đặc sản');
INSERT INTO `product_categories` VALUES (3, 'Đồ khô');
INSERT INTO `product_categories` VALUES (4, 'Gia vị');
INSERT INTO `product_categories` VALUES (5, 'Đồ hộp');
INSERT INTO `product_categories` VALUES (6, 'Thức uống');

-- ----------------------------
-- Table structure for product_discounts
-- ----------------------------
DROP TABLE IF EXISTS `product_discounts`;
CREATE TABLE `product_discounts`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `discount_percentage` int NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `product_discounts_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `product_discounts_chk_1` CHECK ((`discount_percentage` >= 0) and (`discount_percentage` <= 100))
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_discounts
-- ----------------------------

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `price` decimal(10, 0) NOT NULL,
  `brand_id` int NOT NULL,
  `category_id` int NOT NULL,
  `specification` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `quantity` int NOT NULL DEFAULT 0,
  `sold_quantity` int NOT NULL DEFAULT 0,
  `region` enum('Miền Bắc','Miền Trung','Miền Nam') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  INDEX `brand_id`(`brand_id` ASC) USING BTREE,
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `product_categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `products_ibfk_2` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `products_chk_1` CHECK (`price` >= 0),
  CONSTRAINT `products_chk_2` CHECK (`quantity` >= 0),
  CONSTRAINT `products_chk_3` CHECK (`sold_quantity` >= 0)
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES (1, 'Mít sấy Vinamit', 55000, 1, 1, '200g', 90, 20, 'Miền Nam', '<p>M&iacute;t <em>sấy </em><strong>gi&ograve;n</strong></p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779112505/productThumbnails/g9hqvdlhctiy8a2zqh4g.png', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (2, 'Chuối sấy Vinamit', 50000, 1, 1, '200g', 100, 5, 'Miền Nam', '<p>Chuối sấy ngọt</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779112574/productThumbnails/mhxl70vrskvsfw5zzymx.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (3, 'Khoai lang sấy', 48000, 1, 1, '200g', 100, 4, 'Miền Nam', '<p>Khoai lang sấy</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779112810/productThumbnails/odomjyvuttoq8w4lfku8.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (4, 'Sầu riêng sấy', 120000, 1, 1, '150g', 80, 8, 'Miền Nam', '<p>Sầu ri&ecirc;ng sấy</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779112920/productThumbnails/r9ggjnpe3gtcdfeqjki7.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (5, 'Mãng cầu sấy', 60000, 1, 1, '150g', 88, 5, 'Miền Nam', '<p>M&atilde;ng cầu sấy</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779113010/productThumbnails/us3vbochydabblhkmqfv.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (6, 'Cafe Trung Nguyên', 75000, 2, 6, '500g', 150, 20, 'Miền Trung', '<p>C&agrave; ph&ecirc; rang xay</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779113072/productThumbnails/a17nfaug7j2ii9r4ulnp.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (7, 'Cafe Sáng Tạo 1', 80000, 2, 6, '500g', 120, 15, 'Miền Trung', '<p>D&ograve;ng s&aacute;ng tạo</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779113419/productThumbnails/acqknedjczadpu9bluxh.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (8, 'Vinacafe 3in1', 65000, 3, 6, '20 gói', 200, 30, 'Miền Nam', '<p>Cafe h&ograve;a tan</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779113463/productThumbnails/jkldjtu4taz9bnk69ytf.webp', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (9, 'G7 3in1', 70000, 2, 6, '21 gói', 200, 25, 'Miền Trung', '<p>Cafe G7</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779113519/productThumbnails/vynqsiflycg3shmsopnp.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (10, 'Bánh pía sầu riêng', 95000, 15, 2, '6 cái', 120, 12, 'Miền Nam', '<p>B&aacute;nh p&iacute;a S&oacute;c Trăng</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779113564/productThumbnails/hrxohyztcd3lwkiyag1m.webp', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (11, 'Bánh đậu xanh', 40000, 4, 2, '200g', 100, 9, 'Miền Bắc', '<p>Đậu xanh Hải Dương</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779113642/productThumbnails/idnovnq9upjqpegm3zog.webp', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (12, 'Kẹo dừa Bến Tre', 45000, 6, 2, '250g', 100, 10, 'Miền Nam', '<p>Kẹo dừa</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779113835/productThumbnails/haencoe0arqrk1urswi7.webp', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (13, 'Bánh cốm Hà Nội', 55000, 4, 2, '300g', 90, 7, 'Miền Bắc', '<p>B&aacute;nh cốm</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779115561/productThumbnails/gf69y6l4fbdz7ztsvzgy.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (14, 'Bánh in Huế', 35000, 4, 2, '200g', 80, 5, 'Miền Trung', '<p>B&aacute;nh in</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779115743/productThumbnails/rxlavvaajerxq9crjbvi.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (15, 'Nước mắm Nam Ngư', 30000, 10, 4, '500ml', 200, 30, 'Miền Nam', '<p>Nước mắm</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779115814/productThumbnails/ybwpsmc2dkrwmhzpox8m.webp', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (16, 'Mắm nêm Huế', 45000, 9, 4, '400ml', 120, 10, 'Miền Trung', '<p>Mắm n&ecirc;m</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779116302/productThumbnails/engon94wmnls9pm9h72a.webp', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (17, 'Muối tôm Tây Ninh', 35000, 9, 4, '250g', 140, 14, 'Miền Nam', '<p>Muối t&ocirc;m</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779117487/productThumbnails/daozteuxevm4pbx07cqa.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (18, 'Tương ớt Cholimex', 28000, 9, 4, '500g', 200, 22, 'Miền Nam', '<p>Tương ớt</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779117608/productThumbnails/namzbfr8xaiazxyg1et4.webp', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (19, 'Nước mắm Phú Quốc', 90000, 12, 4, '500ml', 150, 18, 'Miền Nam', '<p>Nước mắm nhĩ</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779117697/productThumbnails/xkny708lzkvjq1iwdlid.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (20, 'Khô bò miếng', 120000, 8, 3, '200g', 90, 8, 'Miền Nam', '<p>Kh&ocirc; b&ograve;</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779117755/productThumbnails/cyl5wsqygg0rdd0c0flb.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (21, 'Khô gà lá chanh', 100000, 8, 3, '250g', 110, 12, 'Miền Nam', '<p>Kh&ocirc; g&agrave;</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779117791/productThumbnails/m0h3tihqadsekx2mdrle.png', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (22, 'Tôm khô Cà Mau', 180000, 8, 3, '200g', 70, 6, 'Miền Nam', '<p>T&ocirc;m kh&ocirc;</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779117933/productThumbnails/yzqamvpk4292wvdphlyo.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (23, 'Mực khô Phan Thiết', 200000, 8, 3, '300g', 60, 5, 'Miền Trung', '<p>Mực kh&ocirc;</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779117870/productThumbnails/dqngvos19id2jfjm5f7k.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (24, 'Cá chỉ vàng khô', 150000, 8, 3, '300g', 75, 7, 'Miền Trung', '<p>C&aacute; kh&ocirc;</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779118011/productThumbnails/xywfv1tn9ufa2hkbd13c.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (25, 'Cá hộp Hạ Long', 30000, 11, 5, '155g', 200, 25, 'Miền Bắc', '<p>C&aacute; hộp</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779118143/productThumbnails/m6hb4oeil7fkemypcbot.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (26, 'Pate Hạ Long', 28000, 11, 5, '170g', 180, 20, 'Miền Bắc', '<p>Pate gan</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779118202/productThumbnails/hrguuwh5mplqyrvjakqo.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (27, 'Xúc xích tiệt trùng', 25000, 8, 5, '200g', 150, 15, 'Miền Nam', '<p>X&uacute;c x&iacute;ch</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779118241/productThumbnails/uh3yykdp1t84brtdfyqn.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (28, 'Mì Miliket', 3500, 13, 3, '1 gói', 500, 100, 'Miền Nam', '<p>M&igrave; 2 con t&ocirc;m</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779118264/productThumbnails/hd3nnuyszxxgoi1wv8h9.jpg', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (29, 'Mì Hảo Hảo', 4000, 14, 3, '1 gói', 500, 120, 'Miền Nam', '<p>M&igrave; chua cay</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779118294/productThumbnails/yz51tkezxcuejfkknkgo.webp', '2026-01-28 09:28:10');
INSERT INTO `products` VALUES (30, 'Bánh tráng Tây Ninh', 30000, 9, 2, '200g', 150, 15, 'Miền Nam', '<p>B&aacute;nh tr&aacute;ng dẻo</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779118406/productThumbnails/j9inbvsnta40jszwd6og.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (31, 'Bánh tráng me', 35000, 9, 2, '200g', 119, 13, 'Miền Nam', '<p>B&aacute;nh tr&aacute;ng me</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779118441/productThumbnails/ebskcxm6ury4u7vgru74.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (32, 'Bánh tráng muối', 32000, 9, 2, '200g', 130, 10, 'Miền Nam', '<p>B&aacute;nh tr&aacute;ng muối</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779118469/productThumbnails/mi4ox7k7hennvs5wgdaj.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (33, 'Mứt dừa Bến Tre', 60000, 6, 2, '300g', 72, 37, 'Miền Nam', '<p>Mứt dừa</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779118497/productThumbnails/fxw1uf1z3bcu6xhca3bz.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (34, 'Mứt gừng Huế', 50000, 4, 2, '250g', 90, 7, 'Miền Trung', '<p>Mứt gừng</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779118536/productThumbnails/cysggon5ndblt3r4ryjq.webp', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (35, 'Mứt sen Hà Nội', 65000, 4, 2, '300g', 80, 6, 'Miền Bắc', '<p>Mứt sen</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779118951/productThumbnails/buleywbe4ifhzq5c4220.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (36, 'Trà sen Tây Hồ', 120000, 4, 6, '100g', 69, 6, 'Miền Bắc', '<p>Tr&agrave; sen</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119044/productThumbnails/soop7rtnsjqsmjgknzoc.webp', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (37, 'Trà atiso Đà Lạt', 90000, 4, 6, '200g', 100, 11, 'Miền Trung', '<p>Tr&agrave; atiso</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119143/productThumbnails/bhkmldzazhf2568nryby.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (38, 'Trà lài khô', 50000, 4, 6, '200g', 110, 8, 'Miền Nam', '<p>Tr&agrave; l&agrave;i</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119185/productThumbnails/kuvqazuj8cfbczndoqji.webp', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (39, 'Nem chua Thanh Hóa', 70000, 8, 3, '10 cái', 116, 18, 'Miền Bắc', '<p>Nem chua</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119239/productThumbnails/yhaw8xatk5y5bosysiku.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (40, 'Chả lụa Huế', 85000, 8, 3, '500g', 90, 9, 'Miền Trung', '<p>Chả lụa</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119267/productThumbnails/zgvvq00jxhdq7iuqnynr.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (41, 'Chả bò Đà Nẵng', 120000, 8, 3, '500g', 80, 6, 'Miền Trung', '<p>Chả b&ograve;</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119306/productThumbnails/o6fxbfvluhgbqxibgc9h.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (42, 'Cá cơm khô', 90000, 8, 3, '300g', 85, 7, 'Miền Trung', '<p>C&aacute; cơm kh&ocirc;</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119351/productThumbnails/vkodue5gcscqv8d8igza.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (43, 'Ruốc tôm Huế', 110000, 8, 3, '250g', 70, 6, 'Miền Trung', '<p>Ruốc t&ocirc;m</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119460/productThumbnails/fdqkmmbfjpeesuospxgh.webp', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (44, 'Khô cá lóc', 140000, 8, 3, '400g', 60, 5, 'Miền Nam', '<p>Kh&ocirc; c&aacute; l&oacute;c</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119499/productThumbnails/ndtyval08v1a2sql1dq7.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (45, 'Sữa đậu nành hộp', 25000, 11, 6, '6 lon', 150, 15, 'Miền Nam', '<p>Sữa đậu n&agrave;nh</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119522/productThumbnails/epnqpxqu4k8logfdtzqm.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (46, 'Nước sâm bí đao', 30000, 11, 6, '6 lon', 140, 10, 'Miền Nam', '<p>Nước s&acirc;m</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119595/productThumbnails/s3xdtwayuioilpksp4o8.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (47, 'Trà bí đao Wonder', 28000, 11, 6, '6 chai', 130, 11, 'Miền Nam', '<p>Tr&agrave; b&iacute; đao</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119628/productThumbnails/lrit42juio5vtdgakvak.webp', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (48, 'Bánh cáy Thái Bình', 55000, 4, 2, '300g', 90, 8, 'Miền Bắc', '<p>B&aacute;nh c&aacute;y</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119683/productThumbnails/uv6at7uupmkbequyviay.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (49, 'Bánh gai Hải Dương', 50000, 4, 2, '10 cái', 85, 6, 'Miền Bắc', '<p>B&aacute;nh gai</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119725/productThumbnails/fdbj5tnpt5iq238aoxr5.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (50, 'Bánh thuẫn Quảng', 45000, 4, 2, '250g', 95, 7, 'Miền Trung', '<p>B&aacute;nh thuẫn</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119791/productThumbnails/vy4oh9yntm77v7w31mmr.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (51, 'Bánh đậu xanh nướng', 42000, 4, 2, '200g', 100, 8, 'Miền Trung', '<p>Đậu xanh nướng</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119857/productThumbnails/xuxjmevk7tez23le1jjq.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (52, 'Đường thốt nốt', 60000, 6, 3, '500g', 90, 7, 'Miền Nam', '<p>Đường thốt nốt</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119945/productThumbnails/uuhpmtkrc9krdejgvxlb.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (53, 'Cốm dẹp Trà Vinh', 70000, 6, 3, '300g', 75, 5, 'Miền Nam', '<p>Cốm dẹp</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119920/productThumbnails/oyoaapvkja6x6redygfk.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (54, 'Hạt điều rang muối', 150000, 6, 3, '500g', 100, 12, 'Miền Nam', '<p>Hạt điều</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119993/productThumbnails/vzizbrs7jourwjzbgqtf.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (55, 'Sa tế tôm', 45000, 9, 4, '200g', 120, 13, 'Miền Nam', '<p>Sa tế</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779119983/productThumbnails/ntzmxx8fzat6hvqcf3ht.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (56, 'Tiêu sọ Phú Quốc', 90000, 12, 4, '200g', 80, 6, 'Miền Nam', '<p>Ti&ecirc;u sọ</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779120035/productThumbnails/en6vbfl0hv7lgqexgolq.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (57, 'Cafe rang xay S', 85000, 2, 6, '500g', 100, 10, 'Miền Trung', '<p>Cafe nguy&ecirc;n chất</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779120055/productThumbnails/sa2qv1h0ifayqxwq8g1a.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (58, 'Cafe Robusta', 80000, 2, 6, '500g', 90, 9, 'Miền Trung', '<p>Robusta</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779120004/productThumbnails/dhq9qqk2sssprstr40bp.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (59, 'Bắp rang bơ', 25000, 4, 2, '200g', 130, 10, 'Miền Nam', '<p>Bắp rang</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779120077/productThumbnails/pk0jefqk1y3xkrtaycwe.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (60, 'Đậu phộng da cá', 30000, 4, 2, '250g', 120, 12, 'Miền Nam', '<p>Đậu phộng</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779120101/productThumbnails/dnlzlxiwyc07a8xq4zjf.webp', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (61, 'Mắm ruốc Huế', 45000, 9, 4, '400g', 100, 9, 'Miền Trung', '<p>Mắm <strong>trưng</strong></p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779120179/productThumbnails/veu65cqhdjhfc80hbbj4.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (62, 'Mắm cá linh', 70000, 9, 4, '500g', 85, 7, 'Miền Nam', '<p>Mắm c&aacute; linh</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779120166/productThumbnails/wdfh4qqgv92hht9jqn3o.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (63, 'Ổi sấy dẻo', 55000, 1, 1, '200g', 90, 6, 'Miền Nam', '<p>Ổi sấy</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779120220/productThumbnails/kpg5mr125zxm8ofpgbkw.webp', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (64, 'Thơm sấy dẻo', 60000, 1, 1, '200g', 85, 5, 'Miền Nam', '<p>Thơm sấy</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779120237/productThumbnails/l886iq4pafnaivsdhadj.webp', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (65, 'Thanh long sấy', 65000, 1, 1, '200g', 80, 5, 'Miền Nam', '<p>Thanh long</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779120265/productThumbnails/lyuwgqxzxzb8wgv6bfbt.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (66, 'Khô heo cháy tỏi', 130000, 8, 3, '250g', 75, 6, 'Miền Nam', '<p>Kh&ocirc; heo</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779120192/productThumbnails/gxyhwryjcx99klao3u7i.jpg', '2026-01-28 09:29:17');
INSERT INTO `products` VALUES (67, 'Lạp xưởng tươi', 160000, 8, 3, '500g', 70, 6, 'Miền Nam', '<p>Lạp xưởng</p>', 'https://res.cloudinary.com/dxbuueljs/image/upload/v1779120203/productThumbnails/iymopbkezv8fg6bqs8lc.jpg', '2026-01-28 09:29:17');

-- ----------------------------
-- Table structure for reviews
-- ----------------------------
DROP TABLE IF EXISTS `reviews`;
CREATE TABLE `reviews`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `user_id` int NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `rating` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `product_id`(`product_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `reviews_chk_1` CHECK (`rating` between 1 and 5)
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of reviews
-- ----------------------------

-- ----------------------------
-- Table structure for shipping_infos
-- ----------------------------
DROP TABLE IF EXISTS `shipping_infos`;
CREATE TABLE `shipping_infos`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `receiver_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `receiver_phone_number` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `province` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `district` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `ward` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `detailed_address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `shipping_infos_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of shipping_infos
-- ----------------------------
INSERT INTO `shipping_infos` VALUES (4, 15, 'Lưu Quốc Pháp', '0921955395', 'Thành phố Hải Phòng', 'Huyện Cát Hải', 'Xã Hiền Hào', '75/49/11/8 ĐAPĐ09');

-- ----------------------------
-- Table structure for user_keys
-- ----------------------------
DROP TABLE IF EXISTS `user_keys`;
CREATE TABLE `user_keys`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `public_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `algorithm` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DSA',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `revoked_at` datetime NULL DEFAULT NULL,
  `status` enum('ACTIVE','REVOKED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_keys_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_user_keys_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_keys
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `full_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `phone_number` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `gender` enum('Nam','Nữ','Không xác định') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Không xác định',
  `date_of_birth` date NULL DEFAULT NULL,
  `role` enum('admin','user') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'user',
  `provider` enum('LOCAL','GOOGLE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'LOCAL',
  `google_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_verified` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `google_id`(`google_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin@gmail.com', '25d55ad283aa400af464c76d713c07ad', 'Administrator', NULL, 'Không xác định', NULL, 'admin', 'LOCAL', NULL, 1);
INSERT INTO `users` VALUES (15, '23130227@st.hcmuaf.edu.vn', NULL, 'Pháp Lưu Quốc', NULL, 'Không xác định', NULL, 'user', 'GOOGLE', '113675882092221451479', 1);

SET FOREIGN_KEY_CHECKS = 1;
