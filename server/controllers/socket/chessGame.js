module.exports = function(io) {
    const allUsers = {};
    const allRooms = [];

    io.on("connection", (socket) => {
        allUsers[socket.id] = {
            socket: socket,
            onine: true,
        };

        socket.on("request_to_play", (data) => {
            const currentUser = allUsers[socket.id];
            currentUser.playerName = data.playerName;
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

                const pieceColor = Math.random() === 0 ? true : false;

                currentUser.socket.emit("opponentFound", {
                    opponentName: opponentPlayer.playerName,
                    isWhite: pieceColor,
                });

                opponentPlayer.socket.emit("opponentFound", {
                    opponentName: opponentPlayer.playerName,
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