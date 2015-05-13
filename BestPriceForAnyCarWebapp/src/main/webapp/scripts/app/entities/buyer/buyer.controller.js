'use strict';

angular.module('bestpriceApp')
    .controller('BuyerController', function ($scope, Buyer, BuyerSearch, ParseLinks) {
        $scope.buyers = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Buyer.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.buyers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Buyer.get({id: id}, function(result) {
                $scope.buyer = result;
                $('#saveBuyerModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.buyer.id != null) {
                Buyer.update($scope.buyer,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Buyer.save($scope.buyer,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Buyer.get({id: id}, function(result) {
                $scope.buyer = result;
                $('#deleteBuyerConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Buyer.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBuyerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            BuyerSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.buyers = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveBuyerModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.buyer = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
