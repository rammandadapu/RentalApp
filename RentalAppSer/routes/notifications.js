var gcm = require('android-gcm');
var constants = require('./constants');
var mongodb = require('mongodb');
var mongoUrl=constants.MongoURL;
var MongoClient = mongodb.MongoClient;

function sendNotification(result) {
	var gcmObject = new gcm.AndroidGcm('AIzaSyAXZ74tqeNIwD_0ueUiJJZO61cZlAfKuew');
	for(var i = 0; i < result.length; i++) {
	    var obj = result[i];
	    if(obj.regId !== undefined && obj.regId !== "") {
	    	var regIds = [];
	    	regIds.push(obj.regId);
	    	
	    	//create new message 
	    	var message = new gcm.Message({
	    		registration_ids: regIds,
	    	 data: {
	    	     name: obj.name,
	    	     keyword: obj.keyword,
	    	     location: obj.location,
	    	     pricelow: obj.pricelow,
	    	     pricehigh: obj.pricehigh,
	    	     condo: obj.condo,
	    	     apartment: obj.apartment,
	    	     house: obj.house,
	    	     townhouse: obj.townhouse,
	    	 }
	    	});
	    	
	    	//send the message 
	    	gcmObject.send(message, function(err, response) {
	    		
	    	});
	    }
	}
	
	//console.log("regIDs:::::::::::::"+JSON.stringify(regIds));
	return "done";
};

exports.notifySavedSearches = function(newPost, callback) {
	console.log("newpost: "+JSON.stringify(newPost));
	MongoClient.connect(mongoUrl, function (err, db) {
		  if (err) {
		    console.log('Unable to connect to the mongoDB server. Error:', err);
		  } else {		 
		    console.log('Connection established to', mongoUrl);
		 
		    var collection = db.collection('savedSearches');		    		   		   
		    
		    //{"$and":[{"apartment":"true"},{"pricelow":{"$lt":3000}},{"pricehigh":{"$gt":3000}},
		    //{"$text":{"$search":"pool fire pink"}},
		    //{"$or":[{"location":{ "$regex": ".*san jose.*", "$options": "i"}},{"location":{ "$regex": ".*95112.*", "$options": "i"}}]}]}

		    
		    var andPart = [];
		    
		    var type = newPost.type;
		    var typeQuery = {};
		    if(type == "Condo") typeQuery["condo"] = "true";
		    else if(type == "Apartment") typeQuery["apartment"] = "true";
		    else if(type == "Townhouse") typeQuery["townhouse"] = "true";
		    else if(type == "House") typeQuery["house"] = "true";
		    
		    andPart.push(typeQuery);
		    
		    var priceProperty = newPost.price;
		    var priceQueryLow = {};
		    var priceQueryHigh = {};
		    		    		  		    
		    if(priceProperty !== undefined && priceProperty !== "") {
		    	priceQueryLow["pricelow"] = JSON.parse('{"$lt":'+parseFloat(priceProperty)+'}');		  
		    	priceQueryHigh["pricehigh"] = JSON.parse('{"$gt":'+parseFloat(priceProperty)+'}');
		    }		    		  
		    
		    andPart.push(priceQueryLow);
		    andPart.push(priceQueryHigh);
		    
		    var desc = newPost.desc;		   
		    var keywordQuery = {};
		    //{ $text: { $search: "blue pool is nice"} }
		    
		    if(desc !== undefined && desc !== "") {
		    	keywordQuery["$text"] = JSON.parse('{ "$search": "'+desc+'"}');
		    }		
		    
		    andPart.push(keywordQuery);
		    
		    var city = newPost.address.city;
		    var zip = newPost.address.zip;
		    var locQuery = {};		  		  
	    	var locArray = [];	    	
    		locArray.push(JSON.parse('{"location": { "$regex": ".*'+city+'.*", "$options": "i"}}'));
    		locArray.push(JSON.parse('{"location": { "$regex": ".*'+zip+'.*", "$options": "i"}}'));    	
    		locQuery["$or"] = locArray;//JSON.parse('[{"address.city": "{ "$regex": ".*'+location+'.*", "$options": "i"}}, {"address.zip": { "$regex": ".*'+location+'.*", "$options": "i"}}]');		    	    		   
		    
    		andPart.push(locQuery);
    		
    		var query = {};
    		query["$and"] = andPart;
    		
		    console.log("notify query::::::::::"+JSON.stringify(query));
		    
		    collection.find(query).toArray(function (err, result) {
		      if (err) {
		        console.log(err);
		        callback("no results");
		      } else if (result.length) {
		          console.log('Found:', result);
		          sendNotification(result);
		          callback(result);
		      } else {
		        console.log('No document(s) found with defined "find" criteria!');
		        callback("no results");
		      }
		      //Close connection
		      db.close();
		    });
		  }
		});
}
