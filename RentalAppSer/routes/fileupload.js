/**
 * http://usejsdoc.org/
 */
var constants = require('./constants');
var mailcomponent = require('./mailcomponent');
var mongodb = require('mongodb');
var path = require('path');
var mongoUrl=constants.MongoURL;
var MongoClient = mongodb.MongoClient;

/*exports.uploadtest=function(req,res){
	var newPost  = JSON.parse(req.body.property);	
    //console.log(newPost);
    //console.log(newPost.post);
   // res.send();
    //console.log(newPost)	
    newPost["imageUrl"]=req.files[0].filename;
	console.log("body..");
	console.log(newPost);
	console.log("files..")
	console.log(req.files);
	res.send();
}*/

exports.upload=function(req,res){
	MongoClient.connect(mongoUrl, function (err, db) {
		  if (err) {
		    console.log('Unable to connect to the mongoDB server. Error:', err);
		  } else {		 
		    console.log('Connection established to', mongoUrl);
		    var collection = db.collection('property');		    		   

		    var newPost  = JSON.parse(req.body.property);
		    if(req.files)
		    	newPost["imageUrl"]=req.files[0].filename;
		    //console.log(newPost);
		    //console.log(newPost.post);
		   // res.send();
		    //console.log(newPost)	
		    
		    collection.insert(newPost,function (err, result) {
		      if (err) {
		        console.log(err);
		      } else{
		    	  console.log(result.ops[0]._id);		    	  
		    	  mailcomponent.sendMailHelper(function(response){
		    		  res.status(201);
		    		  res.send(result.ops[0]._id);
		    	  },result.ops[0].createdBy,"Notification from Team7 App","Your post has been published successfully")
		    	  
		      } 
		      //Close connection
		      db.close();
		    });
		  }
		});
	
};

exports.download=function(req,res){
	
	    console.log(req.body);
	     var id=req.param("id");;
	     console.log(id);
	     id ="/uploads/"+id;
	     console.log(id);
		  res.sendFile(appRoot+id);
		

};