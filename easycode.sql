/*
Navicat MySQL Data Transfer

Source Server         : 192.168.100.8
Source Server Version : 80011
Source Host           : 192.168.100.8:5509
Source Database       : easycode

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2019-09-30 10:50:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dbconfig
-- ----------------------------
DROP TABLE IF EXISTS `dbconfig`;
CREATE TABLE `dbconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dbType` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `database` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `characterEncoding` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dbconfig
-- ----------------------------
INSERT INTO `dbconfig` VALUES ('1', 'MySql', '192.168.100.8', '5509', 'mysql', 'root', 'suncompass', 'utf-8');
INSERT INTO `dbconfig` VALUES ('2', 'MySql', '192.168.100.8', '5509', 'garden', 'root', 'suncompass', 'utf-8');
INSERT INTO `dbconfig` VALUES ('3', 'MySql', '120.79.23.201', '3306', 'emergency', 'root', 'suncompass123456', 'utf-8');
INSERT INTO `dbconfig` VALUES ('4', 'MySql', '192.168.100.8', '5509', 'exam', 'root', 'suncompass', 'utf-8');

-- ----------------------------
-- Table structure for template
-- ----------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE `template` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `templateName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板名称',
  `fileName` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件名',
  `templatePath` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '模板地址',
  `groupId` varchar(10) NOT NULL COMMENT '组ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `Fk_ID` (`groupId`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of template
-- ----------------------------
INSERT INTO `template` VALUES ('29', 'List.vue.vm', 'List', '/template/custom/List_1e94e8eaa7c94f9a8ad7dd5ac6d6c9ab.vue.vm', '1');
INSERT INTO `template` VALUES ('30', 'Detail.vue.vm', 'Detail', '/template/custom/Detail_d971411aa26740688f402de3de22b6b4.vue.vm', '1');
