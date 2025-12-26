-- 1) 建库
CREATE DATABASE IF NOT EXISTS research_mgr
    DEFAULT CHARSET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE research_mgr;

-- 2) 用户表（系统管理）
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
                          id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                          username      VARCHAR(50)  NOT NULL UNIQUE,
                          password_hash VARCHAR(100) NOT NULL,
                          real_name     VARCHAR(50),
                          emp_no        VARCHAR(50),
                          gender        VARCHAR(10),
                          title         VARCHAR(50),
                          department    VARCHAR(100),
                          phone         VARCHAR(30),
                          email         VARCHAR(100),
                          hire_date     DATE,
                          remark        VARCHAR(500),
                          role          VARCHAR(20) NOT NULL DEFAULT 'USER',
                          status        TINYINT NOT NULL DEFAULT 1,
                          created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          INDEX idx_user_username (username),
                          INDEX idx_user_real_name (real_name),
                          UNIQUE KEY uk_emp_no (emp_no)
) ENGINE=InnoDB;

-- 3) 字典类型表
DROP TABLE IF EXISTS sys_dict_type;
CREATE TABLE sys_dict_type (
                               id         BIGINT PRIMARY KEY AUTO_INCREMENT,
                               type_code  VARCHAR(50) NOT NULL UNIQUE,      -- 如 PROJECT_NATURE
                               type_name  VARCHAR(100) NOT NULL,            -- 如 项目性质
                               remark     VARCHAR(300),
                               status     TINYINT NOT NULL DEFAULT 1,
                               sort_no    INT NOT NULL DEFAULT 0,
                               created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 4) 字典项表
DROP TABLE IF EXISTS sys_dict_item;
CREATE TABLE sys_dict_item (
                               id         BIGINT PRIMARY KEY AUTO_INCREMENT,
                               type_code  VARCHAR(50) NOT NULL,             -- 关联 sys_dict_type.type_code
                               item_code  VARCHAR(50) NOT NULL,             -- 如 NSFC
                               item_name  VARCHAR(100) NOT NULL,            -- 如 国家自然科学基金
                               status     TINYINT NOT NULL DEFAULT 1,
                               sort_no    INT NOT NULL DEFAULT 0,
                               remark     VARCHAR(300),
                               created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               UNIQUE KEY uk_type_item (type_code, item_code),
                               INDEX idx_type_code (type_code)
) ENGINE=InnoDB;

-- 5) 人员表（项目参与人员管理）
-- 人员表已合并到 sys_user

-- 6) 项目表（项目基本情况管理）
DROP TABLE IF EXISTS research_project;
CREATE TABLE research_project (
                                  id               BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  project_code     VARCHAR(50) UNIQUE,         -- 项目编号，可空
                                  name             VARCHAR(200) NOT NULL,
                                  nature_code      VARCHAR(50) NOT NULL,       -- 字典：PROJECT_NATURE
                                  scope_code       VARCHAR(50) NOT NULL,       -- 字典：PROJECT_SCOPE
                                  start_date       DATE,
                                  end_date         DATE,
                                  leader_person_id BIGINT,                     -- 负责人（可空）
                                  budget           DECIMAL(12,2),              -- 经费（可选）
                                  status_code      VARCHAR(50),                -- 字典：PROJECT_STATUS（可选）
                                  remark           VARCHAR(500),
                                  created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  INDEX idx_project_name (name),
                                  INDEX idx_project_leader (leader_person_id),
                                  CONSTRAINT fk_project_leader FOREIGN KEY (leader_person_id) REFERENCES sys_user(id)
                                      ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 7) 项目参与人员（多对多）
