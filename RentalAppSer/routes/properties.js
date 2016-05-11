var gcm = require('android-gcm');
var mongodb = require('mongodb');
var constants = require('constants');
var ObjectId = require('mongodb').ObjectID;
var mailcomponent = require('./mailcomponent');

var url = "mongodb://localhost:27017/rentalAppDB";
var MongoClient = mongodb.MongoClient;

function getOwnerEmail(recordId,callback){
	MongoClient.connect(url, function (err, db) {
		  if (err) {
		    console.log('Unable to connect to the mongoDB server. Error:', err);
		  } else {
			  var query  = {};
			  query["_id"] = new ObjectId(recordId);
			  var filter={}
			  filter["createdBy"]=1
			  var collection = db.collection('property');
			  collection.findOne(query,filter,function(err,result){
				  if(err){
					  callback( "cmpe277maps@gmail.com")
				  }
				else{
					
					console.log(result.createdBy);
					callback(result.createdBy)
					//return result.
				}
			  })
		  }
	})
}


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
			         //console.log(result);
			    	  getOwnerEmail(id,function(email){
			    	  mailcomponent.sendMailHelper(function(response){
			    		  res.send("OK");
			    		  //Close connection
					      db.close();
			    	  },email,"Notification from Team7 App","Your post has been updated")
			    	  
			      });
			     
		    
		    }	
		  });
		  }
	});
};

/***
 * TODO: While writing update need to send email also
 * 
 * 
 * 
 * 
 * 
 */

function updateViewCount(propertyId,callback){
	
}
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
		    //console.log(id);
		    
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
		    	   collection.update(query,{ $inc: { viewCount: 1}},function(err,updateResult){
		    		   console.log('Found:', result);
		    		   console.log('No of Views Update:', updateResult);
				        res.send(result);
		    	   })
		          
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