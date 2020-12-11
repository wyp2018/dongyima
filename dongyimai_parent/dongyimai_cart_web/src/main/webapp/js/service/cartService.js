//服务层
app.service('cartService', function ($http) {

    this.findCartList = function () {

        return $http.get("./cart/findCartList.do");
    }
    this.changeNum = function (skuId,num) {

    	console.log(skuId);
		return $http.get("./cart/addToCart.do?skuId=" + skuId + "&num=" + num);

	}


	this.sum = function (cartList) {

			totalValue = {totalNum:0,totalMoney:0.00};

    	for(var i = 0;i<cartList.length;i++){
    		var cart = cartList[i];
    		for(var j = 0;j<cart.orderItemList.length;j++){
    			var orderItem = cart.orderItemList[j];
    			totalValue.totalNum += orderItem.num;
    			totalValue.totalMoney += orderItem.totalFee;
			}
		}
    	return totalValue;
	}


});