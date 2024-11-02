const eloUtil = require('../../utils/eloUtil');
const redisClient = require('../../config/redisConfig');
const jwt = require("jsonwebtoken");

const REDIS_REQUEST_PLAY_KEY = "request_to_play";
const REDIS_PLAYING_KEY = "playing";

module.exports = function(io) {
    const allUsers = {};
    const allRooms = [];

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
        allUsers[socket.id] = {
            socket: socket,
            onine: true,
        };

        socket.on("request_to_play", (data) => {

            redisClient.SADD(REDIS_REQUEST_PLAY_KEY, socket.user.id + "");

            const currentUser = allUsers[socket.id];
            currentUser.playerName = data.playerName;
            currentUser.imageUrl = data.imageUrl;
            currentUser.elo = data.elo;
            currentUser.online = true;
            currentUser.playing = false;
            
            let opponentPlayer;

            for(const key in allUsers) {
                const user = allUsers[key];
                if(user.online && !user.playing && socket.id !== key) {
                    opponentPlayer = user;
                    break;
                }
            }

            if(opponentPlayer) {
                allRooms.push({
                    player1: opponentPlayer,
                    player2: currentUser,
                });

                const pieceColor = Math.random() === 0;

                currentUser.socket.emit("opponentFound", {
                    opponentName: opponentPlayer.playerName,
                    imageUrl: opponentPlayer.imageUrl,
                    isWhite: pieceColor,
                });

                opponentPlayer.socket.emit("opponentFound", {
                    opponentName: currentUser.playerName,
                    imageUrl: currentUser.imageUrl,
                    isWhite: !pieceColor,
                });

                currentUser.socket.on("playerMove", (data) => {
                    opponentPlayer.socket.emit("opponentMove", {
                        ...data,
                    });
                });

                opponentPlayer.socket.on("playerMove", (data) => {
                    currentUser.socket.emit("opponentMove", {
                        ...data,
                    });
                });

                currentUser.socket.on("canDraw", () => {
                    opponentPlayer.socket.emit("opponentWantToDraw");
                });

                opponentPlayer.socket.on("canDraw", () => {
                    currentUser.socket.emit("opponentWantToDraw");
                });

                currentUser.socket.on("gameEnd", (data) => {
                    const {newEloA, newEloB} = eloUtil.newElo(currentUser.elo, opponentPlayer.elo, data.result, pieceColor);
                    currentUser.socket.emit("newScore", {
                        newScore: newEloA,
                    });
                    opponentPlayer.socket.emit("newScore", {
                        newScore: newEloB,
                    });
                });

                opponentPlayer.socket.on("gameEnd", (data) => {
                    const {newEloA, newEloB} = eloUtil.newElo(currentUser.elo, opponentPlayer.elo, data.result, !pieceColor);
                    currentUser.socket.emit("newScore", {
                        newScore: newEloA,
                    });
                    opponentPlayer.socket.emit("newScore", {
                        newScore: newEloB,
                    });
                });
            }else {
                currentUser.socket.emit("OpponentNotFound");
            }
        });

        socket.on("disconnect", function() {
            const currentUser = allUsers[socket.id];
            currentUser.online = false;
            currentUser.playing = false;

            for(let index = 0; index < allRooms.length; index++) {
                const { player1, player2 } = allRooms[index];

                if(player1.socket.id === socket.id) {
                    player2.socket.emit("opponentLeftMatch");
                    break;
                }

                if(player2.socket.id === socket.id) {
                    player1.socket.emit("opponentLeftMatch");
                    break;
                }
            }
        });
    });
}
