var Discover = {
    search: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Discover", "search", []);
    }
};

module.exports = Discover;
