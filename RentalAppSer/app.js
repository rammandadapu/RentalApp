var express = require('express')
	, http = require('http')
	, nodeMailer = require('nodemailer')
	, bodyParser = require('body-parser')
	, index = require('./routes/index')
	, mailcomponent = require('./routes/mailcomponent')
	, fileupload= require('./routes/fileupload')
	, searchProperty = require('./routes/search')
	, multer  =   require('multer')
	, gcm = require('android-gcm')
	, mongodb = require('mongodb');

var app = express();
var MongoClient = mongodb.MongoClient;
var url = 'mongodb://localhost:27017/rentalAppDB';
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
	  extended: true
	}));
var upload = multer({ dest: 'uploads/' });

//service
app.get('/', index.helloWorld);
app.post('/sendMail', mailcomponent.sendMail);
app.post('/upload', upload.array('photos', 12), function (req, res, next) {
	fileupload.upload(req,res);
	});
app.post('/test',function(req,res){console.log(req.body);res.end();});



//Chitti
app.post('/searchtest', searchProperty.search);



http.createServer(app).listen(1337, '127.0.0.1');
console.log('Server running at http://127.0.0.1:1337/');
