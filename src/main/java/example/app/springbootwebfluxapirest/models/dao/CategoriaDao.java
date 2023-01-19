package example.app.springbootwebfluxapirest.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import example.app.springbootwebfluxapirest.models.documents.Categoria;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String>{

}
