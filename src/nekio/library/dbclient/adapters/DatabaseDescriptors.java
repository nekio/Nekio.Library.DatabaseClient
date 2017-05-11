package nekio.library.dbclient.adapters;

/**
 * 	Clase de Acceso a Datos (DAO)
 * 
 * 	@author MSIS. Sinesio Ivan Carrillo Heredia
 * 	@version 07/Dic/2016
 *
**/

/* Modificaciones
 * Fecha		Modificó	Descripción
 * <dd/mm/aaaa>	<autor>		<comentarios>	
 */

// <editor-fold defaultstate="collapsed" desc="Libraries">
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nekio.library.common.log.Log;
import nekio.library.common.model.contracts.DbTypes;
import nekio.library.dbclient.connection.DataAccess;
import nekio.library.dbclient.helpers.OracleHelper;
import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
// </editor-fold>

public class DatabaseDescriptors {
    // <editor-fold defaultstate="collapsed" desc="ARRAY - Collections">
    public static ARRAY getCollection(List<Map> records, DbTypes.Object descriptor, DbTypes.Collection collection){
        ARRAY oracleArray = null;
        
        try{
            OracleConnection c = DataAccess.getOracleConnection();
            List<STRUCT> tipos = DatabaseDescriptors.getTypes(c, descriptor, records);
            oracleArray = DatabaseDescriptors.getCollectionTypes(c, collection, tipos);
         }catch(Exception e){
             Log.e(e);
         }
        
        return oracleArray;
    }
    
    public static ARRAY getCollectionTypes(OracleConnection connection, DbTypes.Collection descriptor, List<STRUCT> types){
        ARRAY oracleArray = null;
        try{            
            ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor(descriptor.getName(), connection);
            oracleArray = new ARRAY(arrayDescriptor, connection, OracleHelper.ListToArray(types));
            
            Log.m("\n" + "ORACLE COLLECTION - " + OracleHelper.arrayDump(oracleArray));
        }catch(Exception e){
            Log.e(e);
        }
        
        return oracleArray;
    }
    
    public static ARRAY getCollectionTypes(OracleConnection connection, DbTypes.Collection descriptor, String[] values){
        ARRAY oracleArray = null;
        try{            
            String descriptorName = descriptor.getName();
            
            ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor(descriptorName, connection);
            oracleArray = new ARRAY(arrayDescriptor, connection, values);
            
            Log.m("ORACLE ARRAY - " + descriptorName + ":" + OracleHelper.arrayDump(oracleArray));
        }catch(Exception e){
            Log.e(e);
        }
        
        return oracleArray;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="STRUCT - Type">
    public static List<STRUCT> getTypes(OracleConnection connection, DbTypes.Object descriptor, List<Map> records){
        List<STRUCT> result = new ArrayList<STRUCT>();
        
        Log.m("ORACLE STRUCT - " + descriptor);
        for(Map values : records){
            result.add(DatabaseDescriptors.getType(connection, descriptor, values));
            Log.m("\t" + values.toString());
        }
        
        return result;
    }
    
    public static STRUCT getType(OracleConnection connection, DbTypes.Object descriptor, Map values){
        STRUCT struct = null;
        
        try {            
            StructDescriptor itemDescriptor = StructDescriptor.createDescriptor(descriptor.getName(), connection);
            struct = new STRUCT(itemDescriptor, connection, values);
        } catch (Exception e) {
            Log.e(e);
        }
        
        return struct;
    }
    
    public static STRUCT getType(OracleConnection connection, DbTypes.Object descriptor, String[] values){
        STRUCT struct = null;
        
        try {            
            StructDescriptor itemDescriptor = StructDescriptor.createDescriptor(descriptor.getName(), connection);
            struct = new STRUCT(itemDescriptor, connection, values);
        } catch (Exception e) {
            Log.e(e);
        }
        
        return struct;
    }
    // </editor-fold>
}
