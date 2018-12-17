<?php
require 'poorrow_module.pjp';

$nickname = seize('nickname');
$username = seize0('username');
$password = seize0('password');

die_if2(username_exists($username), "用户名已存在");

// 可以注册，创建用户名
$userID = createUserID();
$sql = "INSERT INTO users (userID, username, password) values ('$userID', 'username', 'password')";
query2($sql);

echo XML($userID, "USERID");
echo T;

?><?php

/**
 * 用户名是否已存在
 * @param  string $username 用户名
 * @return bool           是否存在
 */
function usernameExists($username)
{
	return row("SELECT * from users where username = '$username'");
}

/**
 * 为每个用户生成一个独一无二的ID
 * @return string 用户ID
 */
function createUserID() // 生成独一无二的ID
{
	$userID = 0;
	do{
		$userID = rand(100000000, 999999999);
		$sql = "select * from users where userID='$userID'";
		$result = mysql_query($sql);
		$row = mysql_fetch_array($result);
	}while ($row);
	return $userID;
}