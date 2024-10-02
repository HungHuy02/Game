const jwt = require("jsonwebtoken");

const generateAccessToken = (id, name) =>
    jwt.sign({ id, name }, process.env.JWT_SECRET, { expiresIn: "1800s" });

const generateRefreshToken = (id) =>
    jwt.sign({ id }, process.env.JWT_SECRET, { expiresIn: "7d" });

module.exports = {
    generateAccessToken,
    generateRefreshToken,
};