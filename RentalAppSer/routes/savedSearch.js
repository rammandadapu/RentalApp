var gcm = require('android-gcm');
var mongodb = require('mongodb');
var constants = require('constants');
var ObjectId = require('mongodb').ObjectID;

var url = "mongodb://localhost:27017/rentalAppDB";
var MongoClient = mongodb.MongoClient;

exports.saveSearch = function(req, res) {
	MongoClient.connect(url, function (err, db) {
		  if (err) {
		    console.log('Unable to connect to the mongoDB server. Error:', err);
		  } else {		 
		    console.log('Connection established to', url);
		 
		    var collection = db.collection('savedSearches');		    		   

		    var email = req.query.email;
		    var name = req.query.name;
		    var keyword = req.query.keyword;
		    var location = req.query.location;
		    var condo = req.query.condo;
		    var apartment = req.query.apartment;
		    var house = req.query.house;
		    var townhouse = req.query.townhouse;
		    var pricelow = req.query.pricelow;
		    var pricehigh = req.query.pricehigh;
		    
		    var query = {};
		    
		    if(email !== undefined && email !== "") {
		    	query["email"] = email;
		    }
		    else {
		    	res.send("email missing");
		    }
		    
		    if(name !== undefined && name !== "") {
		    	query["name"] = name;
		    }
		 
		    if(condo !== undefined && condo !== "") {
		    	query["condo"] = condo;
		    }
		    
		    if(apartment !== undefined && apartment !== "") {
		    	query["apartment"] = apartment;
		    }
		    
		    if(house !== undefined && house !== "") {
		    	query["house"] = house;
		    }
		    
		    if(townhouse !== undefined && townhouse !== "") {
		    	query["townhouse"] = townhouse;
		    }
		    
		    if(pricelow !== undefined && pricelow !== "") {
		    	query["pricelow"] = pricelow;
		    }
		    
		    if(pricehigh !== undefined && pricehigh !== "") {
		    	query["pricehigh"] = pricehigh;
		    }
		    
		    if(location !== undefined && location !== "") {
		    	query["location"] = location;
		    }
		    
		    if(keyword !== undefined && keyword !== "") {
		    	query["keyword"] = keyword;
		    }
		    
		    collection.insert(query, {w:1}, function(err, result) {
		    	if (err) {
			        console.log(err);
			        res.end("error");
			      } else {
			          console.log('Inserted:', result);
			          res.end("successful");			   
			      }
		    	db.close();
		    });
		  }
	});
}

exports.getSavedSearches = function(req, res) {
	MongoClient.connect(url, function (err, db) {
		  if (err) {
		    console.log('Unable to connect to the mongoDB server. Error:', err);
		  } else {		 
		    console.log('Connection established to', url);
		 
		    var collection = db.collection('savedSearches');		    		   

		    var query = {};
		    var email = req.param("email");
		    
		    if(email !== undefined && email !== "") {
		    	query["email"] = email;
		    	collection.find(query).toArray(function (err, result) {
			      if (err) {
			        console.log(err);
			        res.end("no results");
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
		    else {
		    	res.send("email mandatory");
		    }
		  }
	});
}


exports.getSavedSearchResults = function(req, res) {
	MongoClient.connect(url, function (err, db) {
		  if (err) {
		    console.log('Unable to connect to the mongoDB server. Error:', err);
		  } else {		 
		    console.log('Connection established to', url);
		    console.log("B");
		 
		    var collection = db.collection('savedSearches');		    		   

		    var id  = req.param("sid");	
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
		    	  		var searchCriteria = result[0];
		    		    var status = "Available";
		    		    
		    		    var querySearch = {};
		    		    var total = [];
		    		    		    		 
		    		    var type = [];
		    		    
		    		    if(searchCriteria.condo !== undefined && searchCriteria.condo !== "") {
		    		    	if(searchCriteria.condo == "true")
		    		    		type.push("Condo");
		    		    }
		    		    
		    		    if(searchCriteria.apartment !== undefined && searchCriteria.apartment !== "") {
		    		    	if(searchCriteria.apartment == "true")
		    		    		type.push("Apartment");
		    		    }
		    		    
		    		    if(searchCriteria.house !== undefined && searchCriteria.house !== "") {
		    		    	if(searchCriteria.house == "true")
		    		    		type.push("House");
		    		    }
		    		    
		    		    if(searchCriteria.townhouse !== undefined && searchCriteria.townhouse !== "") {
		    		    	if(searchCriteria.townhouse == "true")
		    		    		type.push("Townhouse");
		    		    }		    		   
		    		    
		    		    if(type.length > 0) {
		    		    	querySearch["type"] = JSON.parse('{"$in":'+JSON.stringify(type)+'}');
		    		    }
		    		    
		    		    
		    		    if(searchCriteria.keyword !== undefined && searchCriteria.keyword !== "") {	
		    		    	//change to search all the text			   
		    		    	querySearch["desc"] = JSON.parse('{"$regex": ".*'+searchCriteria.keyword+'.*", "$options": "i"}');		  
		    		    }		  
		    		    		    		    
		    		    querySearch["status"] = status;		    	
		    		    
		    		    var price = [];
		    		    		    		  
		    		    
		    		    if(searchCriteria.pricelow !== undefined && searchCriteria.pricelow !== "") {
		    		    	price.push(JSON.parse('{"price": {"$gt":'+searchCriteria.pricelow+'}}'));
		    		    }	
		    		    
		    		    if(searchCriteria.pricehigh !== undefined && searchCriteria.pricehigh !== "") {
		    		    	price.push(JSON.parse('{"price": {"$lt":'+searchCriteria.pricehigh+'}}'));
		    		    }		    		  
		    		    
		    		    if(price.length > 0) {
		    		    	querySearch["$and"] = JSON.parse(JSON.stringify(price));
		    		    }
		    		    
		    		    //$or: [{"address.city": { $regex: /.*san jose.*///i}}, {"address.zip": { $regex: /.*san jose.*/i}}]
		    		    if(searchCriteria.location !== undefined && searchCriteria.location !== "") {
		    		    	var locArray = [];
		    		    	locArray.push(JSON.parse('{"address.city": { "$regex": ".*'+searchCriteria.location+'.*", "$options": "i"}}'));
		    		    	locArray.push(JSON.parse('{"address.zip": { "$regex": ".*'+searchCriteria.location+'.*", "$options": "i"}}'));
		    		    	querySearch["$or"] = JSON.parse(JSON.stringify(locArray));//JSON.parse('[{"address.city": "{ "$regex": ".*'+location+'.*", "$options": "i"}}, {"address.zip": { "$regex": ".*'+location+'.*", "$options": "i"}}]');
		    		    }		    		   
		    		    
		    		    console.log(querySearch);
		    		    
		    		    var collectionProperty = db.collection('property');	
		    		    		 
		    		    collectionProperty.find(querySearch).toArray(function (err, result) {
		    		      if (err) {
		    		        console.log(err);
		    		        res.end("no results");
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
		      } else {
		        console.log('No document(s) found with defined "find" criteria!');
		        res.end("no results");
		      }		     
		    });
		  }
		});
};
