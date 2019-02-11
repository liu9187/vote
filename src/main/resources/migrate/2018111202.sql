-- 主表
CREATE TABLE IF  NOT EXISTS vote_main_table(
 id VARCHAR(255)  COMMENT '主表id',
 vote_title VARCHAR(255) COMMENT '主题', 
create_user_num VARCHAR(255)  COMMENT '创建人工号', 
create_user_name  VARCHAR(255) COMMENT '创建人名称',
start_time  bigint  COMMENT '创建时间',
describes VARCHAR(255) COMMENT  '描述' ,
 remarks VARCHAR(255) COMMENT '备注' ,
 end_time bigint COMMENT '结束时间',
state int DEFAULT 0 COMMENT '状态  1 未发布 2 已发布 3 已删除 4 已结束'
 );
 -- 选择表
CREATE TABLE IF  NOT EXISTS option_table(
id INT NOT NULL  AUTO_INCREMENT ,
vote_id VARCHAR(255) NOT NULL COMMENT'主表ID',
option_title VARCHAR(255) NOT NULL COMMENT '主题',
state_option INT NOT NULL DEFAULT 1 COMMENT '状态（1未删除 0已删除）',
department VARCHAR(25) NOT NULL COMMENT '部门',
picture_url  VARCHAR(255) COMMENT'图片地址',
remarks VARCHAR(255)  COMMENT'备注',
create_time  timestamp   DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',
PRIMARY KEY (`id`)
);
-- 选择表子表
CREATE TABLE IF  NOT EXISTS option_sublist_table(
id INT NOT NULL AUTO_INCREMENT,
option_id INT NOT NULL COMMENT'选择表id',
picture_url VARCHAR(255) COMMENT'图片地址',
view_url VARCHAR(255) COMMENT'视频地址',
sublist_title VARCHAR(255) COMMENT'说明',
state_sublist INT DEFAULT 1 COMMENT'状态（0已删除 1未删除）',
create_time  timestamp   DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',
remarks VARCHAR(255) COMMENT'备注',
PRIMARY KEY(`id`)
);
-- 答案表
CREATE TABLE IF  NOT EXISTS answer_table(
id INT(11) NOT NULL  AUTO_INCREMENT ,
option_id INT(11) NOT NULL  COMMENT '选项表ID',
option_title VARCHAR(255)  COMMENT'选项表主题',
answer_user_name VARCHAR(255) COMMENT'答题人',
create_time  timestamp   DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',
PRIMARY KEY (`id`)
);