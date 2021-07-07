package com.borgescloud.datastax.retailer.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// import com.borgescloud.datastax.retailer.model.ChangeSet;
import com.borgescloud.datastax.retailer.model.Credential;
import com.borgescloud.datastax.retailer.model.Product;
import com.borgescloud.datastax.retailer.model.ProductRows;
import com.borgescloud.datastax.retailer.model.ProductData;
import com.borgescloud.datastax.retailer.model.Stock;
import com.borgescloud.datastax.retailer.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@RestController
@RequestMapping("/api/demo")
public class DemoRestController {

    @Value("${ASTRA_DB_ID:86cc0804-5759-459d-b989-3885662e01d9}")
    private String ASTRA_DB_ID;
    @Value("${ASTRA_DB_REGION:us-west1}")
    private String ASTRA_DB_REGION;
    @Value("${ASTRA_DB_USERNAME:YvtXEZHRRtcdLecgyEKMNCMg}")
    private String ASTRA_DB_USERNAME;
    @Value("${ASTRA_DB_PASSWORD:Q0ZWn-,nzT.s+rAM0f8Ol61.I42Fe+xyb21x_YMY1wXP+Cdc5-5AP+bltm,BpXdTJb+s3qzQOPE6n3Xy9eb_012zwb_ar6,oHZMXU0WitruZSyAQ+QRFFUFb.L0,PLQR}")
    private String ASTRA_DB_PASSWORD;
    @Value("${ASTRA_DB_KEYSPACE:retailer}")
    private String ASTRA_DB_KEYSPACE;

    private final String authTokenTemplate = "https://%s-%s.apps.astra.datastax.com/api/rest/v1/auth";
    private final String dataTemplate = "https://%s-%s.apps.astra.datastax.com/api/rest/v1/keyspaces/%s/tables/%s/rows/%s";
    private final String dataTemplate2 = "https://%s-%s.apps.astra.datastax.com/api/rest/v2/keyspaces/%s/%s/%s";

    @Autowired
    private ObjectMapper objectMapper;

    private String authToken;

    /**
     * List Products
     */
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> listProducts() {

        String authToken = getAuthToken();
        log.info("calling product list with authtoken {} from Astra", authToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cassandra-token", authToken);
        HttpEntity<?> request = new HttpEntity<Object>("", headers);

        String url = String.format(dataTemplate, ASTRA_DB_ID, ASTRA_DB_REGION, ASTRA_DB_KEYSPACE, "product","");
        ResponseEntity<ProductRows> products = restTemplate.exchange(url, HttpMethod.GET, request, ProductRows.class);

        log.info("retrieved {} rows", products.getBody().getCount());

        List<Product> productList = Arrays.asList(products.getBody().getRows());
        return productList;

    }

    /**
     * Get Product
     */
    @RequestMapping(value = "/products/{uniqId}/{sku}", method = RequestMethod.GET)
    public Product getProduct(@PathVariable("uniqId") String uniqId, @PathVariable("sku") String sku) {
        
        String authToken = getAuthToken();
        log.info("calling get product with authtoken {} from Astra", authToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cassandra-token", authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> request = new HttpEntity<Object>("", headers);

        String primaryKey = String.format("%s/%s", uniqId, sku);

        String url = String.format(dataTemplate2, ASTRA_DB_ID, ASTRA_DB_REGION, ASTRA_DB_KEYSPACE, "product",
                primaryKey);
        ResponseEntity<ProductData> products = restTemplate.exchange(url, HttpMethod.GET, request, ProductData.class);

        log.info("retrieved {} rows", products.getBody().getCount());

        List<Product> productList = Arrays.asList(products.getBody().getData());
        return productList.get(0);
    }

    /**
     * Update Stock
     * 
     * @param stock
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/stock", method = RequestMethod.PUT)
    public void updateStoreStock(Stock stock) throws JsonProcessingException {

        String authToken = getAuthToken();
        log.info("updating product {} stock with authtoken {} from Astra", stock.getUniqId(), authToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cassandra-token", authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Should denormalize the table
        Product p = getProduct(stock.getUniqId(), stock.getSku());
        stock.setNameTitle(p.getNameTitle());
        stock.setSalePrice(p.getSalePrice());

        // ChangeSet cs = new ChangeSet();
        // cs.addChanges("total_units", "" + stock.getTotalUnits());
        // String csJson = objectMapper.writeValueAsString(cs);

        Map<String, Object> mapCs = new HashMap<>();
        mapCs.put("name_title", stock.getNameTitle());
        mapCs.put("sale_price", stock.getSalePrice());
        mapCs.put("total_units", stock.getTotalUnits());
        String csJson = objectMapper.writeValueAsString(mapCs);

        HttpEntity<String> request = new HttpEntity<>(csJson, headers);

        String url = String.format(dataTemplate2, ASTRA_DB_ID, ASTRA_DB_REGION, ASTRA_DB_KEYSPACE, "assortment_price",
                stock.getPrimaryKey());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

        log.info("Response: {}", response);

    }

    ////////////////////////////////////////////////////

    private String getAuthToken() {

        if (authToken == null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Credential> request = new HttpEntity<>(new Credential(ASTRA_DB_USERNAME, ASTRA_DB_PASSWORD));
            Token authTokenObj = restTemplate.postForObject(String.format(authTokenTemplate, ASTRA_DB_ID, ASTRA_DB_REGION),
                    request, Token.class);
            log.info("received authtoken {} from Astra", authTokenObj);

            authToken = authTokenObj.getAuthToken();
        } else {
            log.info("using cached auth token");
        }

        return authToken;
    }

}
