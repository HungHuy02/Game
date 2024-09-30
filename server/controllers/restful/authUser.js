const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

const checkExistingPeople = asyncHandler(async (req, res) => {
    try {
        const { email } = req.query;
        const check = await prisma.user.check.findUnique({
            where: {email: {email}}
        });
        return res.status(200).json({
            data: check ? true : false,
        });
    }catch (error) {
        res.status(400);
        throw new Error(error);
    }
})

module.exports = {
    checkExistingUser,
  };