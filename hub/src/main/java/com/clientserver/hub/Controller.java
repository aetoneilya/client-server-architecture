package com.clientserver.hub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/figures")
public class Controller {
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public Controller(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostMapping
    public ResponseEntity<String> addJson(@RequestBody String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    mongoTemplate.save(node.toString(), "figures");
                }
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJson(@PathVariable Integer id) {
        return new ResponseEntity<>(mongoTemplate.getCollection("figures").find().skip(id).first(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getJsons() {
        return new ResponseEntity<>(mongoTemplate.getCollection("figures").find().into(new ArrayList<>()), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getSize() {
        return new ResponseEntity<>(mongoTemplate.getCollection("figures").countDocuments(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        mongoTemplate.dropCollection("figures");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
