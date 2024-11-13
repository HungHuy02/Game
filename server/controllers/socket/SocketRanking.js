let ioInstance;

const initialize = (io) => {
    ioInstance  = io;
    io.of("/ranking").on("connection", () => {});
}

const updateRanking = () => {
    ioInstance.of("/ranking").emit("newRanking");
}


module.exports = {
    initialize,
    updateRanking
}