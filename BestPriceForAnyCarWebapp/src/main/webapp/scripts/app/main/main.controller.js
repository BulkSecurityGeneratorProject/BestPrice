'use strict';

angular.module('bestpriceApp')
    .controller('MainController', function ($scope, Principal) {

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.getLabelText = function(id,index) {
            switch(id)
            {
                case "conditionInterior":
                        return ["unacceptable", "poor","average","good","perfect"][index];
                    break;
                case "conditionExterior":
                        return ["unacceptable", "poor","average","good","perfect",][index];
                    break;
                case "conditionEngine":
                        return ["unacceptable", "poor","average","good","perfect",][index];
                    break;
                case "conditionTyres":
                        return ["bald", "worn","good","new"][index];
                    break;
                default:
                        return "";
                    break;
            }

        };


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
            $scope.initConditionForm();
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

        $scope.enterValidation = function(){
            return true;
        };

        $scope.exitValidation = function(){
            return true;
        };

        //example using context object
        $scope.exitValidation = function(context){
            return context.firstName === "James";
        }


        //example using promises
        $scope.exitValidation = function(){
            var d = $q.defer()
            $timeout(function(){
                return d.resolve(true);
            }, 2000);
            return d.promise;
        }

        $scope.conditionChecks = [];
        /* vehicle configuration */


        $scope.conditionExterior= "";
        $scope.conditionInterior= "";
        $scope.conditionTyres= "";
        $scope.conditionEngine= "";


        $scope.initConditionForm = function()
        {
            // Without JQuery
            if($scope.conditionChecks.length == 0) {
                var exteriorConditionInput = new Slider('#conditionExterior', {
                    formatter: function (value) {
                        $scope.conditionExterior = $scope.getLabelText("conditionExterior", value);
                        return $scope.getLabelText("conditionExterior", value);
                    }
                });

                var interiorConditionInput = new Slider("#conditionInterior", {
                    formatter: function (value) {
                        $scope.conditionInterior = $scope.getLabelText("conditionInterior", value);
                        return  $scope.getLabelText("conditionInterior", value);
                    }
                });

                var tyresConditionInput = new Slider("#conditionTyres", {
                    formatter: function (value) {
                        $scope.conditionTyres = $scope.getLabelText("conditionTyres", value);
                        return $scope.getLabelText("conditionTyres", value);
                    }
                });

                var engineConditionInput = new Slider("#conditionEngine", {
                    formatter: function (value) {
                        $scope.conditionEngine = $scope.getLabelText("conditionEngine", value);
                        return $scope.getLabelText("conditionEngine", value);
                    }
                });

                $scope.conditionChecks = [exteriorConditionInput, interiorConditionInput, tyresConditionInput, engineConditionInput];
            }
        }
        $scope.startSelling = function()
        {
            //TODO: NEED END POINT TO SAVE SALE RECORD


        }



    });
