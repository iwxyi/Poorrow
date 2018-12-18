<?php
// die; // 安装后去掉注释
require 'public_module.php';

// 添加用户表
$sql = "CREATE TABLE `users`
(
	tableID int NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(tableID),
	userID varchar(255),
	username varchar(255),
	password varchar(255),
	nickname varchar(255) default '',
	cellphone varchar(255) default '',

	data_bills longtext default '',
	date_kinds_spending longtext default '',
	data_kinds_income longtext default '',
	data_kinds_borrowing longtext default '',
	data_cards longtext default '',
	
	create_time bigint
)";
query2($sql, "创建用户表失败");
echo "创建用户表 users 成功\n";

echo "\n初始化完毕，尽情享用吧！\n";
?>