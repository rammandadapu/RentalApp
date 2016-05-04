/**
 * http://usejsdoc.org/
 */

exports.upload=function(req,res){
	console.log(req.files);
	console.log(req.body);
	res.end("ok");
};