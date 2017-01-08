<?php 
include_once("connect.php");

$_db = $db;

$sql = "SELECT * FROM test ORDER BY _id DESC LIMIT 1;";

if($stmt = $_db->prepare($sql))
{
	try {
		$stmt->execute();
	}
	catch(PDOException $e) {
		echo $e->getMessage();
		exit();
	}
	$data = $stmt->fetch(PDO::FETCH_ASSOC);
	$stmt->closeCursor();

	if((($data['_id']) % 2) ==0) {
		echo '0';
		exit();
	}
	else {
		echo '1';
		exit();
	}
}
?>