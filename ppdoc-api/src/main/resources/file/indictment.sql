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

 Date: 20/04/2024 15:51:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for indictment
-- ----------------------------
DROP TABLE IF EXISTS `indictment`;
CREATE TABLE `indictment`  (
  `indictment_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '起诉状id',
  `document_id` int(0) NOT NULL COMMENT '文档id',
  `case_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '案件类型',
  `plaintiff_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原告姓名',
  `plaintiff_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原告身份代码，根据企业或个人分别为统一社会信用代码和身份证号',
  `plaintiff_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原告类型，企业或个人',
  `plaintiff_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原告地址',
  `plaintiff_contact` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原告联系方式',
  `defendant_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '被告姓名',
  `defendant_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被告身份代码',
  `defendant_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被告类型',
  `defendant_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被告地址',
  `defendant_contact` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被告联系方式',
  `litigation_request` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '诉讼请求',
  `facts_background` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '事实背景',
  `legal_basis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '法律依据',
  `evidence_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '证据清单',
  `court_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '法院名称',
  `indictment_date` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起诉状日期',
  PRIMARY KEY (`indictment_id`) USING BTREE,
  INDEX `indictment_document_document_id_fk`(`document_id`) USING BTREE,
  CONSTRAINT `indictment_document_document_id_fk` FOREIGN KEY (`document_id`) REFERENCES `document` (`document_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
