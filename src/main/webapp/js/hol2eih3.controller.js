var initController = function($scope, $http, $filter){
	console.log("initController");
	console.log(parameters);
	$scope.readMoveTodayPatients = function(){
		var url = "/rs/movePatient/read-2016-2-2";
		console.log(url);
		$http({ method : 'GET', url : url
		}).success(function(data, status, headers, config) {
			$scope.moveTodayPatients = data;
			console.log($scope.moveTodayPatients);
		}).error(function(data, status, headers, config) {
			$scope.error = data;
		});
	}
}

hol2eih3App.controller('EditMovePatientInDayCtrl', ['$scope', '$http', '$filter', '$sce'
		, function ($scope, $http, $filter, $sce) {
	initController($scope, $http, $filter);
	$scope.readMoveTodayPatients();
}])
