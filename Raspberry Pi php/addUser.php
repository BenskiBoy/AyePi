<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		$usr = $_POST['_username'];
		$psw = $_POST['_password'];
		$mac = $_POST['_mac'];
		
		require_once('dbConnect.php');
		
		$fileLocation = getenv("DOCUMENT_ROOT") . "/myfile.txt";
		$file = fopen($fileLocation,"w");
		$content = "$usr, $psw, $mac";
		fwrite($file,$content);
		fclose($file);
  
		$sql = "INSERT INTO user (userID, password, _mac) VALUES ('$usr', '$psw','$mac')";
		$json;

		if(mysqli_query($con,$sql)){
			$json = '{"result":1}';
		}else{
			$json = '{"result":0}';
		}
		echo $json;
		mysqli_close($con);
	}