package vo;

import java.util.List;

/**
 * Objeto que representa una asignatura con los meses donde un alumno tine apercibimientos
 * @author mmbernal
 *
 */
public class TutorAsignatura{
	private String nombre;
    private List<String> meses;

    public TutorAsignatura(String nombre, List<String> meses) {
        this.nombre = nombre;
        this.meses = meses;
    }

    public TutorAsignatura() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getMeses() {
        return meses;
    }

    public void setMeses(List<String> meses) {
        this.meses = meses;
    }

    public String getMesesString() {
        String mes = "";
        for (int i = 0; i < meses.size(); i++){
            mes += meses.get(i);
            if (i < meses.size()-1) {
                mes += ", ";
            }
        }
        return mes;
    }
}
