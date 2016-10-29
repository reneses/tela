var expect = require('chai').expect;
var fs = require('fs');

var TEST_FILE = '.tela-test';
var connection = require('../connection')(TEST_FILE);

describe('Connection handler', function () {

    afterEach(function () {
        fs.existsSync(TEST_FILE) && fs.unlinkSync(TEST_FILE);
    });

    describe('Save a connection', function () {
        it('Creates the connection file', function () {
            connection.save("localhost", 80, "1");
            expect(fs.existsSync(TEST_FILE)).to.be.true;
        });
        it('Saves the file properly', function () {
            connection.save("localhost", 80, "1");
            var stored = JSON.parse(fs.readFileSync(TEST_FILE));
            expect(stored).to.deep.equal({
                host: "localhost",
                port: 80,
                session: "1"
            });
        });
        it('Overwrites the values', function () {
            connection.save("localhost", 80, "1");
            connection.save("127.0.0.12", 8080, "2");
            var stored = JSON.parse(fs.readFileSync(TEST_FILE));
            expect(stored).to.deep.equal({
                host: "127.0.0.12",
                port: 8080,
                session: "2"
            });
        });

    });

    describe('Destroy a connection', function () {
        it('Does nothing if not exists', function () {
            connection.destroy();
        });
        it('Deletes the file', function () {
            connection.save("localhost", 80, "1");
            connection.destroy();
            expect(fs.existsSync(TEST_FILE)).to.be.false;
        });
    });

    describe('Loads the existing connection', function () {
        it('Supports a call without being connected', function () {
            var c = connection.load();
            expect(c).to.deep.equal({
                isConnected: false
            });
        });
        it('Loads the proper values', function () {
            connection.save("127.0.0.1", 24, "0");
            var c = connection.load();
            expect(c).to.deep.equal({
                host: "127.0.0.1",
                port: 24,
                session: "0",
                isConnected: true
            });
        });
    });
    
    describe('Connection caching', function () {
        it('Caches the connection', function () {
            connection.save("127.0.0.1", 24, "0");
            fs.unlinkSync(TEST_FILE);
            var c = connection.load();
            expect(c).to.deep.equal({
                host: "127.0.0.1",
                port: 24,
                session: "0",
                isConnected: true
            });
        });
    });

});