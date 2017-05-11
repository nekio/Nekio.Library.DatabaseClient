package nekio.library.dbclient.helpers;

/**
 * 	Clase con metodos auxiliares para la BD Oracle
 * 
 * 	@author MSIS. Sinesio Ivan Carrillo Heredia
 * 	@version 13/Ene/2017
 *
**/

/* Modificaciones
 * Fecha		Modificó	Descripción
 * <dd/mm/aaaa>         <autor>		<comentarios>	
 */

// <editor-fold defaultstate="collapsed" desc="Libraries">
import java.util.List;
import nekio.library.common.log.Log;
import oracle.sql.ARRAY;
import oracle.sql.Datum;
import oracle.sql.STRUCT;
// </editor-fold>

public class OracleHelper {
    // <editor-fold defaultstate="collapsed" desc="Static Methods">
    public static String arrayDump(ARRAY array){
        StringBuilder content = new StringBuilder();
        
        try {
            content.append(array.getDescriptor().getName());
            
            Datum[] datumArr = array.getOracleArray();
            for (Datum datum: datumArr)
            {
                content.append("[");
                String classType = datum.getClass().getSimpleName();
                try{
                    if(classType.equals("STRUCT")){
                        STRUCT struct = (STRUCT)datum;
                        for(Object attribute : struct.getAttributes()){
                            content.append("{");
                            
                            try{
                                content.append(attribute.toString());
                            }catch(Exception e){}
                            
                            content.append("}");
                        }
                    }else{
                        content.append(datum.stringValue());
                    }
                } catch (Exception e) {}
                
                content.append("]");
            }
        } catch (Exception e) {
            Log.e(e);
        }
        
        return content.toString();
    }
    
    public static STRUCT[] ListToArray(List<STRUCT> types){
        STRUCT[] structs = new STRUCT[types.size()];
        
        int index = 0;
        for(STRUCT type : types){
            structs[index++] = type;
        }
        
        return structs;
    }
    // </editor-fold>
}
