const request = require('../utils/request');

const geocode = {
  API_KEY: 'AIzaSyAajfxHEKdFcokeT7fHlkA4MkqtWf7bE68',
  API_URL: 'https://maps.googleapis.com/maps/api/geocode/json?',
};

const getAddressByLatLng = (req, res) => {
  console.log(`Request: ${req.url}`);
  try {
    if (!req.query || (req.query && !req.query.latlng)) {
      throw new Error('Invalid latlng input.');
    }

    const latlng = req.query.latlng.split(',').map(Number);

    if (latlng.length !== 2 || Number.isNaN(latlng[0]) || Number.isNaN(latlng[1])) {
      throw new Error('Invalid latlng input.');
    }

    const url = `${geocode.API_URL}latlng=${latlng[0]},${latlng[1]}&key=${geocode.API_KEY}`;

    request
      .get(url)
      .then((gecodeRes) => {
        const { data } = gecodeRes;

        let result = {};

        if (data && data.results && data.results.length > 0) {
          result = {
            address: data.results[0].formatted_address,
            lat: data.results[0].geometry.location.lat,
            lng: data.results[0].geometry.location.lng,
          };
          return res.json({
            status: 'OK',
            result,
          });
        }

        switch (data.status) {
          case 'ZERO_RESULTS':
            throw new Error('Can not find any result.');
          case 'OVER_QUERY_LIMIT':
            throw new Error('You are over your quota.');
          case 'REQUEST_DENIED':
            throw new Error('Your request was denied.');
          default:
            throw new Error('The request could not be processed due to a server error. The request may succeed if you try again.');
        }
      })
      .catch((err) => {
        console.log(err.message);
        return res.json({
          status: 'FAIL',
          error_message:
            err.response.status === 400
              ? 'Can not find any result.'
              : (err.message && err.message) ||
                'The request could not be processed due to a server error. The request may succeed if you try again.',
        });
      });
  } catch (err) {
    return res.json({
      status: 'FAIL',
      error_message: err.message,
    });
  }
};

module.exports = {
  getAddressByLatLng,
};
