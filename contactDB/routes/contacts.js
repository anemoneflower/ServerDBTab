const router = require('express').Router();
const Contact = require('../models/contact');

// Find All
router.get('/userID/:userID', (req, res) => {
  console.log('who get in here : get');
  Contact.findAll(req.params.userID)
    .then((contacts) => {
      if (!contacts.length) return res.status(404).send({
        err: 'Contact not found'
      });
      res.send(contacts);
    })
    .catch(err => res.status(500).send(err));
});

// Find One by contactid
router.get('/phonenumber/:phonenumber/userID/:userID', (req, res) => {
  console.log('who get in here/phonenumber/:phonenumber');
  Contact.findOneByPhonenumber(req.params.phonenumber, req.params.userID)
    .then((contact) => {
      if (!contact) return res.status(404).send({
        err: 'Contact not found'
      });
      console.log(req.params.phonenumber + "  "+ req.params.userID);
      res.send(contact);
    })
    .catch(err => res.status(500).send(err));
});

// Post total contact document
router.post('/initialize', (req, res) => {
  console.log('who get in here : post - initialize');
  var jsonarr = req.body;
  var sendarr = [];
  jsonarr.some(function(element){//forEach(function(element) {
    // console.log("-------------------foreach inside");
    Contact.create(element)
      .then(contact => sendarr.push(contact)) //.res.send(contact))
      .catch(err => {res.status(500).send(err); return true;});
  });
  res.send(sendarr);
});

// Post one contact
router.post('/', (req, res) => {
  console.log('who get in here : post');

  Contact.create(req.body)
    .then(contact => res.send(contact))
    .catch(err => res.status(500).send(err));

});

// Update by phonenumber
router.put('/phonenumber/:phonenumber', (req, res) => {
  console.log('who get in here : put');
  Contact.updateByPhonenumber(req.params.phonenumber, req.body)
    .then(contact => res.send(contact))
    .catch(err => res.status(500).send(err));
});

// Delete by phonenumber
router.delete('/phonenumber/:phonenumber/userID/:userID', (req, res) => {
  console.log('who get in here : delete');
  Contact.deleteByPhonenumber(req.params.phonenumber, req.params.userID)
    .then(() => res.sendStatus(200))
    .catch(err => res.status(500).send(err));
});

router.delete('/reset/userID/:userID', (req, res) => {
  console.log('who get in here : reset');
  Contact.findAll(req.params.userID)
    .then((contacts) => {
      console.log('findall.....');
      var phonenums = contacts.filter(function(item){return item.phonenumber != null;});
      console.log('phonenums succedd!!!');
      phonenums.forEach(function(element){
        Contact.deleteByOnlyPhonenumber(element.phonenumber)
          .then(()=>res.sendStatus(200))
          .catch(err=>res.status(500).send(err));
      });
    });
    //   if (!contacts.length) return res.status(404).send({
    //     err: 'Contact not found'
    //   });
    //   res.send(`find successfully: ${contacts}`);
    // })
    // .catch(err => res.status(500).send(err));
});

// // Reset All
// router.delete('/reset', (req, res) => {
//   console.log('who get in here : reset');
//   Contact.resetAll()
//     .then((result) => {
//       if (result != true) return res.status(404).send({
//         err: 'ResetError'
//       });
//       res.send(`Reset Successfully!`);
//     })
//     .catch(err => res.status(500).send(err));
// });
module.exports = router;