DROP TABLE IF EXISTS research_project_member;
CREATE TABLE research_project_member (
                                         id          BIGINT PRIMARY KEY AUTO_INCREMENT,
                                         project_id  BIGINT NOT NULL,
                                         person_id   BIGINT NOT NULL,
                                         duty        VARCHAR(100),         -- 角色/分工：负责人/成员/秘书等
                                         join_date   DATE,
                                         remark      VARCHAR(300),
                                         created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         UNIQUE KEY uk_project_person (project_id, person_id),
                                         INDEX idx_pm_project (project_id),
                                         INDEX idx_pm_person (person_id),
                                         CONSTRAINT fk_pm_project FOREIGN KEY (project_id) REFERENCES research_project(id)
                                             ON DELETE CASCADE ON UPDATE CASCADE,
                                         CONSTRAINT fk_pm_person FOREIGN KEY (person_id) REFERENCES sys_user(id)
                                             ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 8) 获奖表（项目/成果获奖情况管理）
DROP TABLE IF EXISTS research_award;
CREATE TABLE research_award (
                                id          BIGINT PRIMARY KEY AUTO_INCREMENT,
                                project_id  BIGINT NULL,                     -- 可关联项目，也可为空（成果不挂项目也行）
                                award_name  VARCHAR(200) NOT NULL,            -- 奖项名称
                                award_level VARCHAR(100),                     -- 国家级/省级/校级等（也可做字典）
                                award_org   VARCHAR(200),                     -- 颁奖单位
                                award_date  DATE,
                                remark      VARCHAR(500),
                                created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                INDEX idx_award_project (project_id),
                                INDEX idx_award_date (award_date),
                                CONSTRAINT fk_award_project FOREIGN KEY (project_id) REFERENCES research_project(id)
                                    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 9) 论文表（期刊论文管理）
DROP TABLE IF EXISTS research_paper;
CREATE TABLE research_paper (
                                id           BIGINT PRIMARY KEY AUTO_INCREMENT,
                                person_id    BIGINT NOT NULL,
                                title        VARCHAR(300) NOT NULL,
                                journal      VARCHAR(200),                    -- 刊物名称
                                index_code   VARCHAR(50) NOT NULL,            -- 字典：PAPER_INDEX_SOURCE（EI/SCI/核心/一般）
                                publish_date DATE,
                                doi          VARCHAR(100),
                                remark       VARCHAR(500),
                                created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                updated_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                INDEX idx_paper_person (person_id),
                                INDEX idx_paper_date (publish_date),
                                CONSTRAINT fk_paper_person FOREIGN KEY (person_id) REFERENCES sys_user(id)
                                    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 10) 著作表（著作管理）
DROP TABLE IF EXISTS research_book;
CREATE TABLE research_book (
                               id           BIGINT PRIMARY KEY AUTO_INCREMENT,
                               person_id    BIGINT NOT NULL,
                               name         VARCHAR(300) NOT NULL,
                               publisher    VARCHAR(200),
                               publish_date DATE,
                               isbn         VARCHAR(50),
                               remark       VARCHAR(500),
                               created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               INDEX idx_book_person (person_id),
                               INDEX idx_book_date (publish_date),
                               CONSTRAINT fk_book_person FOREIGN KEY (person_id) REFERENCES sys_user(id)
                                   ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 11) 初始化字典（题目要求的三类字典 + 可选项目状态）
-- 字典类型
INSERT INTO sys_dict_type(type_code, type_name, sort_no) VALUES
                                                             ('PROJECT_NATURE', '项目性质', 1),
                                                             ('PROJECT_SCOPE', '项目范围', 2),
                                                             ('PAPER_INDEX_SOURCE', '论文检索源', 3),
                                                             ('PROJECT_STATUS', '项目状态', 4)
    ON DUPLICATE KEY UPDATE type_name=VALUES(type_name);

-- 字典项：项目性质
INSERT INTO sys_dict_item(type_code, item_code, item_name, sort_no) VALUES
                                                                        ('PROJECT_NATURE','NSFC','国家自然科学基金',1),
                                                                        ('PROJECT_NATURE','P863','863项目',2),
                                                                        ('PROJECT_NATURE','PROV','部省科委项目',3),
                                                                        ('PROJECT_NATURE','ENT','企业集团项目',4)
    ON DUPLICATE KEY UPDATE item_name=VALUES(item_name), sort_no=VALUES(sort_no);

