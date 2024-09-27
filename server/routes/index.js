const userRouter = require('./users')

function route(app) {
  app.use("/user", userRouter);
}

module.exports = route;
