/**
 * http://usejsdoc.org/
 */
var nodemailer = require('nodemailer');


exports.helloWorld = function(req, res) {
	res.end("Hello World");
}

exports.sendMail = function(req, res) {
	var smtpTransport = nodemailer.createTransport("SMTP", {
		service : "Gmail",
		auth : {
			user : "cmpe277maps@gmail.com",
			pass : "Helloworld@123"
		}
	});

	var mailOptions = {
		to : req.body.to,
		subject : req.body.subject,
		text : req.body.text
	}
	console.log(mailOptions);
	smtpTransport.sendMail(mailOptions, function(error, response) {
		if (error) {
			console.log(error);
			res.end("error");
		} else {
			console.log("Message sent: " + response.message);
			res.end("sent");
		}
	});
}