-- 字典项：项目范围
INSERT INTO sys_dict_item(type_code, item_code, item_name, sort_no) VALUES
                                                                        ('PROJECT_SCOPE','NATION','全国',1),
                                                                        ('PROJECT_SCOPE','INTL','国际',2),
                                                                        ('PROJECT_SCOPE','LOCAL','地方',3)
    ON DUPLICATE KEY UPDATE item_name=VALUES(item_name), sort_no=VALUES(sort_no);

-- 字典项：论文检索源
INSERT INTO sys_dict_item(type_code, item_code, item_name, sort_no) VALUES
                                                                        ('PAPER_INDEX_SOURCE','EI','EI',1),
                                                                        ('PAPER_INDEX_SOURCE','SCI','SCI',2),
                                                                        ('PAPER_INDEX_SOURCE','CORE','核心期刊',3),
                                                                        ('PAPER_INDEX_SOURCE','NORMAL','一般期刊',4)
    ON DUPLICATE KEY UPDATE item_name=VALUES(item_name), sort_no=VALUES(sort_no);

-- （可选）项目状态
INSERT INTO sys_dict_item(type_code, item_code, item_name, sort_no) VALUES
                                                                        ('PROJECT_STATUS','PLANNING','立项',1),
                                                                        ('PROJECT_STATUS','RUNNING','在研',2),
                                                                        ('PROJECT_STATUS','FINISHED','结题',3)
    ON DUPLICATE KEY UPDATE item_name=VALUES(item_name), sort_no=VALUES(sort_no);

USE research_mgr;

USE research_mgr;

-- 1) 用户 + 人员（合并到 sys_user）
INSERT INTO sys_user(username, password_hash, real_name, emp_no, gender, title, department, phone, email, hire_date, role, status) VALUES
                                                                                                                                       ('zhangsan', '123456', '张三', 'T2023001','男','副教授','计算机学院','13800000001','zs@univ.edu','2020-09-01','USER', 1),
                                                                                                                                       ('lisi', '123456', '李四', 'T2023002','女','讲师','计算机学院','13800000002','ls@univ.edu','2021-09-01','USER', 1),
                                                                                                                                       ('wangwu', '123456', '王五', 'T2023003','男','教授','信息学院','13800000003','ww@univ.edu','2015-03-12','USER', 1),
                                                                                                                                       ('zhaoliu', '123456', '赵六', 'T2023004','女','助教','信息学院','13800000004','zl@univ.edu','2022-07-15','USER', 1),
                                                                                                                                       ('sunqi', '123456', '孙七', 'T2023005','男','研究员','人工智能研究院','13800000005','sq@univ.edu','2018-01-20','USER', 1),
                                                                                                                                       ('admin', '123456', '系统管理员', NULL, NULL, NULL, NULL, NULL, NULL, NULL,'ADMIN', 1),
                                                                                                                                       ('test_disabled', '123456', '禁用用户', NULL, NULL, NULL, NULL, NULL, NULL, NULL,'USER', 0);
-- 3) 项目（项目基本情况管理）
-- nature_code: NSFC/P863/PROV/ENT
-- scope_code : NATION/INTL/LOCAL
-- status_code: PLANNING/RUNNING/FINISHED （可选）
INSERT INTO research_project(project_code, name, nature_code, scope_code, start_date, end_date, leader_person_id, budget, status_code, remark) VALUES
                                                                                                                                                   ('P-2024-001','基于知识图谱的科研成果推荐系统','NSFC','NATION','2024-01-01','2026-12-31', 1, 300000.00,'RUNNING','重点研究：推荐算法与系统实现'),
                                                                                                                                                   ('P-2024-002','面向国际会议的论文写作辅助平台','PROV','INTL','2024-03-01','2025-12-31', 3, 180000.00,'RUNNING','包含前后端与NLP模块'),
                                                                                                                                                   ('P-2023-003','企业合作：智能质检系统','ENT','LOCAL','2023-06-01','2024-06-30', 3, 500000.00,'FINISHED','已结题，可用于展示'),
                                                                                                                                                   ('P-2025-004','863：边缘计算任务调度优化','P863','NATION','2025-01-01','2027-12-31', 5, 450000.00,'PLANNING','准备立项材料');

