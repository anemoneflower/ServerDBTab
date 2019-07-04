const router = require('express').Router();
const Todo = require('../models/todo');

// Find All
router.get('/', (req, res) => {
  console.log('who get in here : get');
  Todo.findAll()
    .then((todos) => {
      if (!todos.length) return res.status(404).send({
        err: 'Todo not found'
      });
      res.send(`find successfully: ${todos}`);
    })
    .catch(err => res.status(500).send(err));
});

// Find One by todoid
router.get('/phonenumber/:phonenumber', (req, res) => {
  console.log('who get in here/todoid/:todoid');
  Todo.findOneByTodoid(req.params.phonenumber)
    .then((todo) => {
      if (!todo) return res.status(404).send({
        err: 'Todo not found'
      });
      res.send(`findOne successfully: ${todo}`);
    })
    .catch(err => res.status(500).send(err));
});

// Create new todo document
router.post('/', (req, res) => {
  console.log('who get in here : post');
  Todo.create(req.body)
    .then(todo => res.send(todo))
    .catch(err => res.status(500).send(err));
  // var inputData;
  //
  // req.on('data', (data) => {
  //   inputData = JSON.parse(data);
  // });
  //
  // req.on('end', () => {
  //   console.log("todoid : " + inputData.todoid + " , content : " + inputData.content + ", completed : " + inputData.completed);
  // });
  //
  // res.write("OK!");
  // res.end();
});

// Update by todoid
router.put('/phonenumber/:phonenumber', (req, res) => {
  Todo.updateByTodoid(req.params.phonenumber, req.body)
    .then(todo => res.send(todo))
    .catch(err => res.status(500).send(err));
});

// Delete by todoid
router.delete('/phonenumber/:phonenumber', (req, res) => {
  Todo.deleteByTodoid(req.params.phonenumber)
    .then(() => res.sendStatus(200))
    .catch(err => res.status(500).send(err));
});

module.exports = router;
