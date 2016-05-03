package io.monger.validation.resource;

import io.monger.validation.model.Combined;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/*
 * Copyright (c) 2016 Phillip Babbitt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * CRUD endpoints for the combined model.
 */
@Component
@Path("/combined")
public class CombinedResource {
    private final Map<Integer, Combined> combinedMap = new HashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Combined> list() {
        return combinedMap.values();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid @NotNull final Combined model) {
        if (model.getId() != null && model.getId() > 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            model.setId(getNextId());
            combinedMap.put(model.getId(), model);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(@PathParam("id") final Integer id) {
        if (combinedMap.containsKey(id)) {
            return Response.ok(combinedMap.get(id), MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") final Integer id, final @Valid Combined model) {
        if (combinedMap.containsKey(id)) {
            combinedMap.put(id, model);
            return Response.ok(model, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final Integer id) {
        if (combinedMap.containsKey(id)) {
            combinedMap.remove(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private Integer getNextId() {
        Integer topId = 0;
        for (Map.Entry<Integer, Combined> entry : combinedMap.entrySet()) {
            if (entry.getValue().getId() > topId) {
                topId = entry.getValue().getId();
            }
        }
        return ++topId;
    }
}
