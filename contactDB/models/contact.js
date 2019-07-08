const mongoose = require('mongoose');

// Define Schemes
const contactSchema = new mongoose.Schema({
  name: {
    type: String,
    required: true
  },
  phonenumber: {
    type: String,
    required: true,
    unique: true
  },
  iconID: {
    type: Number,
    default: null
  },
  pID: {
    type: Number,
    default: null
  },
  ImageStr: {
    type: String,
    default: null
  }
}, {
  timestamps: true
});

// Create new contact document
contactSchema.statics.create = function(payload) {
  // this === Model
  const contact = new this(payload);
  // return Promise
  return contact.save();
};
// Find All
contactSchema.statics.findAll = function() {
  // return promise
  // V4부터 exec() 필요없음
  return this.find({});
};
// Find One by contactid
contactSchema.statics.findOneByPhonenumber = function(phonenumber) {
  return this.findOne({
    phonenumber
  });
};
// Update by contactid
contactSchema.statics.updateByPhonenumber = function(phonenumber, payload) {
  // { new: true }: return the modified document rather than the original. defaults to false
  return this.findOneAndUpdate({
    phonenumber
  }, payload, {
    new: true
  });
};
// Delete by contactid
contactSchema.statics.deleteByPhonenumber = function(phonenumber) {
  return this.remove({
    phonenumber
  });
};

// contactSchema.statics.resetAll = function() {
//   var all = this.find({});
//   console.log(typeof(all));
//   for (int i = 0; i < sizeof(all); i++) {
//     // all.forEach(function(element){
//     contactSchema.statics.deleteByPhonenumber(element.phonenumber);
//     // });
//   }
//   return true;
// };


module.exports = mongoose.model('Contact', contactSchema);
