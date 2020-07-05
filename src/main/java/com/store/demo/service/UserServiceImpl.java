package com.store.demo.service;

import com.store.demo.DTO.OutputProductDTO;
import com.store.demo.domain.Product;
import com.store.demo.domain.User;
import com.store.demo.repository.ProductRepo;
import com.store.demo.repository.TypeRepo;
import com.store.demo.repository.UserRepo;
import org.modelmapper.ModelMapper;
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
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl extends ListToPageConverter implements UserService{

    private final ProductRepo productRepo;

    private final TypeRepo typeRepo;

    private final UserRepo userRepo;

    private final ModelMapper modelMapper;

    private final CacheManager cacheManager;

    private static final Logger LOGGER =  LoggerFactory.getLogger(UserServiceImpl.class.getName());

    @Autowired
    public UserServiceImpl(ProductRepo productRepo, TypeRepo typeRepo, UserRepo userRepo, ModelMapper modelMapper, CacheManager cacheManager) {
        this.productRepo = productRepo;
        this.typeRepo = typeRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.cacheManager = cacheManager;
    }

    @Override
    @Cacheable(value = "products")
    @Retryable(value = SQLException.class)
    public List<OutputProductDTO> findAllPage(int page) {
        Page<Product> products= productRepo.findAll(PageRequest.of(page, 5));

        if(page < products.getTotalPages()) {
            LOGGER.info("User get paginated list of products");
            return products.getContent()
                    .stream().map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        else {
            LOGGER.error(String.format("Page number %s out of bounds", page));
            throw new IllegalArgumentException(String.format("Page number %s out of bounds", page));
        }
    }

    @Override
    @Cacheable(value = "products")
    @Retryable(value = SQLException.class)
    public OutputProductDTO getById(int id) {
        if(productRepo.existsById(id)){
            LOGGER.info("User got product with ID: "+id);
            return convertToDTO(productRepo.getOne(id));
        }
        LOGGER.warn("Invalid product ID: "+id);
        return null;
    }

    @Override
    @Cacheable(value = "products")
    @Retryable(value = SQLException.class)
    public List<OutputProductDTO> getByType(String type, int p){
        List<OutputProductDTO> products=productRepo
                .findAllByType(typeRepo.findByType(type))
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());

        LOGGER.info("User got all products with type: "+type);
        return getListOfPage(products, p, 1);
    }

    @Override
    @Retryable(value = SQLException.class)
    public String addOrder(String productName){
        User user= userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(productRepo.findByName(productName)!=null) {
            user.getProducts().add(productRepo.findByName(productName));
            userRepo.save(user);
            LOGGER.info(String.format("Product %s was added to cart", productName));
            Objects.requireNonNull(cacheManager.getCache("users")).clear();
            return String.format("Product %s was added to cart", convertToDTO(productRepo.findByName(productName)));
        }
        else LOGGER.debug("Product does not exist");
        throw new IllegalArgumentException(String.format("Product %s does not exist", productName));
    }

    @Override
    @Cacheable(value = "users")
    @Retryable(value = SQLException.class)
    public List<OutputProductDTO> showOrders(int p) {
        List<OutputProductDTO> products= userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .getProducts()
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());
        LOGGER.info("User get his cart");
        return getListOfPage(products, p, 2);
    }

    @Recover
    public Product recover(SQLException sqlException, int id){      //      Recover for getById, placeholder for now
        LOGGER.error(String.format("Recover method initialized on error %s",sqlException.getMessage()));
        return null;
    }

    private OutputProductDTO convertToDTO(Product product){
        return modelMapper.map(product, OutputProductDTO.class);
    }
}