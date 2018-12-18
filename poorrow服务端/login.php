<?php
require "poorrow_module.php";

$username = seize0('username');
$password = seize0('password');

die_if2(!row("SELECT * from users where username = '$username' && password = '$password'"),
	"用户名或密码错误");

// 获取用户信息并输出
$sql = "SELECT userID from users where username = '$username' && password = '$password'";
$row = row($sql);

echo XML($row['signature'], "SIGNATURE");
echo XML($row['userID'], "USERID");
echo XML($row['nickname'], "NICKNAME");
echo T;
