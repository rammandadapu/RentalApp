var gcm = require('android-gcm');
var mongodb = require('mongodb');

var url = 'mongodb://localhost:27017/rentalAppDB';
var MongoClient = mongodb.MongoClient;

exports.search = function(req, res) {
	MongoClient.connect(url, function (err, db) {
		  if (err) {
		    console.log('Unable to connect to the mongoDB server. Error:', err);
		  } else {		 
		    console.log('Connection established to', url);
		 
		    var collection = db.collection('property');

		    var keyword = req.param("keyword");
		    var location = req.param("location");
		    var type = req.param("type");
		    var pricelow = req.param("pricelow");
		    var pricehigh = req.param("pricehigh");
		    
		    var query = {};
		    
		    if(keyword !== undefined || keyword !== "") {	
		    	//change to search all the text
			    var field = 'desc';
			    var value = keyword;
			    query[field] = value;
		    }
		    if(location !== undefined || location !== "") {	
		    	//change to search all the text
			    var field = 'desc';
			    var value = location;
			    query[field] = value;
		    }
		    if(type !== undefined || type !== "") {	
		    	//change to search all the text
			    var field = 'desc';
			    var value = keyword;
			    query[field] = value;
		    }
		    if(keyword !== undefined || keyword !== "") {	
		    	//change to search all the text
			    var field = 'desc';
			    var value = keyword;
			    query[field] = value;
		    }
		    if(keyword !== undefined || keyword !== "") {	
		    	//change to search all the text
			    var field = 'desc';
			    var value = keyword;
			    query[field] = value;
		    }
		    
		    var query = {};
		    var field = 'userID';
		    var value = 'divya';
		    query[field] = value;
		    
		    // Insert some users
		    collection.find(query).toArray(function (err, result) {
		      if (err) {
		        console.log(err);
		      } else if (result.length) {
		          console.log('Found:', result);
		          res.send(result);
		      } else {
		        console.log('No document(s) found with defined "find" criteria!');
		      }
		      //Close connection
		      db.close();
		    });
		  }
		});
}