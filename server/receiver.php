<?php
/**
 * Created by PhpStorm.
 * User: Ava1oN
 * Date: 4/19/17
 * Time: 9:23 PM
 */

require_once "db.inc.php";

echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_GET['userName'])) {
    echo '<connect status="no" msg="123" />';
    exit;
}
if(!isset($_GET['gridNum'])) {
    echo '<connect status="no" msg="2" />';
    exit;
}

// Process in a function
process($_GET['userName'],$_GET['gridNum']);


/**
 * Process the query
 * @param $user the user to look for
 * @param $password the user password
 */
function process($userName,$gridNum) {
    // Connect to the database
    $pdo = pdo_connect();

    $userNameQ = $pdo->quote($userName);
    $query = "UPDATE gamelive SET userName=$userNameQ,gridNum=$gridNum";
    $pdo->query($query);

    $flag = $pdo;

    if(!$flag) {
        echo '<connect status="noo" />';
        exit;
    }
    else {
        echo '<connect status="yes" />';
        exit;
    }
}