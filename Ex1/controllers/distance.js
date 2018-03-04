const computeDistanceByLatLngInKm = require('../utils/compute_distance.js');

const getDistanceByLatLng = (req, res) => {
  const origLatlng = req.query.orig.split(',').map(Number);
  const destLatlng = req.query.dest.split(',').map(Number);

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
};

module.exports = {
  getDistanceByLatLng,
};
