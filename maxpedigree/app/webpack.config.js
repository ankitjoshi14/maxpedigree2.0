// Import webpack moduke
var webpack = require("webpack");
// Import open browser plugin
var OpenBrowserPlugin = require('open-browser-webpack-plugin');
// Import minicss
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const UglifyJsPlugin = require("uglifyjs-webpack-plugin");
const OptimizeCSSAssetsPlugin = require("optimize-css-assets-webpack-plugin");
const HtmlWebPackPlugin = require("html-webpack-plugin");
//Import path module
const path = require('path');

var devMode = true;

module.exports = env => {
    // Use env.<YOUR VARIABLE> here:
  console.log('NODE_ENV: ', env.NODE_ENV); // 'local'
  console.log('Production: ', env.production); // true
  console.log("directroy:" , __dirname);
  devMode = !env.production;
    return {
    entry: "./src/index.js", //set entry file
    // Resolve to output directory and set file
    
    output: {
        path: path.resolve(__dirname, '../src/main/webapp'),
    },
    optimization: {
        minimizer: [
          new UglifyJsPlugin({
            cache: true,
            parallel: true,
            sourceMap: true // set to true if you want JS source maps
          }),
          new OptimizeCSSAssetsPlugin({})
        ]
      },
    // Add Url param to open browser plugin
    plugins: [new OpenBrowserPlugin({url: 'http://localhost:3000'}),
    new MiniCssExtractPlugin({
        // Options similar to the same options in webpackOptions.output
        // both options are optional
        filename: devMode ? '[name].css' : '[name].[hash].css',
        chunkFilename: devMode ? '[id].css' : '[id].[hash].css',
      }),
      new HtmlWebPackPlugin({
        template: "./src/index.html",
        filename: "./index.html"
      })

],

    // Set dev-server configuration
    devServer: {
        inline: true,
        contentBase: './dist',
        port: 3000
    },

    // Add babel-loader to transpile js and jsx files
    module: {
        rules: [
            {
                test: /\.js?$/,
                exclude: /(node_modules)/,
                use: [
                    {
                        loader: 'babel-loader',
                        query: {
                            presets: ["react"]
                        }
                    }
                ]
            },
            {
                test: /\.html$/,
                use: [
                  {
                    loader: "html-loader",
                    options: { minimize: true }
                  }
                ]
              },
            {
                test: /\.css$/,
                exclude: /node_modules/,
                use: [
                    {
                      loader: devMode ? 'style-loader' : MiniCssExtractPlugin.loader
                    },
                    {
                      loader: "css-loader",
                      options: {
                        modules: true,
                        importLoaders: 1,
                        localIdentName: "[name]_[local]_[hash:base64]",
                        sourceMap: true,
                        minimize: true
                      }
                    }
                  ]
            },
            {
              test: /\.css$/,
              include: /node_modules/,
              use: ['style-loader', 'css-loader']
            }
        ]
    }
}};