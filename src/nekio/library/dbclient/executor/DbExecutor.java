package nekio.library.dbclient.executor;

/**
 * 	Clase Generica que implementa el Patron DAO
 * 
 * 	@author MSIS. Sinesio Ivan Carrillo Heredia
 * 	@version 16/Feb/2017
 *
**/

/* Modificaciones
 * Fecha		Modificó	Descripción
 * <dd/mm/aaaa>         <autor>		<comentarios>	
 */

// <editor-fold defaultstate="collapsed" desc="Libraries">
import java.util.ArrayList;
import java.util.List;
import nekio.library.common.log.Log;
import nekio.library.common.model.contracts.DAO;
import nekio.library.common.model.contracts.DTO;
import nekio.library.dbclient.adapters.DatabaseAdapter;
import nekio.library.dbclient.adapters.Entity;
import nekio.library.dbclient.connection.DataAccess;
// </editor-fold>

public class DbExecutor implements DAO{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    private Entity entity;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public DbExecutor(String tableName, String idField, String fields){
        this.entity = new Entity(tableName, idField, fields);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Implementations">
    @Override
    public DTO findById(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DTO> list(String where, String orderBy) {
        List<DTO> result = new ArrayList<DTO>();
        
        DatabaseAdapter db = null;
        try {
            db = DataAccess.executeStatement(entity.getSelectAllStatement(), null, null);
            
            //TO-DO
            
            DataAccess.close(db);
        } catch (Exception e) {
            Log.e(e);
        }finally {
            entity.setParameters(null);
            db = null;
        }
        
        return result;
    }

    @Override
    public boolean insert(DTO dto){
        boolean result = false;
        
        DatabaseAdapter db = null;
        try {
            db = DataAccess.executeStatement(entity.getInsertStatement(), entity.getParameters(), null);
            
            String response = db.getStatement().toString();
            Log.m(response);
            
            DataAccess.close(db);
        } catch (Exception e) {
            Log.e(e);
        }finally {
            entity.setParameters(null);
            db = null;
        }
        
        return result;
    }

    @Override
    public boolean update(DTO dto){
        boolean result = false;
        
        DatabaseAdapter db = null;
        try {
            
        } catch (Exception e) {
            Log.e(e);
        }finally {
            entity.setParameters(null);
            db = null;
        }
        
        return result;
    }

    @Override
    public boolean delete(int id){
        boolean result = false;
        
        DatabaseAdapter db = null;
        try {
            
        } catch (Exception e) {
            Log.e(e);
        }finally {
            entity.setParameters(null);
            db = null;
        }
        
        return result;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Encapsulated">
    public Entity getEntity() {
        return entity;
    }
    // </editor-fold>
}
