const router = require('express').Router();
const Simsim = require('../models/simsim');

// Find All
router.get('/', (req, res) => {
  console.log('who get in here : get');
  Simsim.findAll()
    .then((contacts) => {
      if (!contacts.length) return res.status(404).send({
        err: 'SimSim not found'
      });
      res.send(contacts);
    })
    .catch(err => res.status(500).send(err));
});



// // Post total simsim document
// router.post('/initialize', (req, res) => {
//   console.log('who get in here : post - initialize');
//   var jsonarr = req.body;
//   var sendarr = [];
//   jsonarr.some(function(element){//forEach(function(element) {
//     // console.log("-------------------foreach inside");
//     Simsim.create(element)
//       .then(simsim => sendarr.push(simsim)) //.res.send(simsim))
//       .catch(err => {res.status(500).send(err); return true;});
//   });
//   res.send(sendarr);
// });

// Post one simsim
router.post('/', (req, res) => {
  console.log('who get in here : post');

  Simsim.create(req.body)
    .then(simsim => res.send(simsim))
    .catch(err => res.status(500).send(err));

});

// Update by phonenumber
router.put('/phonenumber/:phonenumber', (req, res) => {
  console.log('who get in here : put');
  Simsim.updateByPhonenumber(req.params.phonenumber, req.body)
    .then(simsim => res.send(simsim))
    .catch(err => res.status(500).send(err));
});

// Delete by phonenumber
router.delete('/phonenumber/:phonenumber/userID/:userID', (req, res) => {
  console.log('who get in here : delete');
  Simsim.deleteByPhonenumber(req.params.phonenumber, req.params.userID)
    .then(() => res.sendStatus(200))
    .catch(err => res.status(500).send(err));
});

router.delete('/reset/userID/:userID', (req, res) => {
  console.log('who get in here : reset');
  Simsim.findAll(req.params.userID)
    .then((simsims) => {
      console.log('findall.....');
      var phonenums = simsims.filter(function(item){return item.phonenumber != null;});
      console.log('phonenums succedd!!!');
      phonenums.forEach(function(element){
        Simsim.deleteByOnlyPhonenumber(element.phonenumber)
          .then(()=>res.sendStatus(200))
          .catch(err=>res.status(500).send(err));
      });
    });
    //   if (!simsims.length) return res.status(404).send({
    //     err: 'Simsim not found'
    //   });
    //   res.send(`find successfully: ${simsims}`);
    // })
    // .catch(err => res.status(500).send(err));
});

// // Reset All
// router.delete('/reset', (req, res) => {
//   console.log('who get in here : reset');
//   Simsim.resetAll()
//     .then((result) => {
//       if (result != true) return res.status(404).send({
//         err: 'ResetError'
//       });
//       res.send(`Reset Successfully!`);
//     })
//     .catch(err => res.status(500).send(err));
// });
module.exports = router;
