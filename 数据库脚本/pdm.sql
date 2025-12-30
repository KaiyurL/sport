/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : pdm

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 30/12/2025 17:07:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `userpwd` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `headurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (3, 'admin', '123456', '管理员', '男', '18888888888', 'nopic.png');
INSERT INTO `admin` VALUES (4, 'ad3', '156', '管理员', '男', '18888888888', 'nopic.png');

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID，主键',
  `cfg_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置键，唯一',
  `cfg_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `cfg_key`(`cfg_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 'system_version', '1.0.0', '系统版本', '2025-12-26 23:38:43');
INSERT INTO `config` VALUES (2, 'calorie_calculation_method', 'auto', '卡路里计算方式：auto-自动，manual-手动', '2025-12-26 23:38:43');

-- ----------------------------
-- Table structure for health_data
-- ----------------------------
DROP TABLE IF EXISTS `health_data`;
CREATE TABLE `health_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '健康数据ID，主键',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键关联user表',
  `weight_kg` decimal(5, 2) NULL DEFAULT NULL COMMENT '体重（公斤）',
  `height_cm` decimal(5, 2) NULL DEFAULT NULL COMMENT '身高（厘米）',
  `bmi` decimal(4, 2) NULL DEFAULT NULL COMMENT '身体质量指数',
  `heart_rate_bpm` int NULL DEFAULT NULL COMMENT '心率（次/分钟）',
  `measure_time` datetime NOT NULL COMMENT '测量时间',
  `notes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_health_user_time`(`user_id` ASC, `measure_time` ASC) USING BTREE,
  CONSTRAINT `fk_health_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '健康指标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of health_data
-- ----------------------------
INSERT INTO `health_data` VALUES (1, 1, 70.00, 170.00, 24.22, 80, '2025-12-27 00:59:20', '', '2025-12-27 00:59:34', '2025-12-27 00:59:34');

-- ----------------------------
-- Table structure for sport_record
-- ----------------------------
DROP TABLE IF EXISTS `sport_record`;
CREATE TABLE `sport_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID，主键',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键关联user表',
  `sport_type_id` bigint NOT NULL COMMENT '运动类型ID，外键关联sport_type表',
  `sport_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运动类型名称（冗余存储，便于查询）',
  `duration_min` int NOT NULL COMMENT '运动时长（分钟）',
  `calories` decimal(10, 2) NOT NULL COMMENT '消耗卡路里',
  `record_time` datetime NOT NULL COMMENT '记录时间',
  `notes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_record_user_time`(`user_id` ASC, `record_time` ASC) USING BTREE,
  INDEX `idx_record_type_time`(`sport_type_id` ASC, `record_time` ASC) USING BTREE,
  CONSTRAINT `fk_record_type` FOREIGN KEY (`sport_type_id`) REFERENCES `sport_type` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_record_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '运动记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sport_record
-- ----------------------------
INSERT INTO `sport_record` VALUES (1, 1, 1, '跑步', 424, 111.00, '2025-12-27 00:02:00', '', '2025-12-27 00:58:54', '2025-12-27 00:59:10');
INSERT INTO `sport_record` VALUES (2, 1, 2, '游泳', 1, 221.00, '2025-12-24 00:00:00', '', '2025-12-30 16:12:19', '2025-12-30 16:12:19');
INSERT INTO `sport_record` VALUES (3, 1, 3, '骑行', 0, 320.00, '2025-12-29 00:00:01', '', '2025-12-30 16:14:00', '2025-12-30 16:14:00');
INSERT INTO `sport_record` VALUES (4, 1, 4, '健身', 2, 0.00, '2025-12-30 16:11:58', '', '2025-12-30 16:14:15', '2025-12-30 16:14:15');
INSERT INTO `sport_record` VALUES (5, 1, 1, '跑步', 1, 0.00, '2025-12-09 00:02:00', '', '2025-12-30 16:14:40', '2025-12-30 16:14:40');
INSERT INTO `sport_record` VALUES (6, 1, 1, '跑步', 3, 320.00, '2025-12-10 00:02:00', '', '2025-12-30 16:15:00', '2025-12-30 16:15:00');
INSERT INTO `sport_record` VALUES (7, 1, 3, '骑行', 60, 223.00, '2025-12-11 02:00:00', '', '2025-12-30 16:15:24', '2025-12-30 16:15:24');
INSERT INTO `sport_record` VALUES (8, 1, 2, '游泳', 360, 233.00, '2025-12-12 11:00:00', '', '2025-12-30 16:15:56', '2025-12-30 16:15:56');
INSERT INTO `sport_record` VALUES (9, 1, 1, '跑步', 120, 0.00, '2025-12-30 16:11:58', '', '2025-12-30 16:16:27', '2025-12-30 16:16:27');
INSERT INTO `sport_record` VALUES (10, 1, 2, '游泳', 60, 223.00, '2025-12-30 16:11:58', '', '2025-12-30 16:16:38', '2025-12-30 16:16:38');
INSERT INTO `sport_record` VALUES (11, 1, 1, '跑步', 420, 4542.00, '2025-12-30 16:11:58', '', '2025-12-30 16:17:00', '2025-12-30 16:17:00');

