var initController = function($scope, $http, $filter){
	console.log("initController");

	$scope.formatDateyyyyMMdd = function(d){
		return $filter('date')( d, "yyyy-MM-dd")
	}

	$scope.param = parameters;
	console.log($scope.param);
	if(!$scope.param.date){
		$scope.param.date = $scope.formatDateyyyyMMdd(new Date());
		console.log($scope.param.date);
		console.log("------------------------------------");
	}
	$scope.toDoDate = new Date($scope.param.date);
	console.log($scope.toDoDate);
	$scope.readMoveTodayPatients = function(){
		var url = "/rs/movePatient/read-"+$scope.param.date;
		console.log(url);
		$http({ method : 'GET', url : url
		}).success(function(data, status, headers, config) {
			$scope.movePatientsInDay = data;
			console.log($scope.movePatientsInDay);
		}).error(function(data, status, headers, config) {
			$scope.error = data;
		});
	}
}

hol2eih3App.controller('PatientMovementCtrl', ['$scope', '$http', '$filter', '$sce'
		, function ($scope, $http, $filter, $sce) {
	initController($scope, $http, $filter);
	$scope.readMoveTodayPatients();

	$scope.monthDayDate = function(month, day){
		var d2 = new Date(2016, month - 1, day);
		return d2;
	}
	$scope.passedDays = function(m,d){
		return new Date() > $scope.monthDayDate(m,d);
	}
	
	
	$scope.monthDay = function(m,d){
		var dt = new Date(new Date().getFullYear(), m, 0).getDate();
		if(d>dt){
			return;
		}
		return d;
	}
	
	$scope.isParamDate = function(month, day){
		if($scope.toDoDate.getDate() == day){
			if(month == $scope.toDoDate.getMonth() + 1){
				return true;
			}
		}
		return false;
	}
	
	$scope.satSanDay = function(m,d){
		return $scope.monthDayDate(m,d).getDay() == 6 || $scope.monthDayDate(m,d).getDay() == 0;
	}
	
}]);

hol2eih3App.controller('EditMovePatientInDayCtrl', ['$scope', '$http', '$filter', '$sce'
		, function ($scope, $http, $filter, $sce) {
	initController($scope, $http, $filter);
	$scope.readMoveTodayPatients();

	var oldDepartmentHol = {};
	var oldDepartmentHolId;
	var oldDepartmentHolValue;
	
	$scope.focus = function(departmentHol, field){
		if(oldDepartmentHol.field){
			if(oldDepartmentHol.id != departmentHol.MOVEDEPARTMENTPATIENT_ID || oldDepartmentHol.field != field){
				console.log(oldDepartmentHol);
				var newValue = oldDepartmentHol.obj["MOVEDEPARTMENTPATIENT_"+oldDepartmentHol.field];
				if(newValue != oldDepartmentHol.value){
					console.log(newValue+"/"+oldDepartmentHol.value);
					var url = "/rs/movePatient/updateMoveDepartmentPatien-"+oldDepartmentHol.id+"-MOVEDEPARTMENTPATIENT_"+oldDepartmentHol.field+"-"+newValue;
					console.log(url);
					$http({ method : 'PUT', url : url
					}).success(function(data, status, headers, config) {
						console.log("success");
						console.log(data);
					}).error(function(data, status, headers, config) {
						console.log("error");
						$scope.error = data;
					});
				}
			}
		}
		//set new focus
		oldDepartmentHol.id = departmentHol.MOVEDEPARTMENTPATIENT_ID;
		oldDepartmentHol.field = field;
		oldDepartmentHol.value = departmentHol["MOVEDEPARTMENTPATIENT_"+field];
		oldDepartmentHol.obj = departmentHol;
	}
	
	$scope.change = function(departmentHol){
		calc(departmentHol);
	}
	calc = function(departmentHol){
		var sum = 0;
		if(departmentHol.MOVEDEPARTMENTPATIENT_PATIENT1DAY)
			sum = departmentHol.MOVEDEPARTMENTPATIENT_PATIENT1DAY/1;
		if(departmentHol.MOVEDEPARTMENTPATIENT_IN)
			sum += departmentHol.MOVEDEPARTMENTPATIENT_IN/1;
		if(departmentHol.MOVEDEPARTMENTPATIENT_INDEPARTMENT)
			sum += departmentHol.MOVEDEPARTMENTPATIENT_INDEPARTMENT/1;
		if(departmentHol.MOVEDEPARTMENTPATIENT_OUTDEPARTMENT)
			sum -= departmentHol.MOVEDEPARTMENTPATIENT_OUTDEPARTMENT/1;
		if(departmentHol.MOVEDEPARTMENTPATIENT_OUT)
			sum -= departmentHol.MOVEDEPARTMENTPATIENT_OUT/1;
		if(departmentHol.MOVEDEPARTMENTPATIENT_DEAD)
			sum -= departmentHol.MOVEDEPARTMENTPATIENT_DEAD/1;
		departmentHol.MOVEDEPARTMENTPATIENT_PATIENT2DAY = sum;
	}

}]);

