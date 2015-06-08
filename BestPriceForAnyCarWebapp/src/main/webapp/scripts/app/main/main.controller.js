'use strict';

angular.module('bestpriceApp')
    .controller('MainController', function ($scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;


        });
        $scope.regValid = false;
        $scope.vrnResultAvailable = false;
        $scope.searching = false;
        $scope.vrnRaw = "";
        $scope.vrn = function(){
            return $scope.vrnRaw.replace(" ","");
        };
        $scope.search = function() {
            // do search here
            $scope.vrnResultAvailable =  true;
            $scope.searching = false;
            $scope.$apply();
        };
        $scope.lastTimer = "";

        $scope.vrnChange = function() {
            var matches = $scope.vrn().match(/^[A-Z]{2}[0-9]{2}[A-Z]{3}$|^[A-Z][0-9]{1,3}[A-Z]{3}$|^[A-Z]{3}[0-9]{1,3}[A-Z]$|^[0-9]{1,4}[A-Z]{1,2}$|^[0-9]{1,3}[A-Z]{1,3}$|^[A-Z]{1,2}[0-9]{1,4}$|^[A-Z]{1,3}[0-9]{1,3}$/i);

            if (matches && matches.length > 0) {
                if($scope.lastTimer != "")
                {
                    clearTimeout($scope.lastTimer);
                    $scope.lastTimer = "";
                }
                $scope.searching = true;
                $scope.regValid = true;
                $scope.lastTimer = setTimeout( $scope.search, 1300 );
            }
            else
            {
                if($scope.lastTimer != "") {

                    clearTimeout($scope.lastTimer);
                }
                $scope.searching = false;
                $scope.vrnResultAvailable =false;
                $scope.regValid = false;
            }


        };

    });
