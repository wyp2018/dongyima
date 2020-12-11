package com.offcn.sellergoods.service;


import java.util.List;

import com.offcn.entity.PageResult;
import com.offcn.pojo.TbBrand;


public interface BrandService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbBrand> findAll();
    public PageResult findPage(int pageNum,int pageSize);

    void add(TbBrand brand);

    TbBrand findOne(long id);

    void update(TbBrand brand);
    void delete(long [] ids);

    public PageResult search(TbBrand brand, int pageNum, int pagSize);
}
