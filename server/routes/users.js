var express = require('express');
var router = express.Router();

const userController = require('../controllers/restful/user');

router.post('/updateUser', userController.updateUser);

module.exports = router;
