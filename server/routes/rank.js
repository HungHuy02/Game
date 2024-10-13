var express = require('express');
var router = express.Router();

const rankController = require('../controllers/restful/rank');

router.get('/get-current-rank', rankController.getCurrentUserRank);
router.get('/get-rank', rankController.getRank);

module.exports = router;