package com.offcn.search.service;

import com.offcn.pojo.TbItem;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {

    public Map<String,Object> search(Map searchMap);

    public void deleteItem(Long[] ids);

    public void importItemLost(List<TbItem> itemList);

}
