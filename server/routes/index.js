const userRouter = require('./users.js');
const authUserRouter = require('./authUser.js');

function route(app) {
  app.use("/auth/user", authUserRouter);
}

module.exports = route;
