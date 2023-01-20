package example.app.springbootwebfluxapirest.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import example.app.springbootwebfluxapirest.models.documents.Categoria;
import reactor.core.publisher.Mono;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String>{

    public Mono<Categoria> findCategoriaByNombre(String nombre);

}
