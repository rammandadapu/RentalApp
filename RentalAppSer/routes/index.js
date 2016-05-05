/**
 * http://usejsdoc.org/
 */
var constants = require('./constants');
exports.helloWorld = function(req, res) {
	//sample code for push notifications
	// initialize new androidGcm object 
	/*var gcmObject = new gcm.AndroidGcm('AIzaSyCAPTa1lApHqXFxgn8uNtAxFgvYKd-W3WE');
	 
	// create new message 
	var message = new gcm.Message({
	    registration_ids: ['x', 'y', 'z'],
	    data: {
	        key1: 'key 1',
	        key2: 'key 2'
	    }
	});
	 
	// send the message 
	gcmObject.send(message, function(err, response) {});*/
	res.end("Hello World");
};

exports.test=function(req,res){
	console.log(constants.CONST);
}



