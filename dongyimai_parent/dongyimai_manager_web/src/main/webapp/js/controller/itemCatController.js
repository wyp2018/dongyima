//item_cat控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService,typeTemplateService){
	
	// 继承
	$controller("baseController", {
		$scope : $scope
	});
	
	// 保存
	//保存
	$scope.save=function(){
		var serviceObject;//服务层对象
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改
		}else{
			$scope.entity.parentId=$scope.parentId;//赋予上级ID
			serviceObject=itemCatService.add( $scope.entity  );//增加
		}
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询
					$scope.findByParentId($scope.parentId);//重新加载
				}else{
					alert(response.message);
				}
			}
		);
	}

	
	//查询实体 
	$scope.findOne = function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//批量删除 
	$scope.dele = function(){			
		//获取选中的复选框			
		itemCatService.dele($scope.selectIds).success(
			function(response){
				if(response.success){
					$scope.reloadList();
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	// 定义搜索对象 
	$scope.searchEntity = {};
	// 搜索
	$scope.search = function(page,size){			
		itemCatService.search(page,size,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;
			}			
		);
	}


	$scope.findByParentId=function(pId){
		itemCatService.findByParentId(pId).success(
			function(response){
				$scope.list=response;
			}
		);
	}


	$scope.grade = 1;
	$scope.setGrade = function (grade) {
		$scope.grade = grade;
	}

	$scope.selectList=function(pEntity){
		if($scope.grade==1){//如果为1级
			$scope.entity1=null;
			$scope.entity2=null;
		}
		if($scope.grade==2){//如果为2级
			$scope.entity1=pEntity;
			$scope.entity2=null;
		}
		if($scope.grade==3){//如果为3级
			$scope.entity2=pEntity;
		}
		$scope.findByParentId(pEntity.id);	//查询此级下级列表
	}


	$scope.typeTemplateList = {data:[]};
	$scope.findtypeTemplateList = function () {

		typeTemplateService.selectList.success(function (response) {

			$scope.typeTemplateList = {data:response};
		})
	}




});	
