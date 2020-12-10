/*
 Navicat Premium Data Transfer

 Source Server         : mysqlLocal
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : campuservice

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 04/12/2020 22:10:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for my_order
-- ----------------------------
DROP TABLE IF EXISTS `my_order`;
CREATE TABLE `my_order`  (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `start` int(5) NULL DEFAULT NULL,
  `destination` int(5) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `size` int(5) NULL DEFAULT NULL,
  `price` int(10) NULL DEFAULT NULL,
  `poster_id` int(11) NULL DEFAULT NULL,
  `accepter_id` int(11) NULL DEFAULT NULL,
  `post_time` datetime(0) NULL DEFAULT NULL,
  `accept_time` datetime(0) NULL DEFAULT NULL,
  `confirm_time` datetime(0) NULL DEFAULT NULL,
  `finish_time` datetime(0) NULL DEFAULT NULL,
  `status` int(5) NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `poster_id`(`poster_id`) USING BTREE,
  INDEX `accepter_id`(`accepter_id`) USING BTREE,
  CONSTRAINT `my_order_ibfk_1` FOREIGN KEY (`poster_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `my_order_ibfk_2` FOREIGN KEY (`accepter_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of my_order
-- ----------------------------
INSERT INTO `my_order` VALUES (1, 0, 1, 'description', 0, 3, 2, 1, '2020-12-02 00:00:00', NULL, NULL, NULL, 0);
INSERT INTO `my_order` VALUES (2, 0, 1, 'description', 0, 3, 2, 1, '2020-12-02 14:23:35', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `balance` int(10) NOT NULL DEFAULT 0,
  `student_id` int(11) NOT NULL,
  `student_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'u1', '123123', 0, 1, 's1');
INSERT INTO `user` VALUES (2, 'user1', '123123', 0, 1111111111, 's1');

SET FOREIGN_KEY_CHECKS = 1;
