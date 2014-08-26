var exec = require('cordova/exec');

var Discover = window.Discover = {
    search: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, "Discover", "search", []);
    }
};

if (typeof module != 'undefined' && module.exports) {
  module.exports = window.Discover;
}
