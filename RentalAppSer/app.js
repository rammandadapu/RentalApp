var express = require('express')
	, http = require('http')
	, index = require('./routes/index');

var app = express();

//service
app.get('/', index.helloWorld);

http.createServer(app).listen(1337, '127.0.0.1');
console.log('Server running at http://127.0.0.1:1337/');
