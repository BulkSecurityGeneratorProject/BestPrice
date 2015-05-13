'use strict';

angular.module('bestpriceApp')
    .controller('BuyerDetailController', function ($scope, $stateParams, Buyer) {
        $scope.buyer = {};
        $scope.load = function (id) {
            Buyer.get({id: id}, function(result) {
              $scope.buyer = result;
            });
        };
        $scope.load($stateParams.id);
    });
