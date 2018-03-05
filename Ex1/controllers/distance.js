const computeDistanceByLatLngInKm = require('../utils/compute_distance.js');

const isValidLocation = location =>
  !(location.length !== 2 || Number.isNaN(location[0]) || Number.isNaN(location[1]));

const getDistanceByLatLng = (req, res) => {
  try {
    if (!req.query || (req.query && !req.query.orig && !req.query.dest)) {
      throw new Error('Invalid orig/dest input.');
    }
    const origLatlng = req.query.orig.split(',').map(Number);
    const destLatlng = req.query.dest.split(',').map(Number);
    if (!isValidLocation(origLatlng) || !isValidLocation(destLatlng)) {
      throw new Error('Invalid orig/dest input.');
    }

    const orig = { lat: origLatlng[0], lng: origLatlng[1] };
    const dest = { lat: destLatlng[0], lng: destLatlng[1] };
    const result = {
      distance: computeDistanceByLatLngInKm(orig, dest),
      origin: orig,
      destination: dest,
    };

    res.json({
      result,
      status: 'OK',
    });
  } catch (err) {
    return res.json({
      status: 'FAIL',
      error_message: err.message,
    });
  }
};

module.exports = {
  getDistanceByLatLng,
};
