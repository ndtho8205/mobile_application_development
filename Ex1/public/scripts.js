const { Vue, axios } = window;

const API_URL = `${window.location.protocol}//${window.location.host}/api`;

const request = axios.create({
  timeout: 100000,
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  data: {
    part1: {
      lat: null,
      lng: null,
      result: {},
      progress: 0,
    },
    part2: {
      orig: {
        lat: null,
        lng: null,
      },
      dest: {
        lat: null,
        lng: null,
      },
      result: {},
      progress: 0,
    },
  },
  methods: {
    getAddressByLatLng() {
      this.part1.progress = 25;
      console.log('Getting...');
      request
        .get(`${API_URL}/geocode?latlng=${this.part1.lat},${this.part1.lng}`)
        .then((res) => {
          this.part1.progress = 75;
          if (res.data.status !== 'OK') throw new Error(res.data.error_message);
          this.part1.result = res.data.result;
        })
        .catch((err) => {
          console.log(err);
          this.part1.result = {};
          alert(err.message);
        })
        .then(() => {
          this.part1.progress = 0;
        });
    },

    getDistanceByLatLng() {
      this.part2.progress = 25;
      console.log('Getting...');
      request
        .get(`${API_URL}/distance?orig=${this.part2.orig.lat},${this.part2.orig.lng}&dest=${
          this.part2.dest.lat
        },${this.part2.dest.lng}`)
        .then((res) => {
          this.part2.progress = 75;
          if (res.data.status !== 'OK') throw new Error(res.data.error_message);
          this.part2.result = res.data.result;
        })
        .catch((err) => {
          console.log(err);
          this.part2.result = {};
          alert(err.message);
        })
        .then(() => {
          this.part2.progress = 0;
        });
    },
  },
});
