<?php
die; // 安装后这个变成 die
require 'public_module.php';

// 添加用户表
$sql = "CREATE TABLE `users`
(
	tableID int NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(tableID),
	userID varchar(255),
	username varchar(255),
	password varchar(255),
	nickname varchar(255),
	cellphone varchar(255),

	data_bills longtext,
	date_kinds_spending longtext,
	data_kinds_income longtext,
	data_kinds_borrowing longtext,
	data_cards longtext,
	
	create_time bigint
)";
die_if(!query_sql($sql), "创建用户表失败" . mysql_error());
echo "创建用户表 users 成功\n";


echo "\n初始化完毕，尽情享用吧！\n";
?>