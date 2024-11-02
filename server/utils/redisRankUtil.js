const redisClient = require("../config/redisConfig");

const REDIS_KEY = 'rank';
const REDIS_DATA_KEY = 'rank_data';

const updateUserScore = async (userId, score, userName) => {
    await redisClient.zAdd(REDIS_KEY, [{score: score, value: `${userId}`}]);
    if(!userName) return;
    await redisClient.hSetNX(REDIS_DATA_KEY, `${userId}`, userName);
};

const updateUserName = async (userId, userName) => {
    await redisClient.hSet(REDIS_DATA_KEY, `${userId}`, userName);
}

const getUserRanking = async (userId) => {
    const userRank = await redisClient.zRevRank(REDIS_KEY, `${userId}`);
    return userRank + 1;
};

const getRank = async (limit) => {
    try {
        const result = [];
        const userRankingSet = await redisClient.ZRANGE_WITHSCORES(REDIS_KEY, 0, limit, {
            REV: true,
          });
        const topUserScore = [];
        const topUserId = [];
        userRankingSet.forEach((value) => {
            topUserScore.push(value.score);
            topUserId.push(value.value);
        });

        const listUserName = await redisClient.hmGet(REDIS_DATA_KEY, [...topUserId]);

        for(let i = 0; i < topUserScore.length; i++) {
            result.push({
                id: topUserId[i],
                ranking: i + 1,
                name: listUserName[i],
                score: topUserScore[i]
            });
        }
        return result;
    }catch (error) {
        return [];
    }
}

module.exports = {
    updateUserScore,
    updateUserName,
    getUserRanking,
    getRank
}
