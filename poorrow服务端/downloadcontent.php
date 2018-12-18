<?php
require "poorrow_module.php";

$userID = seize0('userID');
$password = seize0('password');
check_account($userID, $password);

// 在数据库中搜索内容
$field = seize0('field');
$fields = array('nickname', 'username', 'password', 'signature',
	'bills', 'cards',
	'kinds_spending', 'kinds_income', 'kinds_borrowing' );
if (in_array($field, $fields))
{
	$row = row("SELECT $field from users where userID = '$userID'");
	echo XML($row[0], "CONTENT");
}
echo T;