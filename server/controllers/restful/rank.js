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

const getCurrentRankAndAllRanks = asyncHandler(async (req, res) => {
    const {id} = req.user;
    const currentRank = await redis.getUserRanking(id);
    const allRanks = await redis.getRank(10);
    return res.status(200).json({
        success: true,
        rank: currentRank,
        list: allRanks,
    })
});

module.exports = {
    getCurrentUserRank,
    getRank,
    getCurrentRankAndAllRanks,
}