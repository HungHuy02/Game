const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();
const asyncHandler = require("express-async-handler");

const updateUser = asyncHandler(async (req, res) => {
    const { id ,name, email} = req.body;
    if(!id) {
        return res.status(400).json({
            success: false,
            message: "Error: ID is required",
        });
    }
    const updateData = {};
    if(name) updateData.name = name;
    if(email) updateData.email = email;

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
        success: user ? true: false,
        message: user ? "Update successful" : "Something went wrong",
    });
});




module.exports = {
    updateUser,
};

