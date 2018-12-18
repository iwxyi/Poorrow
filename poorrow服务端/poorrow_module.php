<?php
require 'public_module.php';

function user_login($username, $password)
{
	$sql = "SELECT * from users where username = '$username' && password = '$password'";
	return row($sql);
}