<?php

function pdo_connect() {
    try {
        // Production server
        $dbhost="mysql:host=mysql-user.cse.msu.edu;dbname=longziya";
        $user = "longziya";
        $password = "A47215689";
        return new PDO($dbhost, $user, $password);
    } catch(PDOException $e) {
        die( "Unable to select database");
    }
}
