package data.dao.mysql;

import data.dao.DAOFactory;
import data.dao.KupacDAO;
import data.dao.ModelVozilaDAO;
import data.dao.VoziloDAO;

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
}
