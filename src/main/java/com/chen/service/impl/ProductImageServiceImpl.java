package com.chen.service.impl;

import com.chen.dao.ProductImageDao;
import com.chen.entity.ProductImage;
import com.chen.service.ProductImageService;
import com.chen.util.ImageUtils;
import com.chen.util.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * Created by handsome programmer.
 * User: chen
 * Date: 2019/3/30
 * Time: 2:42
 * Description:
 *
 * @author chen
 */
@Service
public class ProductImageServiceImpl implements ProductImageService {

    /**
     * 商品详情图片信息持久层
     */
    private ProductImageDao productImageDao;

    @Override
    public int addInBatchesProductImage(List<ProductImage> productImageList) {
        return productImageDao.bulkInsertProductImage(productImageList);
    }

    @Override
    public File getProductImage(String uuid) {
        ProductImage productImage = productImageDao.queryProductImageByUUID(uuid);
        String productImagePath = PathUtils.getImageBasePath() + productImage.getImgPath();
        productImagePath = PathUtils.replaceFileSeparator(productImagePath);
        assert productImagePath != null;
        return new File(productImagePath);
    }

    @Override
    public List<String> queryProductImageList(int productId) {
        return productImageDao.queryProductImageList(productId);
    }

    @Override
    public boolean deleteProductImageByUUID(String uuid) {
        //删除图片文件
        ProductImage productImage = productImageDao.queryProductImageByUUID(uuid);
        ImageUtils.deleteProductImage(productImage.getImgPath());
        return productImageDao.deleteProductImageByUUId(uuid) != 0;
    }

    @Autowired
    public void setProductImageDao(ProductImageDao productImageDao) {
        this.productImageDao = productImageDao;
    }
}
