'use strict';

angular.module('bestpriceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('buyer', {
                parent: 'entity',
                url: '/buyer',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bestpriceApp.buyer.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/buyer/buyers.html',
                        controller: 'BuyerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('buyer');
                        return $translate.refresh();
                    }]
                }
            })
            .state('buyerDetail', {
                parent: 'entity',
                url: '/buyer/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bestpriceApp.buyer.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/buyer/buyer-detail.html',
                        controller: 'BuyerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('buyer');
                        return $translate.refresh();
                    }]
                }
            });
    });
