var discover = {
	search: function(str, callback) {
		cordova.exec(callback, function(err) {
			callback('Nothing to echo.');
		}, "Discover", "search", [str]);
	};
};

module.exports = discover;
