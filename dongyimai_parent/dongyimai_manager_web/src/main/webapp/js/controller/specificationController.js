//specification控制层 
app.controller('specificationController' ,function($scope, $controller, specificationService){

	$controller('baseController',{$scope:$scope});//继承

	$scope.save=function(){
			specificationService.save($scope.entity).success(function (response) {

				if(response.success){
					//重新查询
					$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			})

	}

	$scope.reloadList = function(){
		$scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
	}



	//查询实体 
	$scope.findOne = function(id){				
		specificationService.findOne(id).success(
			function(response){

				console.log(id);
				$scope.entity= response;					
			}
		);				
	}

	//批量删除 
	$scope.dele = function(){			
		//获取选中的复选框			
		specificationService.dele($scope.selectIds).success(
			function(response){
				if(response.success){
					$scope.reloadList();
					$scope.selectIds=[];
				}						
			}		
		);				
	}

	$scope.entity={specificationOptionList:[]};
//新增规格选项行
	$scope.addRow=function(){
		$scope.entity.specificationOptionList.push({});
	}

	$scope.deleRow=function(index){
		$scope.entity.specificationOptionList.splice(index,1);//删除
	}


	// 定义搜索对象 
	$scope.searchEntity = {};
	// 搜索
	$scope.search = function(page,size){			
		specificationService.search(page,size,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;
			}			
		);
	}


});	
