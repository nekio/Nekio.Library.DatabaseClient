package nekio.library.dbclient.adapters;

/**
 * 	Clase para Mapear las Tablas de Base de Datos
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
import java.util.Map;
import nekio.library.common.log.Log;
import nekio.library.dbclient.helpers.DbHelper;
// </editor-fold>

public class Entity {
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    private String tableName;
    private String selectAllStatement;
    private String insertStatement;
    private String updateStatement;
    private String deleteStatement;
    private Map<Integer, Object> parameters;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public Entity(String tableName, String idField, String fields){
        this.tableName = tableName;
        
        int fieldsCounter = fields.split(",").length;
        
        this.selectAllStatement = 
            "SELECT " + idField + "," + fields +" FROM " + tableName;
        
        this.insertStatement = 
            "INSERT INTO " + tableName +
            "(" + fields + ") " + 
            DbHelper.getStatementSignature("VALUES", fieldsCounter);
        
        this.updateStatement = 
            "TO-DO";
        
        this.deleteStatement = 
            "DELETE FROM "+ tableName + " " + 
            "WHERE " + idField + "=?";
        
        Log.m(this.toString());
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Encapsulated">
    public void setParameters(Map<Integer, Object> parameters){
        this.parameters = parameters;
    }
    
    public Map<Integer, Object> getParameters() {
        return parameters;
    }

    public String getTableName() {
        return tableName;
    }
    
    public String getSelectAllStatement() {
        return selectAllStatement;
    }
    
    public String getInsertStatement() {
        return insertStatement;
    }

    public String getUpdateStatement() {
        return updateStatement;
    }
    
    public String getDeleteStatement() {
        return deleteStatement;
    }
    // </editor-fold>
    
    @Override
    public String toString() {
        return "Entity{" + "tableName=" + tableName + ", selectAllStatement" + selectAllStatement + ", insertStatement=" + insertStatement + ", updateStatement=" + updateStatement + ", deleteStatement=" + deleteStatement + '}';
    }
}
