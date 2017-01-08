<?php 
	//Importing Database Script 
	require_once('dbConnect.php');
	
	//Creating sql query
	$sql = "SELECT DISTINCT userID, timeDetected FROM user_history WHERE  timeDetected IN (SELECT  MAX(timeDetected ) FROM user_history GROUP BY userID)";

	//getting result 
	$r = mysqli_query($con,$sql);
	
	//creating a blank array 
	$result = array();
	
	//looping through all the records fetched
	while($row = mysqli_fetch_array($r)){
		
		//Pushing name and id in the blank array created 
		array_push($result,array(
			"_username"=>$row['userID'],
			"_timeStamp"=>$row['timeDetected']
		));
	}
	
		$fileLocation = getenv("DOCUMENT_ROOT") . "/getLocalUsers.txt";
		$file = fopen($fileLocation,"w");
		$content = print_r($result, true);
		fwrite($file,$content);
		fclose($file);

		//echo $content;
	
	//Displaying the array in json format 
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);