<?php
/**
 * Created by PhpStorm.
 * User: Ava1oN
 * Date: 4/20/17
 * Time: 3:50 AM
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
function process($usr) {
    // Connect to the database
    $pdo = pdo_connect();

    $player1Q = "SELECT player1 FROM players";
    $player2Q = "SELECT player2 FROM players";
    $player1 = $pdo->query($player1Q);
    $player2 = $pdo->query($player2Q);
    $player1 = $player1->fetch();
    $player2 = $player2->fetch();
    if ($player1['player1'] != NULL and $player2['player2'] != NULL) {
        $drop = "DROP TABLE gamelive";
        $create = "create table gamelive (userName varchar(255),gridNum INT)";
        $player2 = $player2['player2'];
        $ini = "INSERT INTO gamelive(userName, gridNum) VALUES ($player2,43)";
        $pdo->query($drop);
        $pdo->query($create);
        $pdo->query($ini);
        if ($player1['player1'] == $usr) {
            $player2 = $player2['player2'];
            echo "<connect status='yes' msg='start' op=\"$player2\"/>";
        } else {
            $player1 = $player1['player1'];
            echo "<connect status='yes' msg='start' op=\"$player1\"/>";
        }

        exit;
    } else {
        echo '<connect status="yes" msg="need1" />';
        exit;
    }
    exit;

}