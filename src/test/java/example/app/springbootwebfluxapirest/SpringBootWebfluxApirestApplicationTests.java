package example.app.springbootwebfluxapirest;

import example.app.springbootwebfluxapirest.models.documents.Categoria;
import example.app.springbootwebfluxapirest.models.documents.Producto;
import example.app.springbootwebfluxapirest.models.services.ProductoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootWebfluxApirestApplicationTests {

	@Autowired
	private WebTestClient client;
	@Autowired
	private ProductoService productoService;
	@Value("${config.base.endpoint}")
	private String url;
	@Test
	void listarTest() {
		client.get()
				.uri("/api/v2/productos")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBodyList(Producto.class)
				/*.consumeWith(response -> {
					List<Producto> productos = response.getResponseBody();
					productos.forEach(p -> {
						System.out.println(p.getNombre());
					});
					assertTha()
				});*/
				.hasSize(9);
	}

	@Test
	void verTest() {

		Producto producto = productoService.findByNombre("TV Panasonic Pantalla LCD").block();

		client.get()
				.uri("/api/v2/productos/{id}", Collections.singletonMap("id", producto.getId()))
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.nombre").isEqualTo("TV Panasonic Pantalla LCD");

	}

	@Test
	void crearTest() {
		Categoria categoria = productoService.findCategoriaByNombre("Muebles").block();

		Producto producto = new Producto("Mesa comedor", 100.00, categoria);
		client.post().uri("/api/v2/productos")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(producto), Producto.class)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.nombre").isEqualTo("Mesa comedor")
				.jsonPath("$.categoria.nombre").isEqualTo("Muebles");
	}

	@Test
	void editarTest() {
		Producto producto = productoService.findByNombre("TV Panasonic Pantalla LCD").block();
		Categoria categoria = productoService.findCategoriaByNombre("Muebles").block();
		Producto productoEditado = new Producto("TV Panasonic Pantalla LCD2", 900.00, categoria);

		client.put().uri("/api/v2/productos/{id}", Collections.singletonMap("id", producto.getId()))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(producto), Producto.class)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.nombre").isEqualTo("TV Panasonic Pantalla LCD2")
				.jsonPath("$.categoria.nombre").isEqualTo("Muebles");

	}

	@Test
	public void eliminarTest() {
		Producto producto = productoService.findByNombre("Mica Cómoda 5 Cajones").block();
		client.delete()
				.uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
				.exchange()
				.expectStatus().isNoContent()
				.expectBody()
				.isEmpty();

		client.get()
				.uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
				.exchange()
				.expectStatus().isNotFound()
				.expectBody()
				.isEmpty();
	}
}
