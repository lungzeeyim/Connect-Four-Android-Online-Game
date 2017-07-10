<?php
/**
 * Created by PhpStorm.
 * User: Ava1oN
 * Date: 4/20/17
 * Time: 2:35 AM
 */

require_once "db.inc.php";

echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_GET['usr'])) {
    echo '<connect status="no" msg="1" />';
    exit;
}


// Process in a function
process($_GET['usr']);


/**
 * Process the query
 * @param $user the user to look for
 * @param $password the user password
 */
function process($username) {
    // Connect to the database
    $pdo = pdo_connect();

    $usrQ = $pdo->quote($username);

    $queryA ="SELECT player1 FROM players";
    $A = $pdo->query($queryA);
    $A = $A->fetch();
    if ($A['player1'] == NULL) {
        $queryU = "UPDATE players SET player1 = $usrQ";
        $U = $pdo->query($queryU);
        if(!$U) {
            echo '<connect status="noo1" />';
            exit;
        }
        else {
            echo '<connect player1="player1" />';
            exit;
        }
    }else {
        $queryU = "UPDATE players SET player2 = $usrQ";
        $U = $pdo->query($queryU);
        if(!$U) {
            echo '<connect status="noo2" />';
            exit;
        }
        else {
            echo '<connect player="player2" />';
            exit;
        }
        exit;
    }

    echo '<connect status="no" msg="error" />';
    exit;

}