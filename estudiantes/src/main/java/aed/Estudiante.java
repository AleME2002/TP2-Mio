package aed;

// Crear estudiante O(R)

public class Estudiante {
    int id;
    int fila;
    int columna;
    int correctas;
    int respondidas;
    double nota;
    boolean entrego;
    boolean sospechoso;
    Examen examen;
    HeapMin.HandleEst handle;


//------------------------------------------------------------------------Constructor--------------------------------------------------


    public Estudiante(int id, int cantPreguntas, int ladoAula){
        this.id = id;                                                           // O(1)                        // O(1)
        this.correctas = 0;                                                     // O(1)
        this.respondidas = 0;
        this.nota = 0.0;                                                          // O(1)
        this.entrego = false;                                                   // O(1)
        this.sospechoso = true;                                                 // O(1)
        this.examen = new Examen(cantPreguntas);                                // O(R)
        calcularPosicion(ladoAula);                                             // O(1)
    } // Complejidad: O(R)

    private void calcularPosicion(int ladoAula) {
        int estudiantesPorFila = (ladoAula + 1) / 2;
        this.fila = id / estudiantesPorFila;
        this.columna = (id % estudiantesPorFila) * 2;
    } // O(1)

    //----Gets----
    public HeapMin.HandleEst obtenerHandle(){
        return this.handle;
    }

    public int obtenerId() {
        return id; 
    } // O(1)

    public int obtenerRespuestasCorrectas() {
        return correctas; 
    } // O(1)

    public int obtenerFila() {
        return fila; 
    } // O(1)

    public int obtenerColumna() {
        return columna; 
    } // O(1)

    public int obtenerPreguntasRespondidas() {
        return respondidas; 
    } // O(1)

    public double obtenerNota() {
        return nota; 
    } // O(1)

    public boolean obtenerEstadoEntrega() {
        return entrego; 
    } // O(1)

    public boolean obtenerEstadoSospechoso() {
        return sospechoso; 
    } // O(1)

    public Examen obtenerExamen() {
        return examen; 
    } // O(1)

    public int obtenerRespuesta(int nroPregunta) {
        return this.examen.preguntas[nroPregunta];       
    } // O(1)


    //----Sets----

    public void asignarHandle(HeapMin.HandleEst h){
        this.handle = h;
    }

    public void entregar() {
        this.entrego = true; 
    }

    public void marcarSospechoso(boolean esSospechoso) {
        this.sospechoso = esSospechoso; 
    }

    public void cambiarNota(double nota) {
        this.nota = nota; 
    } // O(1)

    public void resolverPregunta(int preg, int res){
        this.examen.preguntas[preg] = res;
    }

}