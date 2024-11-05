local hashKey = KEYS[1]
local playerId = ARGV[1]
local opponentId = ARGV[2]
local playerSocketId = ARGV[3] 
local opponentSocketId = ARGV[4] 

redis.call('HSETNX', hashKey, playerId, opponentSocketId) 
redis.call('HSETNX', hashKey, opponentId, playerSocketId)