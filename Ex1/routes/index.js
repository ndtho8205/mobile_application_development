const express = require('express');
const path = require('path');

const router = express.Router();

/* GET homepage */
router.get('/', (req, res) => {
  res.sendFile(path.join(`${__dirname}/../public/index.html`));
});

module.exports = router;