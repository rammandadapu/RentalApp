var express = require('express')
	, http = require('http')
	, nodeMailer = require('nodemailer')
	, bodyParser = require('body-parser')
	, index = require('./routes/index')
	, mailcomponent = require('./routes/mailcomponent')
	, gcm = require('android-gcm')
	, mongodb = require('mongodb');

var app = express();
var MongoClient = mongodb.MongoClient;
var url = 'mongodb://localhost:27017/rentalAppDB';
app.use(bodyParser.json());

//service
app.get('/', index.helloWorld);
app.post('/sendMail', mailcomponent.sendMail);


http.createServer(app).listen(1337, '127.0.0.1');
console.log('Server running at http://127.0.0.1:1337/');