-- 4) 项目参与人员（多对多 + 分工）
INSERT INTO research_project_member(project_id, person_id, duty, join_date, remark) VALUES
                                                                                        (1, 1, '负责人', '2024-01-01', '总体设计'),
                                                                                        (1, 2, '成员',   '2024-02-01', '前端Vue3与接口联调'),
                                                                                        (1, 5, '成员',   '2024-02-10', '算法与实验'),

                                                                                        (2, 3, '负责人', '2024-03-01', '项目管理与论文写作'),
                                                                                        (2, 2, '成员',   '2024-03-15', '需求分析与测试'),
                                                                                        (2, 4, '成员',   '2024-04-01', '数据标注与整理'),

                                                                                        (3, 3, '负责人', '2023-06-01', '对接企业'),
                                                                                        (3, 1, '成员',   '2023-07-01', '模型训练'),
                                                                                        (3, 2, '成员',   '2023-07-01', '系统开发'),

                                                                                        (4, 5, '负责人', '2025-01-01', '立项准备'),
                                                                                        (4, 1, '成员',   '2025-02-01', '调研与可行性');

-- 5) 获奖（获奖情况管理：可关联项目）
INSERT INTO research_award(project_id, award_name, award_level, award_org, award_date, remark) VALUES
                                                                                                   (1, '校级科技进步奖一等奖', '校级', '某某大学', '2025-05-20', '推荐系统成果获奖'),
                                                                                                   (2, '省级教学成果奖二等奖', '省级', '省教育厅', '2025-10-12', '写作平台用于教学改革'),
                                                                                                   (3, '企业创新合作优秀项目', '企业级', '合作企业集团', '2024-07-01', '项目结题后获奖'),
                                                                                                   (NULL, '全国大学生创新创业优秀指导奖', '国家级', '教育部相关机构', '2024-12-15', '与具体项目无强绑定');

-- 6) 论文（期刊论文管理：挂人员）
-- index_code: EI/SCI/CORE/NORMAL
INSERT INTO research_paper(person_id, title, journal, index_code, publish_date, doi, remark) VALUES
                                                                                                 (1, '基于图表示学习的成果推荐方法研究', '计算机学报', 'CORE', '2024-08-10', '10.0000/example1', '核心期刊'),
                                                                                                 (1, '面向企业质检的轻量化检测模型', '软件学报', 'CORE', '2023-11-05', NULL, '工程应用'),
                                                                                                 (2, 'Vue3 与 SpringBoot 在科研管理系统中的实践', '现代信息科技', 'NORMAL','2024-06-18', NULL, '系统类论文'),
                                                                                                 (3, 'A Study on Academic Writing Assistance Systems', 'International Journal of AI', 'SCI', '2025-02-02', '10.0000/example2', 'SCI论文'),
                                                                                                 (5, 'Edge Computing Task Scheduling Optimization', 'IEEE Transactions on ...', 'EI', '2025-09-09', NULL, 'EI检索');

-- 7) 著作（著作管理：挂人员）
INSERT INTO research_book(person_id, name, publisher, publish_date, isbn, remark) VALUES
                                                                                      (3, '科研项目管理与实践', '高等教育出版社', '2024-03-01', '978-7-xxxx-xxxx-x', '管理类著作'),
                                                                                      (1, '知识图谱与推荐系统', '电子工业出版社', '2025-01-15', '978-7-xxxx-xxxx-x', '技术类著作'),
                                                                                      (5, '边缘计算：理论与应用', '机械工业出版社', '2025-10-01', '978-7-xxxx-xxxx-x', '前沿方向');

-- 项目-论文 关联表
CREATE TABLE research_project_paper (
                                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        project_id BIGINT NOT NULL,
                                        paper_id BIGINT NOT NULL,
                                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                        UNIQUE KEY uk_project_paper (project_id, paper_id)
);

-- 项目-著作 关联表
CREATE TABLE research_project_book (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       project_id BIGINT NOT NULL,
                                       book_id BIGINT NOT NULL,
                                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       UNIQUE KEY uk_project_book (project_id, book_id)
);
