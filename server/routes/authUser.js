var express = require('express');
var router = express.Router();

const authUserController = require('../controllers/restful/authUser');

router.get("/check-existing-user", authUserController.checkExistingUser);
router.post("/login", authUserController.login);
router.post("/register", authUserController.register);

module.exports = router;