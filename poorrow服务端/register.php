<?php
require 'poorrow_module.php';

$nickname = seize('nickname');
$username = seize0('username');
$password = seize0('password');

die_if2(row("SELECT * from users where username = '$username'"), "用户名已存在");

// 可以注册，创建用户名
do{ $userID = rand(100000000, 999999999); }
while (row("select * from users where userID='$userID'"));
$time = time();
query2("INSERT INTO users (userID, username, password, create_time) values ('$userID', '$username', '$password', '$time')", "创建用户信息失败");

echo XML($userID, "USERID");
echo T;