/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dto;

/**
 *
 * @author Aco
 */
public class DioModelVozilaDTO {
    private int idDio;
    private int idVozila;
    
    public DioModelVozilaDTO(int idDio, int idVozila){
        this.idDio = idDio;
        this.idVozila = idVozila;
    }
    
    public void setIdDio(int id){
        this.idDio = id;
    }
    public void setIdVozila(int id){
        this.idVozila = id;
    }
    
    public int getIdDio(){
        return this.idDio;
    }
    public int getIdVozila(){
        return this.idVozila;
    }
}
