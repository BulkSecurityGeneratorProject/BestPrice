'use strict';

angular.module('bestpriceApp')
    .controller('CarDetailController', function ($scope, $stateParams, Car) {
        $scope.car = {};
        $scope.load = function (id) {
            Car.get({id: id}, function(result) {
              $scope.car = result;
            });
        };
        $scope.load($stateParams.id);
    });
