const express = require('express');

const router = express.Router();

/* GET homepage */
router.get('/', (req, res) => {
  res.send('index');
});

module.exports = router;
