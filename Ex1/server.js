const express = require('express');
const cors = require('cors');

const app = express();
const port = process.env.PORT || 3000;

app.use(cors());

app.use(express.static('public'));

app.use('/', require('./routes/index'));
app.use('/api', require('./routes/api'));

app.use('/api/geocode', require('./routes/geocode'));
app.use('/api/distance', require('./routes/distance'));

app.listen(port, () => {
  console.log(`Express server listening on port ${port}`);
});
