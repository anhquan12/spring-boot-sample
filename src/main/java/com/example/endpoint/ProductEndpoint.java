package com.example.endpoint;

import com.example.entity.CustomErrorType;
import com.example.entity.Product;
import com.example.model.ProductModel;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import rx.schedulers.Schedulers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductEndpoint {

    @Autowired
    private ProductModel productModel;

    @Autowired
    private ProductService productService;

//    @RequestMapping(path = "/endpoint/product/delete/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity delete(@PathVariable int id) {
////        Optional<Product> optionalProduct = productModel.findById(id);
//        if (optionalProduct.isPresent()) {
//            Product product = optionalProduct.get();
//            product.setStatus(0);
//            productModel.save(product);
//            return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
//        } else {
//            return new ResponseEntity(new CustomErrorType("Unable to delete. Product with id " + id + " not found."),
//                    HttpStatus.NOT_FOUND);
//        }
//    }

    @RequestMapping(path = "/endpoint/product/delete-many", method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestBody String ids) throws UnsupportedEncodingException {
        String[] splittedIds = java.net.URLDecoder.decode(ids, "UTF-8").split(",");
        Integer[] arrayIds = new Integer[splittedIds.length];
        for (int i = 0; i < splittedIds.length; i++) {
            arrayIds[i] = new Integer(splittedIds[i]);
        }
        Iterable<Product> list = productModel.findAllById(Arrays.asList(arrayIds));
        for (Product p :
                list) {
            p.setStatus(0);
        }
        productModel.saveAll(list);
        return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/file/image")
    public ResponseEntity<?> handleFileUploadImages(@RequestParam(name = "file") List<MultipartFile> files, @RequestParam(name = "file_name") List<String> fileNames) {
        return productService.saveImages(files, fileNames);
    }

    @GetMapping(value = "/public/file/image/{file:.+}")
    public ResponseEntity<?> getImage(@PathVariable(name = "file") String file) {
        return productService.getImage("origin", file);
    }

    @GetMapping(value = "/public/file/image/{size}/{file:.+}")
    public ResponseEntity<?> resize(@PathVariable(name = "size") String size,
                                    @PathVariable(name = "file") String file) {
        return productService.getImage(size, file);
    }
}
