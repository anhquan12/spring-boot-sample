package com.example.controller;


import com.example.entity.Product;
import com.example.entity.ProductValidator;
import com.example.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Optional;

@Controller
public class ProductController {

    private static final String UPLOADED_FOLDER = "target/classes/static/uploaded/";
//    private static final String DOMAIN = "http://localhost:8888";

    @Autowired
    private ProductModel productModel;

    @RequestMapping(path = "/product/create", method = RequestMethod.GET)
    public String createProduct(@ModelAttribute Product p) {
        return "product-form";
    }

    @RequestMapping(path = "/product/create", method = RequestMethod.POST)
    public String saveProduct(@Valid Product product, BindingResult result,
                              @RequestParam("myFile") MultipartFile myFile) {
        product.setImgUrl("_");
        new ProductValidator().validate(product, result);
        if (result.hasErrors()) {
            return "product-form";
        }
        try {
            Path path = Paths.get(  UPLOADED_FOLDER + myFile.getOriginalFilename());
            Files.write(path, myFile.getBytes());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        product.setImgUrl("/uploaded/" + myFile.getOriginalFilename());
        productModel.save(product);
        return "redirect:/product/list";
    }

    @RequestMapping(path = "/product/edit/{id}", method = RequestMethod.GET)
    public String editProduct(int id, Model model) {
        Optional<Product> optionalProduct = productModel.findById(id);
        if (optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());
            return "product-form";
        } else {
            return "not-found";
        }
    }

//    @RequestMapping(path = "/product/edit/{id}", method = RequestMethod.POST)
//    public String updateProduct(@PathVariable int id,@Valid Product product, BindingResult result, Model model){
//        Optional<Product> optionalProduct = productModel.findById(id);
//        if (optionalProduct.isPresent()) {
//            if (result.hasErrors()) {
//                return "product-form";
//            }
//            productModel.save(product);
//            return "redirect:/product/list";
//        } else {
//            return "not-found";
//        }
//    }

    @RequestMapping(path = "/product/delete/{id}", method = RequestMethod.POST)
    public String deleteProduct(int id) {
        Optional<Product> optionalProduct = productModel.findById(id);
        if (optionalProduct.isPresent()) {
            productModel.delete(optionalProduct.get());
            return "redirect:/product/list";
        } else {
            return "not-found";
        }
    }

    @RequestMapping(path = "/product/list", method = RequestMethod.GET)
    public String getListProduct(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
        Page<Product> pagination = productModel.findProductsByStatus(1, PageRequest.of(page - 1, limit));
        model.addAttribute("pagination", pagination);
        model.addAttribute("page", page);
        model.addAttribute("limit", limit);
        model.addAttribute("datetime", Calendar.getInstance().getTime());
        return "product-list";
    }

}
