var exec = require('cordova/exec');

var Discover = window.Discover = {
    search: function(version, port, successCallback, errorCallback) {
        exec(successCallback, errorCallback, "Discover", "search", [version, port]);
    }
};

if (typeof module != 'undefined' && module.exports) {
  module.exports = window.Discover;
}
