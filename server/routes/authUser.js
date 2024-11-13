var express = require('express');
var router = express.Router();

const authUserController = require('../controllers/restful/authUser');

router.get("/check-existing-user", authUserController.checkExistingUser);
router.post("/login", authUserController.login);
router.post("/register", authUserController.register);
router.post("/refresh-token", authUserController.refreshAccessToken);
router.post("/login-with-google", authUserController.loginGoogle);

module.exports = router;