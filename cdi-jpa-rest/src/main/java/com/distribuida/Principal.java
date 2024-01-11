package com.distribuida;

import com.distribuida.db.Persona;
import com.distribuida.servicios.ServicioPersona;
import com.google.gson.Gson;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import spark.Request;
import spark.Response;

import java.util.List;

import static spark.Spark.*;

public class Principal {
    static SeContainer container;

    static List<Persona> listarPersonas(Request req, Response res) {
        var servicio = container.select(ServicioPersona.class)
                .get();
        res.type("application/json");

        return servicio.findAll();
    }

    static Persona buscarPersona(Request req, Response res) {
        var servicio = container.select(ServicioPersona.class)
                .get();
        res.type("application/json");

        String _id = req.params(":id");
        var persona = servicio.findById(Integer.valueOf(_id));

        if (persona == null) {
            halt(404, "Persona no encontrada");
        }

        return persona;
    }

    static boolean borrarPersona(Request req, Response res) {
        var servicio = container.select(ServicioPersona.class)
                .get();
        res.type("application/json");

        String _id = req.params(":id");
        boolean borradoExitoso = servicio.deleteById(Integer.valueOf(_id));

        if (!borradoExitoso) {
            halt(404, "Persona no borrada");
        } else {
            res.status(200);
            res.body("Persona borrada exitosamente");
        }
        return borradoExitoso;
    }

    static boolean insertarPersona(Request req, Response res) {
        var servicio = container.select(ServicioPersona.class)
                .get();
        res.type("application/json");

        String requestBody = req.body();
        Gson gson = new Gson();
        Persona persona = gson.fromJson(requestBody, Persona.class);

        boolean insertadoExitoso = servicio.insert(persona);

        if (!insertadoExitoso) {
            halt(404, "No se puede insertar");
        } else {
            res.status(200);
            res.body("Persona insertada exitosamente");
        }

        return insertadoExitoso;
    }

    static boolean actualizarPersona(Request req, Response res) {
        var servicio = container.select(ServicioPersona.class)
                .get();
        res.type("application/json");

        String _id = req.params(":id");
        String requestBody = req.body();

        Gson gson = new Gson();
        Persona persona = gson.fromJson(requestBody, Persona.class);
        persona.setId(Integer.valueOf(_id));

        boolean actualizacionExitoso = servicio.update(persona);

        if (!actualizacionExitoso) {
            halt(404, "No se puede actualizar");
        } else {
            res.status(200);
            res.body("Persona actualizada");
        }
        return actualizacionExitoso;
    }


    public static void main(String[] args) {

        container = SeContainerInitializer
                .newInstance()
                .initialize();

        ServicioPersona servicio = container.select(ServicioPersona.class)
                .get();

        port(8080);

        configureCors();
        Gson gson = new Gson();
        get("/personas", Principal::listarPersonas, gson::toJson);
        get("/personas/:id", Principal::buscarPersona, gson::toJson);
        delete("/personas/:id", Principal::borrarPersona);
        post("/personas", Principal::insertarPersona, gson::toJson);
        put("/personas/:id", Principal::actualizarPersona, gson::toJson);
    }

    static void configureCors() {
        before((request, response) -> {
            // Configuración CORS
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept");
            response.header("Access-Control-Allow-Credentials", "true");
        });

        options("/*", (request, response) -> {
            response.status(200);
            return "OK";
        });
    }
}
