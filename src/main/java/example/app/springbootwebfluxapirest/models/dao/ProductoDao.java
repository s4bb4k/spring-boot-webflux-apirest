package example.app.springbootwebfluxapirest.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import example.app.springbootwebfluxapirest.models.documents.Producto;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String>{

}
