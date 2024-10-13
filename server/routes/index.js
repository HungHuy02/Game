const userRouter = require('./users.js');
const authUserRouter = require('./authUser.js');
const rankRouter = require('./rank.js');
const { authenticateToken } = require('../middlewares/authenticateToken.js');

function route(app) {
  app.use('/auth/user', authUserRouter);
  app.use('/user', authenticateToken, userRouter);
  app.use('/rank', authenticateToken, rankRouter);
}

module.exports = route;
