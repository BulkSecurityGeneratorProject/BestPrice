'use strict';

angular.module('bestpriceApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
