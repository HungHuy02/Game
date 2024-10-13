const asyncHandler = require("express-async-handler");
const redis = require('../../utils/redisRankUtil');

const getCurrentUserRank = asyncHandler(async (req, res) => {
    const {id} = req.user;
    const result = await redis.getUserRanking(id);
    return res.status(200).json({
        success: true,
        rank: result
    });
});

const getRank = asyncHandler (async (req, res) => {
    const result = await redis.getRank(10);
    return res.status(200).json({
        success: true,
        list: result,
    })
});


module.exports = {
    getCurrentUserRank,
    getRank,
}