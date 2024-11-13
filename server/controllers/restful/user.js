const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();
const asyncHandler = require("express-async-handler");
const cloudinary = require("../../config/cloudinaryConfig");
const redis = require('../../utils/redisRankUtil');
const socketRanking = require('../socket/SocketRanking');

const getCurrentUser = asyncHandler(async (req, res) => {
    const { id } = req.user;
    const user = await prisma.user.findUnique({
        where: {id: id},
    });

    if(user) {
        return res.status(200).json({
            name: user.name,
            email: user.email,
            imageUrl: user.image_url,
            elo: user.elo,
        });
    }else {
        res.status(400);
        throw new Error("error");
    }
});

const deleteUserImage = asyncHandler(async (req, res) => {
    const { id } = req.user;
    const result = await cloudinary.uploader.destroy(id);
    if(result.result) {
        const imageUrl = '';
        const user = await prisma.user.update({
            where: {id: id},
            data: {
                image_url: imageUrl,
            }
        });
        if(user) {
            return res.status(200).json({
                id: id,
                success: true,
            })
        }else {
            throw new Error("error");
        }
    }else {
        throw new Error("error");
    }
});

const updateUser = asyncHandler(async (req, res) => {
    const { name, email, imageUrl, elo} = req.body;
    const { id } = req.user;
    const updateData = {};
    if(name) {
        updateData.name = name;
        await redis.updateUserName(id, name);
    }
    if(email) updateData.email = email;
    if(imageUrl) updateData.image_url = imageUrl;
    if(elo) {
        updateData.elo = elo;
        await redis.updateUserScore(id, elo, name);
        socketRanking.updateRanking();
    }
    if (Object.keys(updateData).length === 0) {
        return res.status(400).json({
            success: false,
            message: "Error: No data to update",
        });
    }
    const user = await prisma.user.update({
        where: {id: id},
        data: updateData
    });
    return res.status(200).json({
        success: !!user,
        message: user ? "Update successful" : "Something went wrong",
    });
});

const deleteUser = asyncHandler(async (req, res) => {
    const id = req.user.id;
    const user = await prisma.user.delete({
        where: {id: id},
    });
    if(user) {
        res.status(400);
        throw new Error("error");
    }
    return res.status(200).json({
        success: true,
        message: "Delete successful",
    })
});

const logout = asyncHandler(async (req, res) => {
    const id = req.user.id;
    const user = await prisma.user.update({
        where: {id: id},
        data: {
            refresh_token: null
        }
    });

    if(user.refresh_token) {
        res.status(400);
        throw new Error("error");
    }

    return res.status(200).json({
        success: true,
        message: "Logout successfully"
    });
});


module.exports = {
    getCurrentUser,
    updateUser,
    deleteUserImage,
    deleteUser,
    logout,
};

