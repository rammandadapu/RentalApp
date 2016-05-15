var gcm = require('android-gcm');

exports.sendNotification = function(req,res) {
	var gcmObject = new gcm.AndroidGcm('AIzaSyAXZ74tqeNIwD_0ueUiJJZO61cZlAfKuew');
	
	//create new message 
	var message = new gcm.Message({
		registration_ids: ['fkpw2ABkTFg:APA91bFlo3oJIf4fhomJIdx7uOnyCkJz2N5QUy65gJk3ckIVkRvGQQOmQQlbuwqBu0BGVkJemx4xXbU4g1cLFLxEH2QQ4dGSgvVyR3LCpt02THAer5M9H8-gxxqcPAUpsEnH4jUQgKe0'],
	 data: {
	     key1: 'key 1',
	     key2: 'key 2'
	 }
	});
	
	//send the message 
	gcmObject.send(message, function(err, response) {
		if(err) {
			res.send(err);
		}
		res.send(response);
	});
};
