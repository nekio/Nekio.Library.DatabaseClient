package nekio.library.dbclient.connection;

/**
 * 	Clase puente para conexion y acceso de la Base de Datos
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
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import nekio.library.common.helpers.FileHelper;
import nekio.library.common.log.Log;
import nekio.library.dbclient.adapters.DatabaseAdapter;
import nekio.library.dbclient.adapters.Transaction;
import nekio.library.dbclient.helpers.OracleHelper;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
// </editor-fold>

public class DataAccess {   
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    private static String jndi;
    
    private static String url;
    private static String user;
    private static String pass;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public DataAccess(String jndi) {
        this.jndi = jndi;
    }
    
    public DataAccess(String url, String user, String pass) {
        this.url = url; //jdbc:oracle:thin:@10.70.10.157:1521:admi
        this.user = user;
        this.pass = pass;
    }
    // </editor-fold>
 
    // <editor-fold defaultstate="collapsed" desc="Static Methods">
    public static synchronized OracleConnection getOracleConnection(){
        OracleConnection oracleConnection = null;
        
        try {
            oracleConnection = getJndiConnection().unwrap(OracleConnection.class);
        } catch (Exception e) {
            Log.m("DataAccess - Cannot get OracleConnection");
        }
        
        return oracleConnection;
    }
    public static synchronized Connection getConnection(){
        Connection connection = null;
        
        if((jndi != null) || (url != null)){
            if(jndi == null)
                connection = getJdbcConnection();
            else
                connection = getJndiConnection();
        }else
            Log.m("DataAccess - There is not initialized yet");
        
        return connection;
    }
    
    public static synchronized Connection getJdbcConnection(){
         Connection connection = null;
        
        try {             
            Class jdbcDriverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
            Driver driver = (Driver) jdbcDriverClass.newInstance();
            DriverManager.registerDriver(driver);
            
            connection =  DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            Log.m(e.getMessage());
        }
        
        return connection;
    }
    
    public static synchronized Connection getJndiConnection(){
         Connection connection = null;
        
        try { 
            DataSource dataSource = (DataSource) FileHelper.getContext(jndi);
            connection = dataSource.getConnection();
        } catch (Exception e) {
            Log.m(e.getMessage());
        }
        
        return connection;
    }
    
    public static String executeTransaction(String databaseTransaction, Map<Integer, Object> inputParams, Map<Integer, Integer> outputParams) throws SQLException{
        String result = null;
        
        int transaction = 0;
        String message = null;
        
        DatabaseAdapter db = DataAccess.executeStatement(databaseTransaction, inputParams, outputParams);
        if(outputParams != null){
            for(Map.Entry<Integer, Integer> mapItem : outputParams.entrySet()){

                Integer elementIndex = mapItem.getKey();
                Integer elementValue = mapItem.getValue();
                
                try{
                switch(elementValue){
                    case OracleTypes.VARCHAR:
                        message = db.getStatement().getString(elementIndex);
                        break;
                    case OracleTypes.NUMBER:
                        transaction = db.getStatement().getInt(elementIndex);
                        break;
                }
                }catch(Exception e){
                    Log.e(e);
                }
            }
            
            DataAccess.close(db);
            db = null;
            
            result = transaction + " - " + message;
            Log.m(result);
        }
        
        return result;
    }
    
    public static DatabaseAdapter executeStatement(String databaseTransaction, Map<Integer, Object> inputParams, Map<Integer, Integer> outputParams) throws SQLException{
        Log.m("Starting DB Transaction: " + databaseTransaction);
        
        DatabaseAdapter db = new DatabaseAdapter();
        Connection connection = DataAccess.getConnection();
        
        StringBuilder parameters = new StringBuilder("Parameters:");
        CallableStatement statement = connection.prepareCall(databaseTransaction);
        if(inputParams != null){
            for(Map.Entry<Integer, Object> mapItem : inputParams.entrySet()){

                Integer elementIndex = mapItem.getKey();
                Object elementValue = mapItem.getValue();

                if(elementValue == null){
                    statement.setNull(elementIndex, java.sql.Types.NULL);
                }else if(elementValue instanceof String){
                    statement.setString(elementIndex, String.valueOf(elementValue));
                }else if(elementValue instanceof Integer){
                    statement.setInt(elementIndex, (Integer)elementValue);
                }else if(elementValue instanceof Long){
                    statement.setLong(elementIndex, (Long)elementValue);
                }else if(elementValue instanceof Float){
                    statement.setFloat(elementIndex, (Float)elementValue);
                }else if(elementValue instanceof Double){
                    statement.setDouble(elementIndex, (Double)elementValue);
                }else if(elementValue instanceof Blob){
                    statement.setBlob(elementIndex, (Blob) elementValue);
                }else if(elementValue instanceof Transaction){
                    statement.setString(elementIndex, ((Transaction) elementValue).get());
                }else if(elementValue instanceof Date){
                    statement.setDate(elementIndex, new java.sql.Date(((Date)elementValue).getTime()));
                }else if(elementValue instanceof STRUCT){
                    statement.setObject(elementIndex, (STRUCT) elementValue);
                }else if(elementValue instanceof ARRAY){
                    ARRAY array = (ARRAY) elementValue;
                    statement.setObject(elementIndex, array);
                    
                    parameters.append("\n\t" + elementIndex + ": " + OracleHelper.arrayDump(array)  + " (IN)");
                }

                if(!(elementValue instanceof ARRAY))
                    parameters.append("\n\t" + elementIndex + ": " + elementValue  + " (IN)");
            }
        }
        
        if(outputParams != null){
            String oracleType = null;
            for(Map.Entry<Integer, Integer> mapItem : outputParams.entrySet()){
                Integer elementIndex = mapItem.getKey();
                Integer elementValue = mapItem.getValue();

                statement.registerOutParameter(elementIndex, elementValue);

                switch(elementValue){
                    case OracleTypes.CURSOR:
                        oracleType = "CURSOR";
                        break;
                    case OracleTypes.CHAR:
                        oracleType = "CHAR";
                        break;
                    case OracleTypes.VARCHAR:
                        oracleType = "VARCHAR";
                        break;
                    case OracleTypes.NUMBER:
                        oracleType = "NUMBER";
                        break;
                    case OracleTypes.INTEGER:
                        oracleType = "INTEGER";
                        break;
                    case OracleTypes.BIGINT:
                        oracleType = "BIGINT";
                        break;
                    case OracleTypes.DECIMAL:
                        oracleType = "DECIMAL";
                        break;
                    case OracleTypes.FLOAT:
                        oracleType = "FLOAT";
                        break;
                    case OracleTypes.DOUBLE:
                        oracleType = "DOUBLE";
                        break;
                    case OracleTypes.TIMESTAMP:
                        oracleType = "TIMESTAMP";
                        break;
                    case OracleTypes.TIME:
                        oracleType = "TIME";
                        break;
                    case OracleTypes.DATE:
                        oracleType = "DATE";
                        break;
                    case OracleTypes.BLOB:
                        oracleType = "BLOB";
                        break;
                    default:
                        oracleType = "N/A";
                }

                parameters.append("\n\t" + elementIndex + ": " + oracleType + " (OUT)");
            }
        }
        
        Log.m(parameters.toString());
        statement.execute();
        
        db.setConnection(connection);
        db.setStatement(statement);
        
        return db;
    }
    
    public static void retrieveResultSetHeaders(ResultSet rs){           
        Map<String, String> columnMetadata = new HashMap<String, String>();
        try{
            ResultSetMetaData metadata = rs.getMetaData();
            int columns = metadata.getColumnCount();

            for (int i = 1; i <= columns; i++) {
                columnMetadata.put(
                    i + "." + metadata.getColumnLabel(i),
                    metadata.getColumnTypeName(i) + 
                        "(" + metadata.getPrecision(i) +
                        "," + metadata.getScale(i) + ")"
                );
            }
            
            StringBuilder headers = new StringBuilder();
            for(Map.Entry<String, String> data : columnMetadata.entrySet()){
                headers.append("\n\t[" + data.getKey() + ": " + data.getValue() + "]");
            }
            
            Log.m("Headers:" + headers);
        }catch(Exception e){}
    }
 
    public static void close(DatabaseAdapter db){
        ResultSet rs = db.getResultset();
        Statement st = db.getStatement();
        Connection c = db.getConnection();
        
        try{
            if(rs != null) {
                rs.close();
                rs = null;
            }
            
            if(st != null){
                st.close();
                st = null;
            }
            
            if(c != null){
                c.close();
                c=null;
            }
            
            Log.m("Database transaction closed");
        } catch (SQLException e) {
            Log.e(e);
        }
    }
    // </editor-fold>
}
