'use strict';

angular.module('bestpriceApp')
    .controller('SellerDetailController', function ($scope, $stateParams, Seller) {
        $scope.seller = {};
        $scope.load = function (id) {
            Seller.get({id: id}, function(result) {
              $scope.seller = result;
            });
        };
        $scope.load($stateParams.id);
    });
