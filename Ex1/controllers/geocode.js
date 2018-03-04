const request = require('../utils/request');

const geocode = {
  API_KEY: 'AIzaSyAajfxHEKdFcokeT7fHlkA4MkqtWf7bE68',
  API_URL: 'https://maps.googleapis.com/maps/api/geocode/json?',
};

const getAddressByLatLng = (req, res) => {
  const { lat, lng } = req.query;
  const url = `${geocode.API_URL}latlng=${lat},${lng}&key=${geocode.API_KEY}`;

  request.get(url)
    .then((gecodeRes) => {
      const { data } = gecodeRes;

      let result = {};

      if (data && data.results && data.results.length > 0) {
        result = {
          address: data.results[0].formatted_address,
          lat: data.results[0].geometry.location.lat,
          lng: data.results[0].geometry.location.lng,
        };
      }

      res.json({
        result,
        status: data.status,
      });
    }).catch((err) => {
      console.log(err);
    });
};

module.exports = {
  getAddressByLatLng,
};
