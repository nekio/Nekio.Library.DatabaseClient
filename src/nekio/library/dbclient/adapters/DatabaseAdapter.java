package nekio.library.dbclient.adapters;

/**
 * 	Clase Adaptadora de Objetos de la Base de Datos
 * 
 * 	@author MSIS. Sinesio Ivan Carrillo Heredia
 * 	@version 06/Nov/2016
 *
**/

/* Modificaciones
 * Fecha		Modificó	Descripción
 * <dd/mm/aaaa>         <autor>		<comentarios>	
 */
// <editor-fold defaultstate="collapsed" desc="Libraries">
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
// </editor-fold>

public class DatabaseAdapter {
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    private Connection connection;
    private CallableStatement statement;
    private ResultSet resultset;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Encapsulated">
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public CallableStatement getStatement() {
        return statement;
    }

    public void setStatement(CallableStatement statement) {
        this.statement = statement;
    }
    
    public ResultSet getResultset() {
        return resultset;
    }

    public void setResultset(ResultSet resultset) {
        this.resultset = resultset;
    }
    // </editor-fold>
}
