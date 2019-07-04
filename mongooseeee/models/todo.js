const mongoose = require('mongoose');

// Define Schemes
const todoSchema = new mongoose.Schema({
  name: { type: Number, required: true},
  phonenumber: { type: String, required: true, unique: true  },
  iconID: { type: String, default: false },
  pID: {type: Number}
},
{
  timestamps: true
});

// Create new todo document
todoSchema.statics.create = function (payload) {
  // this === Model
  const todo = new this(payload);
  // return Promise
  return todo.save();
};
// Find All
todoSchema.statics.findAll = function () {
  // return promise
  // V4부터 exec() 필요없음
  return this.find({});
};
// Find One by todoid
todoSchema.statics.findOneByPhonenumber = function (phonenumber) {
  return this.findOne({ phonenumber });
};
// Update by todoid
todoSchema.statics.updateByPhonenumber = function (phonenumber, payload) {
  // { new: true }: return the modified document rather than the original. defaults to false
  return this.findOneAndUpdate({ phonenumber }, payload, { new: true });
};
// Delete by todoid
todoSchema.statics.deleteByPhonenumber = function (phonenumber) {
  return this.remove({ phonenumber });
};
// const Todo = mongoose.model('Todo', todoSchema);
// Todo.find({})
//   .then(todo => console.log(todo))
//   .catch(err => console.log(err))
//
// const todo = new Todo();
// todo.todoid = 1;
// todo.content = 'MongoDB';
// todo.completed = false;
//
// todo.save
//   .then(() => console.log('Saved successfully'));


module.exports = mongoose.model('Todo', todoSchema);
