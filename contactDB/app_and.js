const express = require('express');
const app = express();

let users = [{
    todoid: 1,
    content: 'alice',
    completed: "false"
  },
  {
    todoid: 2,
    content: "MongoDB",
    completed: "false"
  }
]

app.get('/users', (req, res) => {
  console.log('who get in here/users');
  res.json(users)
});

app.post('/post', (req, res) => {
  console.log('who get in here post /users');
  var inputData;

  req.on('data', (data) => {
    inputData = JSON.parse(data);
  });

  req.on('end', () => {
    console.log("todoid : " + inputData.todoid + " , content : " + inputData.content);
  });

  res.write("OK!");
  res.end();
});

app.listen(4500, () => {
  console.log('Example app listening on port 3000!');
});
