import {GraphQLServer} from "graphql-yoga";
import resolvers from "./graphql/resolvers";
import {PrismaClient} from "@prisma/client";
import morgan from 'morgan';
import {logger,stream} from './config/morgan_config';

const path = require('path');
const express = require('express');
const prisma = new PrismaClient();
var multer, storage, crypto;
multer = require('multer')
crypto = require('crypto');
// const PORT = process.env.PORT || 5000;


//server options
const options = {
	port:5000,
	endpoint:'/graphql',
	debug:false,
}

//server 
const server = new GraphQLServer({
	typeDefs: ["graphql/schema.graphql"],
	resolvers
});
//console.log(path.join(__dirname,'Log','error'));

server.express.use('/img',express.static('img'));
server.express.use(morgan('short',{"stream": stream}));

var form = "<!DOCTYPE HTML><html><body>" +
"<form method='post' action='/upload' enctype='multipart/form-data'>" +
"<input type='file' name='upload'/>" +
"<input type='submit' /></form>" +
"</body></html>";

server.express.get('/upload', function(req, res)
{
	res.writeHead(200, {'Content-Type': 'text/html' });
	res.end(form)
});

var upload = multer({ dest: 'uploads/' });
var storage = multer.diskStorage({
	destination: function(req, file, cb){
		cb(null, 'uploads/')
	},
	filename: function(req, file, cb){
		cb(null, file.originalname)
	}
})
server.express.post(
	"/upload",
	multer({
		storage: storage
	}).single('upload'), function(req, res) {
		console.log(req.file);
		console.log(req.body);
		res.redirect("/uploads/" + req.file.filename);
		console.log(req.file.filename);
		return res.status(200).end();
});
  
server.express.get('/uploads/:upload', function (req, res){
	file = req.params.upload;
	console.log(req.params.upload);
	var img = fs.readFileSync(__dirname + "/uploads/" + file);
	res.writeHead(200, {'Content-Type': 'image/png' });
	res.end(img, 'binary');

});

async function main() {
	// ... you will write your Prisma Client queries here
}

main()
	.catch(e => {
		throw e
	})
	.finally(async () => {
		await prisma.disconnect()
	})



server.start(options, ({port}) => 
	console.log("Graphql Server Running : port = "+`${port}`)
);
