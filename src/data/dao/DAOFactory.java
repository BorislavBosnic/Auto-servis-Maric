package data.dao;

import data.dao.mysql.MySQLDAOFactory;

public abstract class DAOFactory {
    
    public abstract RadniNalogRadnikDAO getRadniNalogRadnikDAO();
    
    public abstract KupacDAO getKupacDAO();
    
    public abstract VoziloDAO getVoziloDAO();
    
    public abstract ModelVozilaDAO getModelVozilaDAO();
    
    public abstract ZaposleniDAO getZaposleniDAO();
    
    public abstract StatistikaDAO getStatistikaDAO();
    
    public abstract RadniNalogDAO getRadniNalogDAO();

    public abstract DioDAO getDioDAO();
    
    public abstract DioModelVozilaDAO getDioModelVozilaDAO();
    
    public abstract ProdanDioDAO getProdanDioDAO();
    
    public abstract FakturaDAO getFakturaDAO();
    
    public abstract TerminDAO getTerminDAO();
    
    public abstract RadniNalogDioDAO getRadniNalogDioDAO();
    
    public static DAOFactory getDAOFactory() {
		return new MySQLDAOFactory();
	}
   
}
