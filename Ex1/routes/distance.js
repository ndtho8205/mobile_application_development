const express = require('express');

const router = express.Router();

const distanceControllers = require('../controllers/distance');

router.get('/', distanceControllers.getDistanceByLatLng);

module.exports = router;
