const eloUtil = require('../../utils/eloUtil');
const redisClient = require('../../config/redisConfig');
const jwt = require("jsonwebtoken");
const asyncHandler = require("express-async-handler");
const loadScripts = require("../../luaScripts/loadScripts");
const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();
const redis = require('../../utils/redisRankUtil');
const socketRanking = require('./SocketRanking');


const REDIS_PLAYING_KEY = "playing";

const updateNewElo = async (playerId, newElo, playerName, opponentId, newOpponentElo, opponentName) => {
    await prisma.user.update({
        where: {id: playerId},
        data: {
            elo: newElo,
        }
    });
    await redis.updateUserScore(playerId, newElo, playerName);
    await prisma.user.update({
        where: {id: opponentId},
        data: {
            elo: newOpponentElo,
        }
    });
    await redis.updateUserScore(opponentId, newOpponentElo, opponentName);
    socketRanking.updateRanking();

};

module.exports = function(io) {
    const allConnections = {};
    const waitingList = [];

    io.use((socket, next) => {
        const token = socket.handshake.auth.token;
        if (token) {
            jwt.verify(token, process.env.JWT_SECRET, (err, decode) => {
                if (err) return next(new Error('Authentication error'));
                socket.user = decode;
                next();
            });
        }else {
            next(new Error('Authentication error'));
        }
    }).on("connection", (socket) => {
        allConnections[socket.id] = socket;

        socket.on("request_to_play", asyncHandler(async(data) => {

            const currentUser = {};
            currentUser.id = socket.user.id;
            currentUser.elo = data.elo;
            currentUser.playerName = data.playerName;
            currentUser.imageUrl = data.imageUrl;
            currentUser.timeType = data.timeType;
            currentUser.socketId = socket.id;

            const opponentPlayerSocketId = await redisClient.HGET(REDIS_PLAYING_KEY, currentUser.id + "");;
            if(opponentPlayerSocketId) {
                const opponentSocket = allConnections[opponentPlayerSocketId];
                opponentSocket.removeAllListeners();

                let opponentPlayerElo;

                socket.emit("arePlaying");

                opponentSocket.emit("opponentComeback");

                opponentSocket.once("sendCurrentGameState", (data) => {
                    opponentPlayerElo = data.elo;
                    socket.emit("currentGameState", {
                        ...data,
                    });
                });

                socket.on("playerMove", (data) => {
                    opponentSocket.emit("opponentMove", {
                        ...data,
                    });
                });

                opponentSocket.on("playerMove", (data) => {
                    socket.emit("opponentMove", {
                        ...data,
                    });
                });

                socket.on("canDraw", () => {
                    opponentSocket.emit("opponentWantToDraw");
                });

                opponentSocket.on("canDraw", () => {
                    socket.emit("opponentWantToDraw");
                });

                socket.once("gameEnd", async (data) => {
                    await redisClient.HDEL(REDIS_PLAYING_KEY, [socket.user.id + "", opponentSocket.user.id + ""]);
                    const {newEloA, newEloB} = eloUtil.newElo(currentUser.elo, opponentPlayerElo, data.result, pieceColor);
                    socket.emit("newScore", {
                        newScore: newEloA,
                    });
                    opponentSocket.emit("newScore", {
                        newScore: newEloB,
                    });
                    updateNewElo(socket.user.id, newEloA, opponentSocket.user.id, newEloB);
                });

                opponentSocket.once("gameEnd", async (data) => {
                    await redisClient.HDEL(REDIS_PLAYING_KEY, [socket.user.id + "", opponentSocket.user.id + ""]);
                    const {newEloA, newEloB} = eloUtil.newElo(currentUser.elo, opponentPlayerElo, data.result, pieceColor);
                    socket.emit("newScore", {
                        newScore: newEloA,
                    });
                    opponentSocket.emit("newScore", {
                        newScore: newEloB,
                    });
                    updateNewElo(socket.user.id, newEloA, opponentSocket.user.id, newEloB);
                });
            }else {
                socket.index = waitingList.push(currentUser) - 1;

                let opponentPlayer;

                for (let i = 0; i < waitingList.length; i++) {
                    const user = waitingList[i];
                    if (currentUser.timeType === user.timeType && socket.id !== user.socketId) {
                        opponentPlayer = user;
                        break;
                    }
                }

                if(opponentPlayer) {
                    waitingList.splice(waitingList.indexOf(currentUser), 1);
                    waitingList.splice(waitingList.indexOf(opponentPlayer), 1);
                    redisClient.EVALSHA(await loadScripts.getAddToPlayingHashSHA(), {
                        keys: [REDIS_PLAYING_KEY],
                        arguments: [currentUser.id + '',
                            opponentPlayer.id + '',
                            socket.id,
                            opponentPlayer.socketId
                        ]
                    });

                    const pieceColor = Math.random() === 0;

                    const opponentSocket = allConnections[opponentPlayer.socketId];

                    socket.emit("opponentFound", {
                        opponentName: opponentPlayer.playerName,
                        imageUrl: opponentPlayer.imageUrl ? opponentPlayer.imageUrl : "",
                        isWhite: pieceColor,
                    });

                    opponentSocket.emit("opponentFound", {
                        opponentName: currentUser.playerName,
                        imageUrl: opponentPlayer.imageUrl ? opponentPlayer.imageUrl : "",
                        isWhite: !pieceColor,
                    });

                    socket.on("playerMove", (data) => {
                        opponentSocket.emit("opponentMove", {
                            ...data,
                        });
                    });

                    opponentSocket.on("playerMove", (data) => {
                        socket.emit("opponentMove", {
                            ...data,
                        });
                    });

                    socket.on("canDraw", () => {
                        opponentSocket.emit("opponentWantToDraw");
                    });

                    opponentSocket.on("canDraw", () => {
                        socket.emit("opponentWantToDraw");
                    });

                    socket.once("gameEnd", async (data) => {
                        await redisClient.HDEL(REDIS_PLAYING_KEY, [socket.user.id + "", opponentSocket.user.id + ""]);
                        const {newEloA, newEloB} = eloUtil.newElo(currentUser.elo, opponentPlayer.elo, data.result, pieceColor);
                        socket.emit("newScore", {
                            newScore: newEloA,
                        });
                        opponentSocket.emit("newScore", {
                            newScore: newEloB,
                        });
                        updateNewElo(socket.user.id, newEloA, currentUser.playerName,opponentSocket.user.id, newEloB, opponentPlayer.playerName);
                    });

                    opponentSocket.once("gameEnd", async (data) => {
                        await redisClient.HDEL(REDIS_PLAYING_KEY, [socket.user.id + "", opponentSocket.user.id + ""]);
                        const {newEloA, newEloB} = eloUtil.newElo(currentUser.elo, opponentPlayer.elo, data.result, pieceColor);
                        socket.emit("newScore", {
                            newScore: newEloA,
                        });
                        opponentSocket.emit("newScore", {
                            newScore: newEloB,
                        });
                        updateNewElo(socket.user.id, newEloA, currentUser.playerName,opponentSocket.user.id, newEloB, opponentPlayer.playerName);
                    });
                }else {
                    socket.emit("OpponentNotFound");
                }
            }
        }));

        socket.on("disconnect", asyncHandler(async() => {
            const opponentSocketId = await redisClient.HGET(REDIS_PLAYING_KEY, socket.user.id + "");
            for(var i = 0; i < waitingList.length && i <= socket.index; i++) {
                if(waitingList[i].id === socket.user.id) {
                    waitingList.splice(i, 1);
                    break;
                }
            }
            delete allConnections[socket.id];
            if(opponentSocketId) {
                const opponentSocket = allConnections[opponentSocketId];
                if(opponentSocket) {
                    opponentSocket.opponentId = socket.user.id;
                    opponentSocket.emit("opponentLeftMatch");
                }else {
                    redisClient.HDEL(REDIS_PLAYING_KEY, [socket.user.id + "", socket.opponentId + ""]);
                }
            }
        }));
    });

    const guestWaitingList = [];

    io.of("/guest").on("connection", (socket) => {
        allConnections[socket.id] = socket;

        socket.on("request_to_play", async(data) => {

            const currentUser = {};
            currentUser.playerName = data.playerName;
            currentUser.timeType = data.timeType;
            currentUser.socketId = socket.id;

            socket.index = guestWaitingList.push(currentUser) - 1;

            let opponentPlayer;

            for (let i = 0; i < guestWaitingList.length; i++) {
                const user = guestWaitingList[i];
                if (currentUser.timeType === user.timeType && socket.id !== user.socketId) {
                    opponentPlayer = user;
                    break;
                }
            }

            if(opponentPlayer) {
                guestWaitingList.splice(waitingList.indexOf(currentUser), 1);
                guestWaitingList.slice(waitingList.indexOf(opponentPlayer), 1);
                redisClient.EVALSHA(await loadScripts.getAddToPlayingHashSHA(), {
                    keys: [REDIS_PLAYING_KEY],
                    arguments: [socket.id,
                        opponentPlayer.socketId,
                        socket.id,
                        opponentPlayer.socketId
                    ]
                });

                const pieceColor = Math.random() === 0;

                const opponentSocket = allConnections[opponentPlayer.socketId];

                socket.emit("opponentFound", {
                    opponentName: opponentPlayer.playerName,
                    imageUrl: "",
                    isWhite: pieceColor,
                });

                opponentSocket.emit("opponentFound", {
                    opponentName: currentUser.playerName,
                    imageUrl: "",
                    isWhite: !pieceColor,
                });

                socket.on("playerMove", (data) => {
                    opponentSocket.emit("opponentMove", {
                        ...data,
                    });
                });

                opponentSocket.on("playerMove", (data) => {
                    socket.emit("opponentMove", {
                        ...data,
                    });
                });

                socket.on("canDraw", () => {
                    opponentSocket.emit("opponentWantToDraw");
                });

                opponentSocket.on("canDraw", () => {
                    socket.emit("opponentWantToDraw");
                });

                socket.once("gameEnd", async (data) => {
                    await redisClient.HDEL(REDIS_PLAYING_KEY, [socket.user.id + "", opponentSocket.user.id + ""]);
                    const {newEloA, newEloB} = eloUtil.newElo(currentUser.elo, opponentPlayer.elo, data.result, pieceColor);
                    socket.emit("newScore", {
                        newScore: newEloA,
                    });
                    opponentSocket.emit("newScore", {
                        newScore: newEloB,
                    });
                    updateNewElo(socket.user.id, newEloA, currentUser.playerName,opponentSocket.user.id, newEloB, opponentPlayer.playerName);
                });

                opponentSocket.once("gameEnd", async (data) => {
                    await redisClient.HDEL(REDIS_PLAYING_KEY, [socket.user.id + "", opponentSocket.user.id + ""]);
                    const {newEloA, newEloB} = eloUtil.newElo(currentUser.elo, opponentPlayer.elo, data.result, pieceColor);
                    socket.emit("newScore", {
                        newScore: newEloA,
                    });
                    opponentSocket.emit("newScore", {
                        newScore: newEloB,
                    });
                    updateNewElo(socket.user.id, newEloA, currentUser.playerName,opponentSocket.user.id, newEloB, opponentPlayer.playerName);
                });
            }else {
                socket.emit("OpponentNotFound");
            }
        });

        socket.on("disconnect", asyncHandler(async() => {
            for(var i = 0; i < guestWaitingList.length && i <= socket.index; i++) {
                if(guestWaitingList[i].id === socket.id) {
                    guestWaitingList.splice(i, 1);
                    break;
                }
            }
            const socketId = socket.id;
            delete allConnections[socket.id];
            const opponentSocketId = await redisClient.HGET(REDIS_PLAYING_KEY, socket.id);
            if(opponentSocketId) {
                if(opponentSocketId) {
                    const opponentSocket = allConnections[opponentSocketId];
                    opponentSocket.emit("opponentLeftMatch");
                }
                redisClient.HDEL(REDIS_PLAYING_KEY, [socketId, opponentSocketId]);
            }
        }));
    });
}
