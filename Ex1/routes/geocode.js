const express = require('express');

const router = express.Router();

const gecodeControllers = require('../controllers/geocode');

router.get('/', gecodeControllers.getAddressByLatLng);

module.exports = router;
