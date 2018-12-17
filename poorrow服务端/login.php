<?php
require "poorrow_module.php";

$username = seize0('username');
$password = seize0('password');

die_if2(!can_login($username, $password), "用户名或密码错误");

// 获取用户信息并输出
$sql = "SELECT userID from users where username = '$username' && password = '$password'";
$row = row($sql);

$userID = $row['userID'];
$nickname = $row['nickname'];

echo XML($row['userID'], "USERID");
echo XML($row['nickname'], "NICKNAME");
echo T;

 ?><?php
function can_login($username, $password)
{
	$sql = "SELECT * from users where username = '$username' && password = '$password'";
	return row($sql);
}
