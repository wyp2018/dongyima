 //控制层 
app.controller('contentController' ,function($scope,$controller,contentService){


	//根据广告分类id获取广告列表
	$scope.findMoveContent = function (catId) {

		contentService.findMoveContent(catId).success(function (response) {

			$scope.moveContent = response;
		})
	}

	$scope.jumpToSearch = function () {

		location.href = "http://localhost:9007/search.html#?kw=" + $scope.kw;
	}

});	