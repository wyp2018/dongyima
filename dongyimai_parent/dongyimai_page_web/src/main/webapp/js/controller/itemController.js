//item控制层
app.controller('itemController' ,function($scope,$http){

	$scope.num=1;
	//改变数量
	$scope.changeNum = function(x){

		$scope.num += x;
		if($scope.num<1){
			$scope.num=1;

		}

	}

	//记录用户选择的规格
	$scope.specificationItems={};

	//选择不同的规格
	$scope.selectSpec = function(specName,optionName){
		$scope.specificationItems[specName] = optionName;

		//选择的规格改版后,通过记录的最新规格找到他
		for(var i=0;i<skuList.length;i++){
		var result = equalObj(skuList[i].spec,$scope.specificationItems);
		if(result){
			$scope.sku = skuList[i];
				break;
			}
		}

	}
	
	equalObj = function(obj1,obj2){
		for(var k in obj1){
			if(obj1[k]!=obj2[k]){
				return false;
			}
		}
		return true;
	
	}



	//判断某规格是否选中
	$scope.isSelected = function(specName,optionName){

		if($scope.specificationItems[specName] == optionName){
			return true;
		}else{
			return false;
		}
	}

	//进入页面加载默认的sku
	$scope.loadSku = function(){
		//sku包含了id title price spec
		$scope.sku = skuList[0];

		//为避免两个对象互相引用的是同一个地址
		//先将一个对象 转化为字符串 再将字符串转化为对象
		$scope.specificationItems = JSON.parse(JSON.stringify($scope.sku.spec));

	}

	//预留的购物车方法
	$scope.addToCart = function(){
	
		// alert("商品id为:" + $scope.sku.id + ",购买的数量为:" + $scope.num);

		//跨系统调用
		$http.get("http://localhost:9014/cart/addToCart.do?skuId=" + $scope.sku.id +"&num=" + $scope.num ,{'withCredentials':true}).success(function (response) {

			if(response.success){
				location.href = "http://localhost:9014/cart.html";
			}else {
				alert(response.message);
			}
		});

	}




});
