<?php
require "poorrow_module.php";

$userID = seize0('userID');
$password = seize0('password');
check_account($userID, $password);

// 上传文件文本到数据库
$file_names = array('bills', 'cards',
	'kinds_spending', 'kinds_income', 'kinds_borrowing' );
$count = count($filenames);

for ($i=0; $i < $count; $i++) {
	$content = seize($file_names[$i]);
	if ($content)
	{
		$sql = "UPDATE users set ${$file_names[$i]} = '$content' where userID = '$userID'";
		query($sql, "保存 ${$file_names[$i]} = $content 出错");
	}
}

echo T;