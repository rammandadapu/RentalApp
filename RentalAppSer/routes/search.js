var gcm = require('android-gcm');
var QueryBuilder = require('mongodb-querybuilder');
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
		    var condo = req.query.condo;
		    var apartment = req.query.apartment;
		    var house = req.query.house;
		    var townhouse = req.query.townhouse;
		    var pricelow = req.query.pricelow;
		    var pricehigh = req.query.pricehigh;	
		    
		    var query = {};
		    var total = [];
		    		    		 
		    var type = [];
		    
		    if(condo !== undefined && condo !== "") {
		    	if(condo == "true")
		    		type.push("Condo");
		    }
		    
		    if(apartment !== undefined && apartment !== "") {
		    	if(apartment == "true")
		    		type.push("Apartment");
		    }
		    
		    if(house !== undefined && house !== "") {
		    	if(house == "true")
		    		type.push("House");
		    }
		    
		    if(townhouse !== undefined && townhouse !== "") {
		    	if(townhouse == "true")
		    		type.push("Townhouse");
		    }		    		   
		    
		    if(type.length > 0) {
		    	query["type"] = JSON.parse('{"$in":'+JSON.stringify(type)+'}');
		    }
		    
		    
		    if(keyword !== undefined && keyword !== "") {	
		    	//change to search all the text			   
		    	query["desc"] = JSON.parse('{"$regex": ".*'+keyword+'.*", "$options": "i"}');		  
		    }		  
		    
		    var price = [];
		    		    		  
		    
		    if(pricelow !== undefined && pricelow !== "") {
		    	price.push(JSON.parse('{"price": {"$gt":'+pricelow+'}}'));
		    }	
		    
		    if(pricehigh !== undefined && pricehigh !== "") {
		    	price.push(JSON.parse('{"price": {"$lt":'+pricehigh+'}}'));
		    }		    		  
		    
		    if(price.length > 0) {
		    	query["$and"] = JSON.parse(JSON.stringify(price));
		    }
		    
		    //$or: [{"address.city": { $regex: /.*san jose.*///i}}, {"address.zip": { $regex: /.*san jose.*/i}}]
		    if(location !== undefined && location !== "") {
		    	var locArray = [];
		    	locArray.push(JSON.parse('{"address.city": { "$regex": ".*'+location+'.*", "$options": "i"}}'));
		    	locArray.push(JSON.parse('{"address.zip": { "$regex": ".*'+location+'.*", "$options": "i"}}'));
		    	query["$or"] = JSON.parse(JSON.stringify(locArray));//JSON.parse('[{"address.city": "{ "$regex": ".*'+location+'.*", "$options": "i"}}, {"address.zip": { "$regex": ".*'+location+'.*", "$options": "i"}}]');
		    }
		    
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