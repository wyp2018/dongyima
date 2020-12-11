//goods控制层 
app.controller('goodsController', function ($scope, $controller, $location, goodsService, uploadService, itemCatService, typeTemplateService) {

    // 继承
    $controller("baseController", {
        $scope: $scope
    });

    // 保存
    $scope.save = function () {

        $scope.entity.goodsDesc.introduction = editor.html();
        goodsService.save($scope.entity).success(function (response) {
            if (response.success) {
                //跳转到商品列表
                location.href = "goods.html";
            } else {
                alert(response.message);
            }
        });
    }

    //查询实体
    $scope.findOne = function () {

        //接收从goods.html传递过来的参数

        var id = $location.search()['id'];


        if (id == null) {
            return;
        }
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;

                //富文本信息
                editor.html($scope.entity.goodsDesc.introduction);
                //图片信息
                $scope.entity.goodsDesc.itemImages =
                    JSON.parse($scope.entity.goodsDesc.itemImages);

                //扩展属性
                $scope.entity.goodsDesc.customAttributeItems =
                    JSON.parse($scope.entity.goodsDesc.customAttributeItems);

                //规格属性
                $scope.entity.goodsDesc.specificationItems =
                    JSON.parse($scope.entity.goodsDesc.specificationItems);

                for (var i = 0; i < $scope.entity.itemList.length; i++) {
                    $scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
                }

            }
        );
    }

    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                    $scope.selectIds = [];
                }
            }
        );
    }

    // 定义搜索对象
    $scope.searchEntity = {};
    // 搜索
    $scope.search = function (page, size) {
        goodsService.search(page, size, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;
            }
        );
    }


    //图片上传
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (response) {

            if (response.success) {
                $scope.image_entity.url = response.message;
            } else {
                alert(response.message);
            }

        })
    }

    //初始化一个商品对象
    $scope.entity = {"goodsDesc": {"itemImages": [], "specificationItems": []}};


    $scope.saveImag = function () {

        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    $scope.deleImg = function (idx) {
        $scope.entity.goodsDesc.itemImages.splice(idx, 1);
    }

    $scope.findParentCat = function (pId) {
        itemCatService.findByParentId(pId).success(function (response) {

            $scope.itemCatList = response;
        });
    }


    //监听一级分类变化,获取2级分类
    //第一个参数监听谁
    //第二个变化了干什么
    $scope.$watch("entity.goods.category1Id", function (newVal, oldVal) {
        if (newVal) {
            itemCatService.findByParentId(newVal).success(function (response) {

                $scope.itemCat2List = response;
            });
        }
    })


    $scope.$watch("entity.goods.category2Id", function (newVal, oldVal) {
        if (newVal) {
            itemCatService.findByParentId(newVal).success(function (response) {

                $scope.itemCat3List = response;
            });
        }

    })

    //模板id
    $scope.$watch("entity.goods.category3Id", function (newVal, oldVal) {
        if (newVal) {
            itemCatService.findOne(newVal).success(function (response) {

                $scope.entity.goods.typeTemplateId = response.typeId; //
            });
        }

    })


    //获取品牌列表
    $scope.$watch("entity.goods.typeTemplateId", function (newVal, oldVal) {
        if (newVal) {
            typeTemplateService.findOne(newVal).success(function (response) {

                $scope.typeTemplateId = response;

                $scope.typeTemplateId.brandIds = JSON.parse($scope.typeTemplateId.brandIds);
            });
        }

    })

    //扩展属性
    $scope.$watch('entity.goods.typeTemplateId', function (newValue, oldValue) {
        if (newValue) {
            typeTemplateService.findOne(newValue).success(
                function (response) {
                    $scope.typeTemplate = response;
                    $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
                    if ($location.search()['id'] == null) {
                        $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);//扩展属性

                    }
                }
            );
            typeTemplateService.findSpecAndOptionList(newValue).success(function (response) {

                $scope.list = response;
            })
        }
    });


    //记录选择的规格
    $scope.selectSpec = function ($event, specName, optionName) {

        var obj = $scope.searchValInarr($scope.entity.goodsDesc.specificationItems, "attributeName", specName);
        if (obj != null) {
            if ($event.target.checked) {
                obj.attributeValue.push(optionName);
            } else {
                obj.attributeValue.splice(obj.attributeValue.indexOf(optionName), 1);
                if (obj.attributeValue.length == 0) {
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(obj), 1);
                }
            }

        } else {
            $scope.entity.goodsDesc.specificationItems.push({
                "attributeValue": [optionName],
                "attributeName": specName
            });

        }

    }


    $scope.createItemList = function () {

        $scope.entity.itemList = [{spec: {}, price: 0, num: 9999, status: '0', isDefault: '0'}];
        var items = $scope.entity.goodsDesc.specificationItems;
        for (var i = 0; i < items.length; i++) {
            $scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
        }
    }
    addColumn = function (list, columnName, conlumnValues) {
        var newList = [];//新的集合
        for (var i = 0; i < list.length; i++) {
            var oldRow = list[i];
            for (var j = 0; j < conlumnValues.length; j++) {
                var newRow = JSON.parse(JSON.stringify(oldRow));//深克隆
                newRow.spec[columnName] = conlumnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }


    //分类数组
    $scope.catName = [];

    //显示分类名
    $scope.findItemCat = function () {

        itemCatService.findAll().success(function (response) {

            for (var i = 0; i < response.length; i++) {
                $scope.catName[response[i].id] = response[i].name;
            }
        })
    }


    //显示状态数组
    $scope.statusName = ["未审核", "审核通过", "审核未通过", "关闭"];


    //判断规格选项是否勾选
    $scope.isSelected = function (specName, optionName) {
        var obj =
            $scope.searchValInarr($scope.entity.goodsDesc.specificationItems, "attributeName", specName);
        if (obj == null) {
            return false;
        } else {
            if (object.attributeValue.indexOf(optionName) >= 0) {
                return true;
            } else {
                return false;
            }

        }
    }


    //提交重审
    $scope.updateStatus = function (status) {
        goodsService.updateStatus($scope.selectIds, status).success(function (response) {
                if (response.success) {//成功
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];//清空ID集合
                }
            }
        );
    }


});
