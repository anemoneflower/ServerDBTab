// ENV
require('dotenv').config();
// DEPENDENCIES
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');

const app = express();
const port = process.env.PORT || 4500;

// Static File Service
app.use(express.static('public'));
// Body-parser
app.use(bodyParser.urlencoded({
  limit: '500mb',
  extended: true
}));
app.use(bodyParser.json({
  limit: '500mb'
})); //, extended: true

// Node.js의 native Promise 사용
mongoose.Promise = global.Promise;

// CONNECT TO MONGODB SERVER
mongoose.connect(process.env.MONGO_URI, {
    useNewUrlParser: true
  })
  // useMongoClient: true
  .then(() => console.log('Successfully connected to mongodb'))
  .catch(e => console.error(e));

// ROUTERS
app.use('/contacts', require('./routes/contacts'));
app.use('/simsims', require('./routes/simsims'));

app.listen(port, () => console.log(`Server listening on port ${port}`));
