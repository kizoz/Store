package com.store.demo.service;

import com.store.demo.domain.Product;
import com.store.demo.domain.TypeOfProduct;
import com.store.demo.domain.User;
import com.store.demo.repository.ProductRepo;
import com.store.demo.repository.TypeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    private final TypeRepo typeRepo;

    private final CacheManager cacheManager;

    private static final Logger LOGGER =  LoggerFactory.getLogger(ProductServiceImpl.class.getName());

    @Autowired
    public ProductServiceImpl(ProductRepo repo, TypeRepo typeRepo, CacheManager cacheManager) {
        this.productRepo = repo;
        this.typeRepo = typeRepo;
        this.cacheManager = cacheManager;
    }

    @Override
    @Retryable(value = SQLException.class)
    public String addType(String type){
        TypeOfProduct typeOfProduct =new TypeOfProduct();
        typeOfProduct.setType(type);
        typeRepo.save(typeOfProduct);
        LOGGER.info("New type of product was added");
        return typeOfProduct.toString();
    }

    @Override
    @Retryable(value = SQLException.class)
    public String addProduct(String name,
                           Integer price,
                           String type) {
        if(typeRepo.findByType(type)==null){
            LOGGER.error("Invalid product type");
            throw new IllegalArgumentException(String.format("Type %s does not exist", type));
        }
        Product prod= new Product();
        prod.setPrice(price);
        prod.setName(name);
        prod.setType(typeRepo.findByType(type));
        productRepo.save(prod);
        LOGGER.info("User added new product");
        Objects.requireNonNull(cacheManager.getCache("products")).clear();
        return prod.toString();
    }

    @Override
    @Retryable(value = SQLException.class)
    public String editProduct(Integer id,
                              String name,
                              int price,
                              String type) {
        Product prod;
        if(productRepo.existsById(id)){
            prod=productRepo.findById(id).get();

            prod.setPrice(price);
            prod.setName(name);
            prod.setType(typeRepo.findByType(type));
            productRepo.save(prod);
            LOGGER.info("User has changed product with ID: "+id);
            Objects.requireNonNull(cacheManager.getCache("products")).clear();
            return "Product with id= "+id+" was updated";
        }
        LOGGER.warn("Invalid ID: "+id);
        return "Product does not exist";
    }

    @Override
    @Retryable(value = SQLException.class)
    public String deleteById(int id) {
        if(productRepo.existsById(id)) {
            productRepo.findById(id).ifPresent(product -> product.setType(null));
            productRepo.deleteById(id);
            LOGGER.info("User deleted product with ID: "+id);
            Objects.requireNonNull(cacheManager.getCache("products")).clear();
            return "Product was deleted";
        }
        LOGGER.warn("Invalid ID: "+id);
        return "Product does not exist";
    }

    @Override
    @Retryable(value = SQLException.class)
    @Cacheable(value = "users")
    public List<User> showCustomers(String productName, int p) {
        if (productRepo.findByName(productName) != null) {
            List<User> products=productRepo.findByName(productName).getUsers();

            LOGGER.info("List of users who got " + productName);
            return getListOfPage(products, p);
        } else {
            LOGGER.info("Product does not exist");
            return null;
        }
    }

    @Recover
    public String recover(SQLException sqlException, int id){               //      Recover for deleteById
        LOGGER.error(String.format("Recover method initialized on error %s",sqlException.getMessage()));
        return "Cannot delete product";
    }

    protected static <T> List<T> getListOfPage(List<T> list, int p){        //  Gets List, returns page by number
        PagedListHolder<T> page= new PagedListHolder<>(list);
        page.setPageSize(1);
        if(p>page.getPageCount()) {
            LOGGER.error(String.format("Page number %s out of bounds", p));
            throw new IllegalArgumentException(String.format("Page number %s out of bounds", p));
        }
        page.setPage(p);
        return page.getPageList();
    }
}