/**
 * http://usejsdoc.org/
 */
var nodemailer = require('nodemailer');

function sendMailHelper(callback, to, subject, body){
	var smtpTransport = nodemailer.createTransport("SMTP", {
		service : "Gmail",
		auth : {
			user : "cmpe277maps@gmail.com",
			pass : "Helloworld@123"
		}
	});

	var mailOptions = {
		to : to,
		subject : subject,
		text : body
	};
	console.log(mailOptions);
	smtpTransport.sendMail(mailOptions, function(error, response) {
		if (error) {
			console.log(error);
			callback( "error");
		} else {
			console.log("Message sent: " + response.message);
			callback( "sent");
		}
	});
}

exports.sendMail = function(req, res) {
	sendMailHelper(function(result){
		console.log(result);
		res.end(result);
		},req.body.to,req.body.subject,req.body.text);
	
};
