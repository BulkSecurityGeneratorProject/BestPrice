'use strict';

angular.module('bestpriceApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


