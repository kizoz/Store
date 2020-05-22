package com.store.demo.service;

import com.store.demo.domain.Product;
import com.store.demo.domain.User;
import com.store.demo.repository.ProductRepo;
import com.store.demo.repository.TypeRepo;
import com.store.demo.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final ProductRepo productRepo;

    private final TypeRepo typeRepo;

    private final UserRepo userRepo;

    private final CacheManager cacheManager;

    private static final Logger LOGGER =  LoggerFactory.getLogger(UserServiceImpl.class.getName());

    @Autowired
    public UserServiceImpl(ProductRepo productRepo, TypeRepo typeRepo, UserRepo userRepo, CacheManager cacheManager) {
        this.productRepo = productRepo;
        this.typeRepo = typeRepo;
        this.userRepo = userRepo;
        this.cacheManager = cacheManager;
    }

    @Override
    @Cacheable(value = "products")
    @Retryable(value = SQLException.class)
    public Page<Product> findAllPage(int page) {
        Page<Product> products= productRepo.findAll(PageRequest.of(page, 5));
        if(page < products.getTotalPages()) {
            LOGGER.info("User get paginated list of products");
            return products;
        }
        else {
            LOGGER.error(String.format("Page number %s out of bounds", page));
            throw new IllegalArgumentException(String.format("Page number %s out of bounds", page));
        }
    }

    @Override
    @Cacheable(value = "products")
    @Retryable(value = SQLException.class)
    public Product getById(int id) {
        if(productRepo.existsById(id)){
            LOGGER.info("User got product with ID: "+id);
            return productRepo.getOne(id);
        }
        LOGGER.warn("Invalid product ID: "+id);
        return null;
    }

    @Override
    @Cacheable(value = "products")
    @Retryable(value = SQLException.class)
    public List<Product> getByType(String type, int p){
        List<Product> products=productRepo.findAllByType(typeRepo.findByType(type));

        LOGGER.info("User got all products with type: "+type);
        return ProductServiceImpl.getListOfPage(products, p);
    }

    @Override
    @Retryable(value = SQLException.class)
    public String addOrder(String productName){
        User user= userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(productRepo.findByName(productName)!=null) {
            LOGGER.info(String.format("Product %s was added to cart", productName));
            user.getProducts().add(productRepo.findByName(productName));
            userRepo.save(user);
            Objects.requireNonNull(cacheManager.getCache("users")).clear();
            return String.format("Product %s was added to cart", productName);
        }
        else LOGGER.debug("Product does not exist");
        throw new IllegalArgumentException(String.format("Product %s does not exist", productName));
    }

    @Override
    @Cacheable(value = "users")
    @Retryable(value = SQLException.class)
    public List<Product> showOrders(int p) {
        List<Product> products= userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .getProducts();
        LOGGER.info("User get his cart");
        return ProductServiceImpl.getListOfPage(products, p);
    }

    @Recover
    public Product recover(SQLException sqlException, int id){      //      Recover for getById
        LOGGER.error(String.format("Recover method initialized on error %s",sqlException.getMessage()));
        return null;
    }
}