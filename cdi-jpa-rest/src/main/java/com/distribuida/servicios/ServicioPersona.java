package com.distribuida.servicios;

import com.distribuida.db.Persona;

import java.util.List;

public interface ServicioPersona {
    boolean insert(Persona p);

    List<Persona> findAll();

    Persona findById(Integer id);

    boolean deleteById(Integer id);

    boolean update(Persona persona);
}
