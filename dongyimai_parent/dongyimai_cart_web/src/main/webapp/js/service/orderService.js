//order服务层
app.service('orderService', function($http){
	// 保存
	this.submitOrder = function(order) {
		return $http.post('../order/add.do', order);
	}

});