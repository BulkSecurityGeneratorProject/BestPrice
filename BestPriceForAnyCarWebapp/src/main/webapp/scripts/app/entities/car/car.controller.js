'use strict';

angular.module('bestpriceApp')
    .controller('CarController', function ($scope, Car, CarSearch, ParseLinks) {
        $scope.cars = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Car.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.cars.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.cars = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Car.get({id: id}, function(result) {
                $scope.car = result;
                $('#saveCarModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.car.id != null) {
                Car.update($scope.car,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Car.save($scope.car,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Car.get({id: id}, function(result) {
                $scope.car = result;
                $('#deleteCarConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Car.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteCarConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            CarSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.cars = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveCarModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.car = {vrn: null, make: null, model: null, colour: null, miles: null, age: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
