/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;

import data.dto.DioModelVozilaDTO;

/**
 *
 * @author Aco
 */
public interface DioModelVozilaDAO {
    public boolean dodajDioModelVozila(int idDio, int idVozila);
    public DioModelVozilaDTO getDioModelVozila(int idDio, int idVozila);
    public DioModelVozilaDTO getDioModelVozila(int idDio);
    public boolean obrisiDioModelVozila(int idDio);
}
