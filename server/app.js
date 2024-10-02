var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var cors = require('cors');
var dotenv = require("dotenv");

var app = express();
dotenv.config();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(cors("*"));
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

const route = require('./routes/index');
route(app);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
const { errorsMiddleware }= require('./middlewares/errorsMiddleware');
app.use(errorsMiddleware);

const PORT = process.env.PORT || 3000;
const http = require('http');
const server = http
    .createServer(app)
    .listen(PORT, () => console.log(`listening on port ${PORT}`));

const socketIO = require('socket.io');
const io = socketIO(server, {
  cors: {
    origin: "*",
  },
});

const chessGame = require('./controllers/socket/chessGame');
chessGame(io);

module.exports = app;
