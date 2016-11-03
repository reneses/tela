var express = require('express');
var path = require('path');
var app = express();

var publicPath = path.resolve(__dirname, 'dist');
app.use(express.static(publicPath));
app.all('*', function (req, res) {
    res.status(200).sendFile(path.join(publicPath, 'index.html'));
});

var port = process.env.PORT || 3000;
app.listen(port, function () {
    console.log('Server running on port ' + port);
});