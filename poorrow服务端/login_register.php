<?php
require 'poorrow_module.php';

$nickname = seize('nickname');
$username = seize0('username');
$password = seize0('password');

if (row("SELECT * from users where username = '$username'")) // 用户存在，登录
{
	echo XML("login", "KIND");
	$row = row("SELECT * from users where username = '$username' && password = '$password'");
	die_if2(!$row, "密码错误");
	echo XML($row['nickname'], "NICKNAME");
	echo XML($row['signature'], "SIGNATURE");
	echo XML($row['cellphone'], "CELLPHONE");
	$userID = $row['userID'];
}
else // 用户不存在，注册
{
	echo XML("register", "KIND");
	do{ $userID = rand(100000000, 999999999); }
	while (row("select * from users where userID='$userID'"));
	$time = time();
	query2("INSERT INTO users (userID, username, password, create_time) values ('$userID', '$username', '$password', '$time')", "创建用户信息失败");
}

echo XML($userID, "USERID");
echo T;