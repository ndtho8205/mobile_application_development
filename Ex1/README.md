# Exercise 1

On Heroku: https://ex1-mobile-app.herokuapp.com/

On localhost: `npm && npm run start`

APIs:

```bash
# find address by latitude and longitude
LAT=10
LNG=100
curl 'https://ex1-mobile-app.herokuapp.com/api/geocode?latlng=${LAT},${LNG}' | cat

# find distance between two location
curl 'https://ex1-mobile-app.herokuapp.com/api/distance?orig=10,100&dest=11,100' | cat
```