-- ----------------------------
-- Table structure for sport_type
-- ----------------------------
DROP TABLE IF EXISTS `sport_type`;
CREATE TABLE `sport_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '运动类型ID，主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '运动类型名称，唯一',
  `calorie_per_hour` decimal(10, 2) NOT NULL COMMENT '每小时消耗卡路里',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE,
  INDEX `idx_sport_type_name`(`name` ASC) USING BTREE COMMENT '运动类型名称索引'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '运动类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sport_type
-- ----------------------------
INSERT INTO `sport_type` VALUES (1, '跑步', 600.00, '户外或跑步机跑步', '2025-12-26 23:38:43', '2025-12-26 23:38:43');
INSERT INTO `sport_type` VALUES (2, '游泳', 500.00, '自由泳、蛙泳等', '2025-12-26 23:38:43', '2025-12-26 23:38:43');
INSERT INTO `sport_type` VALUES (3, '骑行', 400.00, '自行车骑行', '2025-12-26 23:38:43', '2025-12-26 23:38:43');
INSERT INTO `sport_type` VALUES (4, '健走', 300.00, '快走或散步', '2025-12-26 23:38:43', '2025-12-26 23:38:43');
INSERT INTO `sport_type` VALUES (5, '瑜伽', 200.00, '各种瑜伽练习', '2025-12-26 23:38:43', '2025-12-26 23:38:43');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` enum('male','female','other') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'other',
  `status` tinyint NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE,
  INDEX `idx_user_username`(`username` ASC) USING BTREE,
  INDEX `idx_user_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123456', '18088889999', 'bai', 'male', 1, '2025-12-27 00:44:10', '2025-12-27 00:44:10');
INSERT INTO `user` VALUES (2, '123', '123', '123', NULL, 'other', 1, '2025-12-30 15:37:06', '2025-12-30 15:37:06');
INSERT INTO `user` VALUES (3, 'huangxinyu', '123456', '123456', NULL, 'other', 1, '2025-12-30 16:01:50', '2025-12-30 16:01:50');
INSERT INTO `user` VALUES (6, 'huanggaoyuan', '123456', '12345', NULL, 'other', 1, '2025-12-30 16:02:36', '2025-12-30 16:02:36');
INSERT INTO `user` VALUES (7, 'quishaoman', '123456', '1234567', NULL, 'other', 1, '2025-12-30 16:02:53', '2025-12-30 16:02:53');
INSERT INTO `user` VALUES (8, 'hehuanyu', '123456', '12345678', NULL, 'other', 1, '2025-12-30 16:03:22', '2025-12-30 16:03:22');
INSERT INTO `user` VALUES (9, 'Heather Ferguson', 'cclyonoGpj', '80-5877-0265', 'Heather Ferguson', 'male', 4, '2007-02-10 05:48:30', '2007-08-24 00:33:20');
INSERT INTO `user` VALUES (10, 'Fang Xiuying', 'kP3PBAyPcz', '3-9308-6413', 'Fang Xiuying', 'female', 39, '2020-02-18 13:34:20', '2001-05-10 07:57:47');
INSERT INTO `user` VALUES (11, 'Jamie Gibson', 'Q8lTZvcQy7', '198-3555-6826', 'Jamie Gibson', 'male', 1, '2018-03-07 08:22:14', '2008-11-03 03:57:01');
INSERT INTO `user` VALUES (12, 'Takeuchi Misaki', 'TfmfWdAPTZ', '718-049-3470', 'Takeuchi Misaki', 'other', 123, '2011-12-05 10:12:10', '2021-10-25 08:13:19');
INSERT INTO `user` VALUES (13, 'Yam Chi Yuen', 'NaVvqBMqVy', '3-1609-4336', 'Yam Chi Yuen', 'other', 41, '2015-12-03 15:25:09', '2015-02-23 22:35:13');
INSERT INTO `user` VALUES (14, 'Lo Wing Kuen', 'S7S4GQsc1D', '213-738-8665', 'Lo Wing Kuen', 'male', 96, '2008-02-15 09:17:12', '2000-10-23 02:21:53');
INSERT INTO `user` VALUES (15, 'Ti Ling Ling', 'RSlv0oHzlR', '(1223) 86 4286', 'Ti Ling Ling', 'other', 73, '2019-12-09 12:26:11', '2021-01-31 11:23:17');
INSERT INTO `user` VALUES (16, 'Tsui Yun Fat', 'riqsZpCeqT', '330-516-1997', 'Tsui Yun Fat', 'male', 58, '2005-09-30 15:10:52', '2017-10-25 02:50:27');
INSERT INTO `user` VALUES (17, 'Liu Ziyi', 'b0GNzaKHcI', '212-873-1903', 'Liu Ziyi', 'female', 107, '2011-01-16 10:23:46', '2022-04-13 09:22:35');
INSERT INTO `user` VALUES (18, 'Inoue Ayano', 'dRaLsg3gWF', '769-8312-0808', 'Inoue Ayano', 'female', 96, '2020-01-15 14:51:26', '2025-07-05 18:10:27');
INSERT INTO `user` VALUES (19, 'Zhang Lan', 'rr0xz619to', '718-347-7272', 'Zhang Lan', 'female', 98, '2025-08-17 03:26:20', '2006-03-17 15:52:23');
INSERT INTO `user` VALUES (20, 'Qiu Shihan', 'BROMts4Q0Y', '153-5885-8075', 'Qiu Shihan', 'other', 38, '2023-11-29 08:11:43', '2003-07-06 06:46:18');
INSERT INTO `user` VALUES (21, 'Jesus Perez', 'ecytKJECqu', '7740 470745', 'Jesus Perez', 'male', 101, '2013-04-28 19:51:23', '2010-07-08 01:09:07');
INSERT INTO `user` VALUES (22, 'Alan Gibson', 'sUtd9eSDrR', '178-2471-5449', 'Alan Gibson', 'male', 35, '2018-07-14 17:36:11', '2006-01-23 10:43:41');
INSERT INTO `user` VALUES (23, 'Cao Jiehong', 'BBPoeJ4Gml', '312-697-4763', 'Cao Jiehong', 'female', 73, '2016-12-30 19:06:57', '2007-12-13 04:36:38');
INSERT INTO `user` VALUES (24, 'Lu Anqi', 'ykdS2s80Se', '212-227-0734', 'Lu Anqi', 'female', 96, '2010-06-06 23:45:07', '2000-04-12 08:49:29');
INSERT INTO `user` VALUES (25, 'Chang Ka Fai', '0GMtXPbDwt', '180-9271-4472', 'Chang Ka Fai', 'male', 33, '2025-02-23 01:51:23', '2022-09-19 11:20:01');
INSERT INTO `user` VALUES (26, 'Uchida Rin', 'CknuvE7MoH', '718-969-8414', 'Uchida Rin', 'male', 76, '2009-12-18 06:17:34', '2011-05-21 09:40:38');
INSERT INTO `user` VALUES (27, 'Nishimura Ayano', 'FkxNDwwuML', '7603 988113', 'Nishimura Ayano', 'female', 88, '2007-12-23 00:42:51', '2004-04-07 21:45:19');
INSERT INTO `user` VALUES (28, 'April Romero', '9uLsv01sZH', '70-4016-7175', 'April Romero', 'other', 32, '2022-02-03 06:50:26', '2025-11-26 14:51:00');
INSERT INTO `user` VALUES (29, 'testuser123', 'testpass123', NULL, NULL, 'other', 1, '2025-12-30 16:42:05', '2025-12-30 16:42:05');

SET FOREIGN_KEY_CHECKS = 1;
