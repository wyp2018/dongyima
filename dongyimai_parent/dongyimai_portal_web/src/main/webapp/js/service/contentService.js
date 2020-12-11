//服务层
app.service('contentService',function($http){

	this.findMoveContent = function (catId) {

		return $http.get("./content/findMoveContent.do?catId=" + catId);
	}

});