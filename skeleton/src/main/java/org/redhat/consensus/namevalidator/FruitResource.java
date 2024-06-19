package org.redhat.consensus.namevalidator;

import java.util.List;
import java.net.URI;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import javax.inject.Inject;
import io.quarkus.logging.Log;

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitResource {

	@Inject
	FruitRepository repository;

	@GET
	public List<Fruit> list() {
		return repository.listAll();
	}

	@POST
	@Transactional
	public Response create(Fruit fruit) {
		repository.persist(fruit);
		Log.info("A fruit has been created " + fruit.toString() + ".");
		return Response.created(URI.create("/fruits/" + fruit.id)).build();
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public void delete(Long id) {
		Fruit entity = repository.findById(id);
		if (entity == null) {
			throw new NotFoundException();
		}
		if (repository.isPersistent(entity)) {
			repository.delete(entity);
		}
	}
}