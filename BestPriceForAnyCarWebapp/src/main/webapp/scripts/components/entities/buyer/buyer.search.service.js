'use strict';

angular.module('bestpriceApp')
    .factory('BuyerSearch', function ($resource) {
        return $resource('api/_search/buyers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
