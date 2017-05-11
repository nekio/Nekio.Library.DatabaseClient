package nekio.library.dbclient.adapters;

/**
 * 	Enumeracion para representar las Transacciones de Bases de Datos
 * 
 * 	@author MSIS. Sinesio Ivan Carrillo Heredia
 * 	@version 30/Nov/2016
 *
**/

/* Modificaciones
 * Fecha		Modificó	Descripción
 * <dd/mm/aaaa>         <autor>		<comentarios>	
 */

public enum Transaction {
    // <editor-fold defaultstate="collapsed" desc="Values">
    INSERT("I"),
    UPDATE("U"),
    DELETE("D");
    
    private String representation;
    
    private Transaction(String representation) {
        this.representation = representation;
    }
    
    public String get() {
        return representation;
    }
    // </editor-fold>
}
