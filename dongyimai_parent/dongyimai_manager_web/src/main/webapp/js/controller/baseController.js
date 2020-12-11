//基本控制层
app.controller('baseController' ,function($scope){
    //重新加载列表 数据
    $scope.reloadList=function(){
        //切换页码
        $scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }
    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 3,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }
    };
    $scope.selectIds=[];//选中的ID集合
    //更新复选
    $scope.updateSelection = function($event, id) {

        if($event.target.checked){//如果是被选中,则增加到数组
            $scope.selectIds.push( id);
        }else{
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);//删除
        }
    }
// 全选
    $scope.selectAll = function($event) {
        var state = $event.target.checked;
        $(".eachbox").each(function(index, item) {
            item.checked = state;
            var id = parseInt($(item).parent().next().text());
            if (state) {
                $scope.selectIds.push(id);
            } else {
                var idx = $scope.selectIds.indexOf(id);
                $scope.selectIds.splice(idx, 1);
            }
        });
    }
    //判断某个id是否在数组中存在
    $scope.existId = function (id) {
        for (var i = 0; i < $scope.selectIds.length; i++){
            if (id == $scope.selectIds[i]){
                return true;
            }
        }

        /*for (var i in $scope.selectIds){
            if (i == id){
                return true;
            }
        }*/
        return false;
    }

    $scope.jsonToString=function(jsonString,key){
        var json=JSON.parse(jsonString);//将json字符串转换为json对象
        var value="";
        for(var i=0;i<json.length;i++){
            if(i>0){
                value+=","
            }
            value+=json[i][key];
        }
        return value;
    }


});
