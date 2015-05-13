'use strict';

angular.module('bestpriceApp')
    .factory('SellerSearch', function ($resource) {
        return $resource('api/_search/sellers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
