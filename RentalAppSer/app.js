var express = require('express')
	, http = require('http')
	, nodeMailer = require('nodemailer')
	, bodyParser = require('body-parser')
	, index = require('./routes/index')
	, mailcomponent = require('./routes/mailcomponent')
	, fileupload= require('./routes/fileupload')
	, searchProperty = require('./routes/search')
	, savedSearch = require('./routes/savedSearch')
	, properties = require('./routes/properties')
	, notifications = require('./routes/notifications')
	, multer  =   require('multer')
	, gcm = require('android-gcm')
	, path = require('path')
	, crypto = require("crypto")
	, mongodb = require('mongodb');

global.appRoot = path.resolve(__dirname);
var app = express();
var MongoClient = mongodb.MongoClient;
var url = 'mongodb://localhost:27017/rentalAppDB';
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
	  extended: true
	}));
var storage = multer.diskStorage({
	  destination: './uploads/',
	  filename: function (req, file, cb) {
	    crypto.pseudoRandomBytes(16, function (err, raw) {
	      if (err) return cb(err)

	      cb(null, raw.toString('hex') + path.extname(file.originalname))
	    })
	  }
});

var upload = multer({ storage: storage });
//var upload = multer({ dest: 'uploads/' });

//service
app.get('/', index.test);
app.post('/sendMail', mailcomponent.sendMail);
/*app.post('/upload', upload.array('photos', 12), function (req, res, next) {
	fileupload.uploadtest(req,res);
	});*/

app.post('/postproperty', upload.array('photos', 12), function (req, res, next) {
	fileupload.upload(req,res);
	});
app.get('/download/:id',fileupload.download);
app.put('/property/:pid/update/', upload.array('photos', 12), function (req, res, next) {
	console.log(req.param("pid"));
	properties.updateProperty(req,res);
	});
app.post('/test',function(req,res){console.log(req.body);res.end();});
app.post('/property/:pid/status/:status',properties.changePropertyStatus);



//Chitti
app.get('/searchtest', searchProperty.search);
app.get('/property/:pid', properties.getProperty);
app.post('/saveSearch', savedSearch.saveSearch);
app.get('/getSavedSearches/:email', savedSearch.getSavedSearches);
app.get('/getSavedSearchResults/:sid', savedSearch.getSavedSearchResults);




http.createServer(app).listen(1337);
console.log('Server running at http://127.0.0.1:1337/');
