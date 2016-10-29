var expect = require('chai').expect;
var Tela = require('../tela');
var request = require('request');

var TELA_HOST = "localhost", TELA_PORT = "80";

describe('Tela API', function () {

    var tela = Tela(TELA_HOST, TELA_PORT);

    before(function(done) {
        request.get('http://' + TELA_HOST + ':' + TELA_PORT, function (err, res, body) {
            if (err)
                done(err);
            else
                done();
        });
    });

    it('The Tela testing server is running', function () {
        // request.get('http://' + TELA_HOST + ':' + TELA_PORT, function (err, res, body) {
        //     expect(err).to.be.false;
        //     expect(res.statusCode).to.equal(200);
        //     expect(res.body).to.equal('OKl');
        //     done();
        // }); TODO
    });

});