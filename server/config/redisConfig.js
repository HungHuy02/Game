const { createClient } = require('redis');

const client = createClient({
    password: process.env.REDIS_PASSWORD,
    socket: {
        host: process.env.REDIS_HOST,
        port: process.env.REDIS_PORT
    }
});

client.on('connect', function () {
    console.log('connected redis success!!!');
});
  
client.on('error', err => console.log('Redis Client Error', err)); 

client.connect();

module.exports = client;