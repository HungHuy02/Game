var express = require('express');
var router = express.Router();

const rankController = require('../controllers/restful/rank');

router.get('/get-current-rank', rankController.getCurrentUserRank);
router.get('/get-rank', rankController.getRank);
router.get('/get-all-rank-user', rankController.getCurrentRankAndAllRanks);

module.exports = router;