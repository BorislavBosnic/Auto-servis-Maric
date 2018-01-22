package data.dao.mysql;

import data.dao.DAOFactory;
import data.dao.DioDAO;
import data.dao.DioModelVozilaDAO;
import data.dao.KupacDAO;
import data.dao.ModelVozilaDAO;
import data.dao.ProdanDioDAO;
import data.dao.RadniNalogDAO;
import data.dao.StatistikaDAO;
import data.dao.VoziloDAO;
import data.dao.ZaposleniDAO;

public class MySQLDAOFactory extends DAOFactory {

    @Override
    public KupacDAO getKupacDAO() {
        return new MySQLKupacDAO();
    }    
    
    @Override
    public VoziloDAO getVoziloDAO(){
        return new MySQLVoziloDAO();
    }

    @Override
    public ModelVozilaDAO getModelVozilaDAO() {
        return new MySQLModelVozilaDAO();
    }
    
    @Override
    public ZaposleniDAO getZaposleniDAO(){
        return new MySQLZaposleniDAO();
    }
    
    @Override
    public StatistikaDAO getStatistikaDAO(){
        return new MySQLStatistikaDAO();
    }
    
    @Override
    public RadniNalogDAO getRadniNalogDAO(){
        return new MySQLRadniNalogDAO();
    }
    
    @Override
    public DioDAO getDioDAO(){
        return new MySQLDioDAO();
    }
    
    @Override 
    public DioModelVozilaDAO getDioModelVozilaDAO(){
        return new MySQLDioModelVozilaDAO();
    }
    
    @Override
    public ProdanDioDAO getProdanDioDAO(){
        return new MySQLProdanDioDAO();
    }
}
