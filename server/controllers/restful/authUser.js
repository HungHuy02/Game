const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();
const asyncHandler = require("express-async-handler");
const bcrypt = require('bcrypt');
const jwtUtil = require('../../utils/jwtUtil');
const jwt = require("jsonwebtoken");
const redis = require('../../utils/redisRankUtil');

const checkExistingUser = asyncHandler(async (req, res) => {
    const { email } = req.query;
    const check = await prisma.user.check.findUnique({
        where: {email: email}
    });
    return res.status(200).json({
        data: check ? true : false,
    });
});

const register = asyncHandler(async (req, res) => {
    const {name, email, password} = req.body;
    if(!name || !email || !password) {
        return res.status(400).json({
            success: false,
            message: "Missing input",
        });
    }
    
    const user = await prisma.user.findUnique({
        where: { email: email}
    });
    
    if(user) {
        return res.status(400).json({
            success: false,
            message: "User already exists",
        });
    }else {
        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(password, salt);
        const newUser = await prisma.user.create({
            data: {
                name: name,
                email: email,
                password: hashedPassword,
                elo: 400,
            }
        });

        await redis.updateUserScore(newUser.id, 400, name);
    
        return res.status(200).json({
            success: newUser ? true : false,
            message: newUser
                ? "Registration successful. Please proceed to login."
                : "Something went wrong",
        });
    }
});

const login = asyncHandler(async (req, res) => {
    const { email, password} = req.body;
    if(!email || !password) {
        return res.status(400).json({
            success: false,
            message: "Missing input",
        });
    }

    const user = await prisma.user.findUnique({
        where: {email: email}
    });

    if(user && await bcrypt.compare(password, user.password)) {
        const accessToken = jwtUtil.generateAccessToken(user.id, user.name);
        const refreshToken = jwtUtil.generateRefreshToken(user.id);
        const updateUser = await prisma.user.update({
            where: {email: email},
            data: {refresh_token: refreshToken}
        });

        if(!updateUser) {
            res.status(400);
            throw new Error('error');
        }

        return res.status(200).json({
            success: true,
            accessToken: accessToken,
            refreshToken: refreshToken,
            userData: {
                id: user.id,
                name: user.name,
                email: user.email,
                imageUrl: user.image_url
            }
        });
    }else {
        return res.status(401).json({
            success: false,
            message: "Password or email is incorrect",
        });
    }
});

const refreshAccessToken = asyncHandler(async (req, res) => {
    const { refreshToken } = req.body;
    if (!refreshToken) {
        return res.status(401).json({ error: "No refresh token" });
      }
    
    try {
        const decodedToken = await jwt.verify(refreshToken, process.env.JWT_SECRET);
        const response = await prisma.user.findUnique({
            where: {id: decodedToken.id}
        })
    
        if (!response) {
          return res.status(401).json({ error: "Invalid refresh token" });
        }

        if (refreshToken !== response.refresh_token) {
            return res.status(401).json({ error: "Invalid refresh token" });
        }
    
        const newAccessToken = jwtUtil.generateAccessToken(response.id, response.name);
        return res.status(200).json({ success: true, accessToken: newAccessToken });
    } catch (error) {
        return res.status(401).json({ error: "Invalid refresh token" });
    }
});

module.exports = {
    checkExistingUser,
    register,
    login,
    refreshAccessToken,
  };