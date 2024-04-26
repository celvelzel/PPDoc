/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : ppdoc

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 26/04/2024 11:08:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_case
-- ----------------------------
DROP TABLE IF EXISTS `t_case`;
CREATE TABLE `t_case`  (
  `case_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '案件ID',
  `indictment_id` int(0) NULL DEFAULT NULL COMMENT '起诉状ID',
  `case_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '案件类型',
  `plaintiff_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原告姓名',
  `plaintiff_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原告ID',
  `plaintiff_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原告类型',
  `defendant_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被告姓名',
  `defendant_id` varbinary(20) NULL DEFAULT NULL COMMENT '被告ID',
  `defendant_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被告类型',
  `plaintiff_id_card_id` int(0) NULL DEFAULT NULL COMMENT '原告身份证ID',
  `defendant_id_card_id` int(0) NULL DEFAULT NULL COMMENT '被告身份证ID',
  `plaintiff_license_id` int(0) NULL DEFAULT NULL COMMENT '原告营业执照ID',
  `defendant_license_id` int(0) NULL DEFAULT NULL COMMENT '被告营业执照ID',
  `related_invoice_id` int(0) NULL DEFAULT NULL COMMENT '案件相关发票ID',
  PRIMARY KEY (`case_id`) USING BTREE,
  INDEX `indictment_id_fk`(`indictment_id`) USING BTREE,
  INDEX `plaintiff_id_card_fk`(`plaintiff_id_card_id`) USING BTREE,
  INDEX `plaintiff_license_fk`(`plaintiff_license_id`) USING BTREE,
  INDEX `defendant_id_card_fk`(`defendant_id_card_id`) USING BTREE,
  INDEX `defendant_license_fk`(`defendant_license_id`) USING BTREE,
  INDEX `related_invoice_id_fk`(`related_invoice_id`) USING BTREE,
  CONSTRAINT `defendant_id_card_fk` FOREIGN KEY (`defendant_id_card_id`) REFERENCES `id_card` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `defendant_license_fk` FOREIGN KEY (`defendant_license_id`) REFERENCES `license` (`license_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `indictment_id_fk` FOREIGN KEY (`indictment_id`) REFERENCES `indictment` (`indictment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `plaintiff_id_card_fk` FOREIGN KEY (`plaintiff_id_card_id`) REFERENCES `id_card` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `plaintiff_license_fk` FOREIGN KEY (`plaintiff_license_id`) REFERENCES `license` (`license_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `related_invoice_id_fk` FOREIGN KEY (`related_invoice_id`) REFERENCES `invoice` (`invoice_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
