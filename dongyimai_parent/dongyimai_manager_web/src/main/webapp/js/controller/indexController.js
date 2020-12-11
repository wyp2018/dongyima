app.controller('indexController' ,function($scope,indexService){

    $scope.getName = function () {

        indexService.getName().success(function (response) {
            $scope.loginName = response.substring(1,response.length-1);
        });
    }
});
