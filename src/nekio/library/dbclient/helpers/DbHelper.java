package nekio.library.dbclient.helpers;

import nekio.library.common.log.Log;

/**
 * 	Clase con metodos auxiliares para una BD generica
 * 
 * 	@author MSIS. Sinesio Ivan Carrillo Heredia
 * 	@version 25/Ene/2017
 *
**/

/* Modificaciones
 * Fecha		Modificó	Descripción
 * <dd/mm/aaaa>         <autor>		<comentarios>	
 */
public class DbHelper {
    // <editor-fold defaultstate="collapsed" desc="Static Methods">
    public static String getStatementSignature(String name, int parameters){
        StringBuilder statementSignature = new StringBuilder(name + "(");
            
        int i=0;
        while(i < parameters){
            if(i == 0)
                statementSignature.append("?");
            else
                statementSignature.append(",?");

            i++;
        }
        statementSignature.append(")");

        return statementSignature.toString();
    }
    
    public static String getStatement(String dbo, char source){
        String statement = null;
        
        switch(source){
            case 'p':
                statement = "{ call ? :=  " + dbo + "}";
                break;
            case 's':
                statement = "{ call " + dbo + "}";
                break;
            case 'f':
                statement = "{? = call " + dbo + "}";
                break;
        }
        
        Log.m("\nDatabase Calling: " + statement);
        
        return statement;
    }
    // </editor-fold>
}
