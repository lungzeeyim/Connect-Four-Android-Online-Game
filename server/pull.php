<?php
/**
 * Created by PhpStorm.
 * User: Ava1oN
 * Date: 4/20/17
 * Time: 4:38 AM
 */

require_once "db.inc.php";

echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_GET['pull'])) {
    echo '<connect status="no" msg="1" />';
    exit;
}


// Process in a function
process($_GET['pull']);


/**
 * Process the query
 * @param $user the user to look for
 * @param $password the user password
 */
function process($pull) {
    // Connect to the database
    $pdo = pdo_connect();

    if ($pull == 1) {
        $checkName ="SELECT userName FROM gamelive";
        $checkGrid = "SELECT gridNum FROM gamelive";
        $usr = $pdo->query($checkName);
        $usr = $usr->fetch();
        $usr = $usr['userName'];
        $grid = $pdo->query($checkGrid);
        $grid = $grid->fetch();
        $grid = $grid['gridNum'];
        echo "<connect status='yes' usr=\"$usr\" gridNum=\"$grid\"/>";
        exit;
    }
    echo '<connect status="no" msg = "no"/>';
    exit;

}