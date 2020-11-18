package com.springwebflux.app;

import com.springwebflux.app.models.dao.ProductoDao;
import com.springwebflux.app.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoDao dao;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		mongoTemplate.dropCollection("producto").subscribe();

		Flux.just(
				new Producto("TV",456.56),
				new Producto("Camara",500.56),
				new Producto("Play 4",700.56)
		).flatMap(producto -> {
			producto.setCreateAt(new Date());
			return dao.save(producto);
		}).subscribe(producto -> log.info("Insert:" + producto.getId() + " " + producto.getNombre()));
	}
}
