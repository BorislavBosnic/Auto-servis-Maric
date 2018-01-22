/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;

import data.dto.ProdanDioDTO;
import java.util.ArrayList;

/**
 *
 * @author Aco
 */
public interface ProdanDioDAO {
    public boolean dodajProdanDio(ProdanDioDTO dio);
    public ArrayList<ProdanDioDTO> getSviProdaniDijelovi();
    public ArrayList<ProdanDioDTO> getProdaniDijelovi(Integer idDio);
    
    
}
