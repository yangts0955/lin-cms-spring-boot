-- ----------------------------
-- 文件表
-- ----------------------------
DROP TABLE IF EXISTS t_enum_map;
CREATE TABLE t_enum_map
(
    id          int(10) unsigned NOT NULL AUTO_INCREMENT,
    type        int(10)          NOT NULL   COMMENT '枚举类型 1.性别,2.年级,3.课程状态,4.身份,5.学科',
    name        varchar(30)      NOT NULL   COMMENT '枚举名',
    value       varchar(30)      NOT NULL   COMMENT '显示文字',
    create_time datetime(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_time datetime(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    delete_time datetime(3)               DEFAULT NULL,
    is_deleted  tinyint(1)                DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


INSERT INTO t_enum_map(id, type, name, value) VALUES (1, 1, 'MALE', '男');
INSERT INTO t_enum_map(id, type, name, value) VALUES (2, 1, 'FEMALE', '女');
INSERT INTO t_enum_map(id, type, name, value) VALUES (3, 1, 'UNKNOWN', '未知');
INSERT INTO t_enum_map(id, type, name, value) VALUES (4, 2, 'FIRST_GRADE', '一年级');
INSERT INTO t_enum_map(id, type, name, value) VALUES (5, 2, 'SECOND_GRADE', '二年级');
INSERT INTO t_enum_map(id, type, name, value) VALUES (6, 2, 'THIRD_GRADE', '三年级');
INSERT INTO t_enum_map(id, type, name, value) VALUES (7, 2, 'FOURTH_GRADE', '四年级');
INSERT INTO t_enum_map(id, type, name, value) VALUES (8, 2, 'FIFTH_GRADE', '五年级');
INSERT INTO t_enum_map(id, type, name, value) VALUES (9, 2, 'SIXTH_GRADE', '六年级');
INSERT INTO t_enum_map(id, type, name, value) VALUES (10, 2, 'SEVENTH_GRADE', '七年级');
INSERT INTO t_enum_map(id, type, name, value) VALUES (11, 2, 'EIGHTH_GRADE', '八年级');
INSERT INTO t_enum_map(id, type, name, value) VALUES (12, 2, 'NINTH_GRADE', '九年级');
INSERT INTO t_enum_map(id, type, name, value) VALUES (13, 2, 'SENIOR_ONE', '高一');
INSERT INTO t_enum_map(id, type, name, value) VALUES (14, 2, 'SENIOR_TWO', '高二');
INSERT INTO t_enum_map(id, type, name, value) VALUES (15, 2, 'SENIOR_THREE', '高三');
INSERT INTO t_enum_map(id, type, name, value) VALUES (16, 2, 'SENIOR_HIGH', '高中');
INSERT INTO t_enum_map(id, type, name, value) VALUES (17, 2, 'UNDERGRADUATE', '本科');
INSERT INTO t_enum_map(id, type, name, value) VALUES (18, 2, 'POSTGRADUATE_PREPARATION', '硕士');
INSERT INTO t_enum_map(id, type, name, value) VALUES (19, 2, 'PUBLIC_SERVANT_PREPARATION', '公务员');
INSERT INTO t_enum_map(id, type, name, value) VALUES (20, 2, 'OTHER', '其他');
INSERT INTO t_enum_map(id, type, name, value) VALUES (21, 3, 'TO_START', '未开始');
INSERT INTO t_enum_map(id, type, name, value) VALUES (22, 3, 'IN_PROGRESS', '上课中');
INSERT INTO t_enum_map(id, type, name, value) VALUES (23, 3, 'FINISHED', '已结束');
INSERT INTO t_enum_map(id, type, name, value) VALUES (24, 4, 'TEACHER', '教师');
INSERT INTO t_enum_map(id, type, name, value) VALUES (25, 4, 'STUDENT', '学生');
INSERT INTO t_enum_map(id, type, name, value) VALUES (26, 4, 'PARENT', '家长');
INSERT INTO t_enum_map(id, type, name, value) VALUES (27, 4, 'OPERATOR', '运营');
INSERT INTO t_enum_map(id, type, name, value) VALUES (28, 4, 'GUEST', '访客');
INSERT INTO t_enum_map(id, type, name, value) VALUES (29, 5, 'MATH', '数学');
INSERT INTO t_enum_map(id, type, name, value) VALUES (30, 5, 'CHINESE', '语文');
INSERT INTO t_enum_map(id, type, name, value) VALUES (31, 5, 'ENGLISH', '英语');
INSERT INTO t_enum_map(id, type, name, value) VALUES (32, 5, 'PHYSICS', '物理');
INSERT INTO t_enum_map(id, type, name, value) VALUES (33, 5, 'CHEMISTRY', '化学');
INSERT INTO t_enum_map(id, type, name, value) VALUES (34, 5, 'BIOLOGY', '生物');
INSERT INTO t_enum_map(id, type, name, value) VALUES (35, 5, 'SCIENCE', '科学');
INSERT INTO t_enum_map(id, type, name, value) VALUES (36, 5, 'POLITICS', '政治');
INSERT INTO t_enum_map(id, type, name, value) VALUES (37, 5, 'HISTORY', '历史');
INSERT INTO t_enum_map(id, type, name, value) VALUES (38, 5, 'GEOGRAPHY', '地理');
INSERT INTO t_enum_map(id, type, name, value) VALUES (39, 5, 'IELTS', '雅思');
INSERT INTO t_enum_map(id, type, name, value) VALUES (40, 5, 'TOEFL', '托福');
INSERT INTO t_enum_map(id, type, name, value) VALUES (41, 5, 'NCE', '新概念');
INSERT INTO t_enum_map(id, type, name, value) VALUES (42, 5, 'UNKNOWN', '访客');