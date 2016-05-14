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
		    var condo = req.query.condo;
		    var apartment = req.query.apartment;
		    var house = req.query.house;
		    var townhouse = req.query.townhouse;
		    var pricelow = req.query.pricelow;
		    var pricehigh = req.query.pricehigh;
		    var createdBy = req.query.createdBy;
		    var status = "Available";
		    
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
		    
		    if(status !== undefined && status !== "" &&(createdBy == undefined || createdBy == "")) {
		    	query["status"] = status;
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
		    
		    if(createdBy !== undefined && createdBy !== "") {
		    	query["createdBy"] = createdBy;
		    }
		    
		    console.log(query);
		    		 
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
		});
}

exports.saveSearch = function(req, res) {
	MongoClient.connect(url, function (err, db) {
		  if (err) {
		    console.log('Unable to connect to the mongoDB server. Error:', err);
		  } else {		 
		    console.log('Connection established to', url);
		 
		    var collection = db.collection('savedSearches');		    		   

		    var email = req.query.email;
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
			          res.send(result);			   
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
