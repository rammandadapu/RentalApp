var gcm = require('android-gcm');
var mongodb = require('mongodb');
var constants = require('constants');
var ObjectId = require('mongodb').ObjectID;

var url = "mongodb://localhost:27017/rentalAppDB";
var MongoClient = mongodb.MongoClient;

exports.changePropertyStatus=function(req,res){
	MongoClient.connect(url, function (err, db) {
		  if (err) {
		    console.log('Unable to connect to the mongoDB server. Error:', err);
		  } else {		 
		    console.log('Connection established to', url);
		    console.log("B");
		 
		   
		    var id  = req.param("pid");	
		    var status  = req.param("status");
		    var query  = {};
		    query["_id"] = new ObjectId(id);
		    var updateFields={};
		    updateFields["status"]=status;
		    var updateFieldsDocument={$set:updateFields};
		    var collection = db.collection('property');	
		    collection.update(query,updateFieldsDocument,function(err,result){
		    	if (err) {
			        console.log(err);
			        res.send("failed");
			      } else if (result) {  
			         // console.log(result);
			    	  res.send("OK");
			      }
			      //Close connection
			      db.close();
		    
		    });	
		  }
	});
};
exports.getProperty = function(req, res) {
	console.log("A");
	MongoClient.connect(url, function (err, db) {
		  if (err) {
		    console.log('Unable to connect to the mongoDB server. Error:', err);
		  } else {		 
		    console.log('Connection established to', url);
		    console.log("B");
		 
		    var collection = db.collection('property');		    		   

		    var id  = req.param("pid");	
		    console.log(id);
		    
		    var query  = {};
		    
		    if(id !== undefined && id !== "") {	
		    	//change to search all the text			   
		    	query["_id"] = new ObjectId(id);	  
		    }		    		   		    				    		   		    		   
		    
		    console.log(query);
		    //var queryObject = new ObjectID(query);
		    		 
		    collection.find(query).toArray(function (err, result) {
		      if (err) {
		        console.log(err);
		      } else if (result.length) {
		          console.log('Found:', result);
		          res.send(result);
		      } else {
		        console.log('No document(s) found with defined "find" criteria!');
		        res.end("no results");
		      }
		      //Close connection
		      db.close();
		    });
		  }
		});
};