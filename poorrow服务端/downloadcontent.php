<?php
require "poorrow_module.php";

$userID = seize0('userID');
$password = seize0('password');
check_account($userID, $password);

// 在数据库中搜索内容
$name = seize0('filename');
$file_names = array('bills', 'cards',
	'kinds_spending', 'kinds_income', 'kinds_borrowing' );
if (in_array($file_names, $name))
{
	$row = row("SELECT $name from users where userID = '$userID'");
	echo XML($row[0], "CONTENT");
}
echo T;