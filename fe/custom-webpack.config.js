const webpack = require("webpack");
const PRODUCTION = process.env.NODE_ENV === "production";
module.exports = async (config) => {
  config.plugins.push(
    new webpack.DefinePlugin({
      //process: "process/browser",
      "process.env": JSON.stringify(process.env),
      "process.env.NODE_ENV": JSON.stringify(PRODUCTION),
    })
  );

  return config;
};
