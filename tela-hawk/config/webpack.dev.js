var webpack = require('webpack');
var webpackMerge = require('webpack-merge');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var commonConfig = require('./webpack.common.js');
var helpers = require('./helpers');

require('dotenv').config({silent: true});
const ENV = process.env.NODE_ENV = process.env.ENV = 'dev';
const TELA_SERVER = process.env.TELA_SERVER;
const INSTAGRAM_REDIRECT_HOST = process.env.INSTAGRAM_REDIRECT_HOST;
const INSTAGRAM_CLIENT_ID = process.env.INSTAGRAM_CLIENT_ID;

module.exports = webpackMerge(commonConfig, {
    devtool: 'cheap-module-eval-source-map',

    output: {
        path: helpers.root('dist'),
        publicPath: 'http://localhost:3000/',
        filename: '[name].js',
        chunkFilename: '[id].chunk.js'
    },

    plugins: [
        new ExtractTextPlugin('[name].css'),
        new webpack.DefinePlugin({
            'process.env': {
                'ENV': JSON.stringify(ENV),
                'TELA_SERVER': JSON.stringify(TELA_SERVER),
                'INSTAGRAM_REDIRECT_HOST': JSON.stringify(INSTAGRAM_REDIRECT_HOST),
                'INSTAGRAM_CLIENT_ID': JSON.stringify(INSTAGRAM_CLIENT_ID)
            }
        })
    ],

    devServer: {
        historyApiFallback: true,
        stats: 'minimal'
    }
});