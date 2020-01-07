package com.example.endpoint;

import com.example.builder.ProductBuilder;
import com.example.entity.Product;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;
import com.example.util.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class ProductEndpoint {

    @Autowired
    private ProductRepository productRepository;

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
            arrayIds[i] = Integer.valueOf(splittedIds[i]);
        }
        Iterable<Product> list = productRepository.findAllById(Arrays.asList(arrayIds));
        for (Product p :
                list) {
            p.setStatus(0);
        }
        productRepository.saveAll(list);
        return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "get")
    public Page<Product> getAll(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "price", required = false) String price,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "2") int size) {
        Pageable pageable = PageRequest.of(page-1 , size);
        Page<Product> result = productRepository.findByNameAndPrice(name, price, pageable);
        return new PageImpl<>(result.getContent(),pageable, result.getTotalElements());
        }

    @GetMapping(value = "get2")
    public PageResponse<Product> getAll(@RequestParam Map<String, String> params,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "2") int size) {
        ProductBuilder builder = new ProductBuilder.Builder()
                        .setName(params.get("name"))
                        .setPrice(params.get("price"))
                        .build();
        Pageable pageable = PageRequest.of(page-1 , size);
        Page<Product> result = productRepository.findAll2(builder, pageable);
        return new PageResponse<>(result.getContent(), result.getTotalPages(), result.getTotalElements(), result.getNumber(), result.getSize());
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
