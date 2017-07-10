<?php

require_once "db.inc.php";

echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_GET['usr'])) {
    echo '<hatter status="no" msg="1" />';
    exit;
}
if(!isset($_GET['psw'])) {
    echo '<hatter status="no" msg="2" />';
    exit;
}

// Process in a function
process($_GET['usr'],$_GET['psw']);


/**
 * Process the query
 * @param $user the user to look for
 * @param $password the user password
 */
function process($usr,$psw) {
    // Connect to the database
    $pdo = pdo_connect();

    $usrQ = $pdo->quote($usr);
    $query = "select password from connect where user=$usrQ;";

    $rows = $pdo->query($query);
    if($rows != False) {
		
		$row = $rows->fetch();
		
		if ($row['password'] != $psw)
		{
			echo '<connect status="no" msg="error" />';
			exit;
		}
		
		echo '<connect status="yes" msg="login successful" />';
		exit;
    }
    
    echo '<connect status="no" msg="error" />';
	exit;

}
