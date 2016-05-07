var gcm = require('android-gcm');
var mongodb = require('mongodb');
var constants = require('constants');

var url = "mongodb://localhost:27017/rentalAppDB";
var MongoClient = mongodb.MongoClient;

exports.search = function(req, res) {
	MongoClient.connect(url, function (err, db) {
		  if (err) {
		    console.log('Unable to connect to the mongoDB server. Error:', err);
		  } else {		 
		    console.log('Connection established to', url);
		 
		    var collection = db.collection('property');		    		   

		    var keyword = req.query.keyword;
		    var location = req.query.location;
		    var type = req.query.type;
		    var pricelow = req.query.pricelow;
		    var pricehigh = req.query.pricehigh;		    		   
		    
		    var query = {};
		    
		    if(keyword !== undefined && keyword !== "") {	
		    	//change to search all the text			   
		    	query["desc"] = JSON.parse('{"$regex": ".*'+keyword+'.*", "$options": "i"}');		  
		    }		    		   
		    
		    if(type !== undefined && type !== "") {
		    	query["type"] = type;
		    }
		    
		    if(pricelow !== undefined && pricelow !== "") {
		    	query["price"] = JSON.parse('{"$gt":'+pricelow+'}');
		    }	
		    
		    if(pricehigh !== undefined && pricehigh !== "") {
		    	query["price"] = JSON.parse('{"$lt":'+pricehigh+'}');
		    }
		    
		    console.log(location);
		    
		    //$or: [{"address.city": { $regex: /.*san jose.*/i}}, {"address.zip": { $regex: /.*san jose.*/i}}]
		    /*if(location !== undefined && location !== "") {
		    	query["$or"] = JSON.parse('[{"address.city": "{ "$regex": ".*'+location+'.*", "$options": "i"}}, {"address.zip": { "$regex": ".*'+location+'.*", "$options": "i"}}]');
		    }*/
		    
		    console.log(query);
		    		 
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
}