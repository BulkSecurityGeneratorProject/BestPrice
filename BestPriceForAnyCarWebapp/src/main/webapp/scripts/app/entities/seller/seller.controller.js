'use strict';

angular.module('bestpriceApp')
    .controller('SellerController', function ($scope, Seller, SellerSearch, ParseLinks) {
        $scope.sellers = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Seller.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.sellers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Seller.get({id: id}, function(result) {
                $scope.seller = result;
                $('#saveSellerModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.seller.id != null) {
                Seller.update($scope.seller,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Seller.save($scope.seller,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Seller.get({id: id}, function(result) {
                $scope.seller = result;
                $('#deleteSellerConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Seller.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSellerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            SellerSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.sellers = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveSellerModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.seller = {name: null, city: null, postcode: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
