<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		$id = $_POST['_username'];
		$pwd = $_POST['_password'];
		
		require_once('dbConnect.php');
		
		//Creating sql query
		$sql = "SELECT _username, _password FROM user WHERE _username = '$id' AND _password = '$pwd'";

		//getting result 
		$r = mysqli_query($con,$sql);
	
		$json;
		//looping through all the records fetched
		if($r->num_rows >= 1){
			$json = '{"result":1}';
		}
		else{
			$json = '{"result":0}';
		}
		
		$fileLocation = getenv("DOCUMENT_ROOT") . "/myfile2.txt";
		$file = fopen($fileLocation,"w");
		$content = "$json";
		fwrite($file,$content);
		fclose($file);

		echo $json;
		mysqli_close($con);
	}