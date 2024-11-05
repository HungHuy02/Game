const fs = require('fs');
const redisClient = require('../config/redisConfig');

const addToPlayingHashScript = fs.readFileSync('./luaScripts/addToPlayingHash.lua', 'utf8');

let cachedSHA = null;

async function getAddToPlayingHashSHA() {
    if (cachedSHA) {
        return cachedSHA; 
    }

    try {
        cachedSHA = await redisClient.scriptLoad(addToPlayingHashScript);
        return cachedSHA;
    } catch (err) {
        console.error("Error loading Lua script:", err);
        throw err;
    }
}

module.exports = {
    getAddToPlayingHashSHA,
}