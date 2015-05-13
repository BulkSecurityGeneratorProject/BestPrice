'use strict';

angular.module('bestpriceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('car', {
                parent: 'entity',
                url: '/car',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bestpriceApp.car.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/car/cars.html',
                        controller: 'CarController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('car');
                        return $translate.refresh();
                    }]
                }
            })
            .state('carDetail', {
                parent: 'entity',
                url: '/car/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bestpriceApp.car.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/car/car-detail.html',
                        controller: 'CarDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('car');
                        return $translate.refresh();
                    }]
                }
            });
    });
