<?php
require "poorrow_module.php";

$userID = seize0('userID');
$password = seize0('password');
check_account($userID, $password);

// 上传文件文本到数据库
$fields = array('nickname', 'signature', 'new_password',
	'bills', 'cards',
	'kinds_spending', 'kinds_income', 'kinds_borrowing' );
$count = count($fields);
$time = time();

for ($i=0; $i < $count; $i++) {
	$field = $fields[$i]; // "${$fields[$i]}" 将变量名转换成字符串
	$content = seize($fields[$i]);
	if ($field == 'new_password') $field = 'password'; // 修改密码
	if ($content)
	{
		$sql = "UPDATE users set $field = '$content' where userID = '$userID'";
		query($sql, "保存 $field = $content 出错");
	}
	if ($field == "bills")
		query("UPDATE users set sync_time = '$time' where userID = '$userID'");
}

echo T;