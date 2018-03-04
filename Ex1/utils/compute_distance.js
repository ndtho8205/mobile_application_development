const deg2rad = deg => deg * (Math.PI / 180);

const computeDistanceByLatLngInKm = (orig, dest) => {
  const R = 6371.0; // Radius of the earth in km
  const dLat = deg2rad(dest.lat - orig.lat);
  const dLng = deg2rad(dest.lng - orig.lng);
  const a =
    (Math.sin(dLat / 2) * Math.sin(dLat / 2))
    + (Math.cos(deg2rad(orig.lat)) * Math.cos(deg2rad(dest.lat))
      * Math.sin(dLng / 2) * Math.sin(dLng / 2));
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  const d = R * c; // Distance in km
  return d;
};

module.exports = computeDistanceByLatLngInKm;
