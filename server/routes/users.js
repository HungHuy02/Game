var express = require('express');
var router = express.Router();

const userController = require('../controllers/restful/user');

router.get('/get-current', userController.getCurrentUser);
router.post('/update-user', userController.updateUser);

module.exports = router;
