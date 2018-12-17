<?php
require 'public_module.php';

function user_login($userID, $password)
{
	$sql = "SELECT * from users where userID = '$userID' && password = '$password'";
	return row($sql);
}