'use strict';

angular.module('bestpriceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('seller', {
                parent: 'entity',
                url: '/seller',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bestpriceApp.seller.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/seller/sellers.html',
                        controller: 'SellerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('seller');
                        return $translate.refresh();
                    }]
                }
            })
            .state('sellerDetail', {
                parent: 'entity',
                url: '/seller/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bestpriceApp.seller.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/seller/seller-detail.html',
                        controller: 'SellerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('seller');
                        return $translate.refresh();
                    }]
                }
            });
    });
