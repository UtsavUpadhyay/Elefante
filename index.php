<?php

require_once('Connection.php');

switch($_GET['method']){	
	//case 'get_table_names': getTables($handler);
		//break;		
	//case 'get_table_columns': getTableColumns($handler,$_GET['table_name']);
		//break;		
	case 'login': login($handler,$_GET['login_id'],$_GET['password']);
		break;		
	case 'get_projects': getProjects($handler,$_GET['login_id']);
		break;		
	case 'get_project_details': getProjectDetails($handler,$_GET['project_name']);
		break;	
	case 'get_elephant_details': getElephantDetails($handler,$_GET['project_name']);
		break;		
	case 'get_elephant_activity': getActivity($handler,$_GET['elephant_id']);
		break;
	case 'add_activity': addActivity($handler,$_GET['login_id'],$_GET['password'],$_GET['elephant_id'],$_GET['type'],$_GET['title'],$_GET['date_time'],$_GET['discription']);
		break;
}

class Table{
	public $Tables_in_harvisol_hms;	
}

function getTables($handler){	
	$table=array();
	$query=$handler->query("SHOW TABLES FROM harvisol_hms");
	$query->setFetchMode(PDO::FETCH_CLASS,'Table');
	while($result=$query->fetch()){
		$table[]=array('name'=>$result->Tables_in_harvisol_hms);
	}
	$post_data=json_encode(array('table'=>$table));
	echo $post_data;
}

function getTableColumns($handler,$tableName){	
	$column=array();
	$query=$handler->query("SHOW COLUMNS FROM $tableName");
	$column=$query->fetchAll(PDO::FETCH_ASSOC);
	$post_data=json_encode(array('column'=>$column));
	echo $post_data;
}

function login($handler,$loginId,$password,$flag=0){	
	$user=array();
        $hash=md5($password); 
//6609ff57b26ac81c292be239e90bdc46
  //      $hash = $password;     
	$query=$handler->query("SELECT * FROM user WHERE `loginid` LIKE '$loginId' AND `password` LIKE '$hash'");
	$result=$query->fetch(PDO::FETCH_ASSOC);
	if($query->rowCount()){
		if($flag==1)
		{
			return true;
		}
		$user=array('id'=>$result['id'],'name'=>$result['name'],'surname'=>$result['surname'],'designation'=>$result['designation'],'address'=>$result['address'],'contact'=>$result['contact'],'email'=>$result['email'],'joindate'=>$result['joindate']);
		$projects=getProjects($handler,$loginId,1);
		$user['project']=$projects;
		$post_data=json_encode(array('user'=>$user));
		echo $post_data;
	}
	else{
		$post_data=array('message'=>'Login Failed','comment'=>'Please Check your Login ID or Password.');
		$post_data = json_encode(array('error' => $post_data), JSON_FORCE_OBJECT);
		echo $post_data;
	}	
}

function getProjects($handler,$loginId,$flag=0){	
	$projects=array();
	$query=$handler->query("SELECT * FROM `usersinproject`,`projectdetail` WHERE `usersinproject`.`projectname`=`projectdetail`.`projectname` AND `userid` LIKE '$loginId'");
	$result=$query->fetchAll(PDO::FETCH_ASSOC);
	foreach($result as $project){
		$project['elephant']=getElephantDetails($handler,$project['projectname'],1);
		$projects[]=$project;
	}
	if($flag==1){
		return $projects;
	}
	$post_data=json_encode(array('project'=>$projects));
	echo $post_data;
}

function getProjectDetails($handler,$projectName,$flag=0){	
	$projectDetail=array();
	$query=$handler->query("SELECT * FROM projectdetail WHERE `projectname` LIKE '$projectName'");	
	$projectDetail=$query->fetch(PDO::FETCH_ASSOC);
	if($flag==1){
		return $projectDetail;
	}
	$post_data=json_encode(array('project_details'=>$projectDetail));
	echo $post_data;
}

function getElephantDetails($handler,$projectName,$flag=0){
		$elephants=array();
		$query=$handler->query("SELECT `elephantprofile`.`elephantid`,`elephantprofile`.`imagename`,`elephantprofile`.`name`,`elephantprofile`.`sex`,`elephantprofile`.`birthyear`,`elephantprofile`.`chipno`,`elephantprofile`.`workplace`,`elephantprofile`.`location`,`elephantprofile`.`bringfrom`,`elephantprofile`.`owner`,`elephantprofile`.`diedate` FROM `projectelephants`,`elephantprofile` WHERE `projectelephants`.`projectid` LIKE '$projectName' AND `projectelephants`.`elephantid`=`elephantprofile`.`elephantid`");
		$result=$query->fetchAll(PDO::FETCH_ASSOC);
		foreach($result as $value){
			$activity=getActivity($handler,$value['elephantid'],1);
			$elephants[]=array('elephantid'=>$value['elephantid'],'imagename'=>$value['imagename'],'name'=>$value['name'],'sex'=>$value['sex'],'birthyear'=>$value['birthyear'],'chipno'=>$value['chipno'],'workplace'=>$value['workplace'],'location'=>$value['location'],'bringfrom'=>$value['bringfrom'],'owner'=>$value['owner'],'diedate'=>$value['diedate'],'activity'=>$activity);	
		}
		if($flag==1){
			return $elephants;
		}
		$post_data=json_encode(array('elephant'=>$elephants));
		echo $post_data;
}

function getActivity($handler,$elephantId,$flag=0){
	$activity=array();
	$query=$handler->query("SELECT * FROM activity WHERE `elephantid` LIKE '$elephantId' order by date desc");	
	$activity=$query->fetchAll(PDO::FETCH_ASSOC);	
	if($flag==1){
		return $activity;
	}
	$post_data=json_encode(array('activity'=>$activity));
	echo $post_data;	
}

function addActivity($handler,$loginId,$password,$elephantId,$type,$title,$dateTime,$discription){
	if(login($handler,$loginId,$password,1)){
		$query="INSERT INTO `activity`(`id`,`elephantid`,`date`,`type`,`title`,`discription`,`enterby`) VALUES (NULL,?,?,?,?,?,?)";	
		$result=$handler->prepare($query);
		$result->execute(array($elephantId,$dateTime,$type,$title,$discription,$loginId));
		$post_data=array('message'=>'last inserted id is: '.$handler->lastInsertId(),'comment'=>'Data Inserted Successfully.');
		$post_data = json_encode(array('response' => $post_data), JSON_FORCE_OBJECT);
		echo $post_data;
	}
}
