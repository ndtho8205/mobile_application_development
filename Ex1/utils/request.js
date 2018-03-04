const axios = require('axios');

const request = axios.create({
  timeout: 100000,
});

module.exports = request;
