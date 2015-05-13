'use strict';

angular.module('bestpriceApp')
    .factory('CarSearch', function ($resource) {
        return $resource('api/_search/cars/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
