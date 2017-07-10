<?php
/**
 * Created by PhpStorm.
 * User: Ava1oN
 * Date: 4/20/17
 * Time: 5:31 AM
 */

require_once "db.inc.php";

echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_GET['end'])) {
    echo '<connect status="no" msg="1" />';
    exit;
}


// Process in a function
process($_GET['end']);


/**
 * Process the query
 * @param $user the user to look for
 * @param $password the user password
 */
function process($end) {
    // Connect to the database
    $pdo = pdo_connect();

    if ($end == 1) {
        $drop ="UPDATE players SET player1= NULL,player2= NULL";
        $done = $pdo -> query($drop);
        if ($done) {
            echo "<connect status='yes' msg='game over'/>";
            exit;
        }
    }
    echo '<connect status="no" msg = "no"/>';
    exit;

}