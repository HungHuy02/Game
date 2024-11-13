var firebase = require("firebase-admin");

var serviceAccount = require("../serviceAccountKey.json");

firebase.initializeApp({
  credential: firebase.credential.cert(serviceAccount)
});

module.exports = firebase;