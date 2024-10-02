const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();
const asyncHandler = require("express-async-handler");
const bcrypt = require('bcrypt');
const jwtUtil = require('../../utils/jwtUtil');

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
        const hashedPassword = await bcrypt.hash(password, salt)
        const newUser = await prisma.user.create({
            data: {
                name: name,
                email: email,
                password: hashedPassword,
            }
        });
    
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

        return res.status(200).json({
            success: true,
            accessToken: accessToken,
            refreshToken: refreshToken,
            userData: {
                id: user.id,
                name: user.name,
                email: user.email
            }
        });
    }else {
        return res.status(401).json({
            success: false,
            message: "Password or email is incorrect",
        });
    }
});

module.exports = {
    checkExistingUser,
    register,
    login,
  };