package aed; 

public class Estudiante implements Comparable<Estudiante>{
    private int id;
    private int fila;
    private int columna;
    private int correctas;
    private int respondidas;
    private double nota;
    private boolean entrego;
    private boolean sospechoso;
    private Examen examen;


    /*/
    ¿QUE TENEMOS EN ESTA CLASE?
        - obtenerId() O(1)
        - obtenerFila() O(1)
        - obtenerColumna() O(1)
        - obtenerRespuestasCorrectas() O(1)
        - obtenerPreguntasRespondidas() O(1)
        - obtenerNota() O(1)
        - obtenerEstadoEntrega() O(1)
        - obtenerEstadoSospechoso() O(1)
        - obtenerExamen() O(1)
        - obtenerRespuesta(int nroPregunta) O(1)

        - marcarEntregado() O(1)
        - marcarSospechoso(boolean esSospechoso) O(1)
        - cambiarNota(double nota) O(1)

        - resolverPregunta(int nroPregunta, int respuesta, int respuestaCorrecta, int totalPreguntas) O(1)
        - copiarPregunta(int nroPregunta, Examen examenCanonico) O(1)
        - cambiarExamenCompleto(int[] nuevoExamen, int[] examenCanonico) O(R)
        - tienePreguntaSinResponder(int nroPregunta) O(1)
        - calcularNota(int[] examenCanonico) O(R)
        - actualizarNota(int totalPreguntas) O(1)

        - cantidadPreguntasQueMeFaltan(Estudiante otro) O(R)
        - encontrarPrimeraPreguntaParaCopiar(Estudiante otro) O(R)
        - copiarPrimeraPregunta(Estudiante otro, int[] examenCanonico) O(R)
        - mejorVecinoParaCopiarse(Estudiante[] vecinos) O(R)
        - cantidadPreguntasQueMeDa(Estudiante otro) O(R)
    /*/



    // CONSTRUCTOR 
    public Estudiante(int id, int cantPreguntas, int ladoAula){
        this.id = id;                                                           // O(1)                        // O(1)
        this.correctas = 0;                                                     // O(1)
        this.respondidas = 0;
        this.nota = 0.0;                                                        // O(1)
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


    // ---- PARA OBTENER (getters)

    public int obtenerId() {
        return this.id; 
    } // O(1)

    public int obtenerFila() {
        return this.fila; 
    } // O(1)

    public int obtenerColumna() {
        return this.columna; 
    } // O(1)

    public int obtenerRespuestasCorrectas() {
        return correctas; 
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
        return this.examen.obtenerRespuesta(nroPregunta);       
    } // O(1)


    // ---- PARA MODIFICAR (setters)
    public void marcarEntregado() {
        this.entrego = true; 
    } // O(1)

    public void marcarSospechoso(boolean esSospechoso) {
        this.sospechoso = esSospechoso; 
    } // O(1)

    public void cambiarNota(double nota) {
        this.nota = nota; 
    } // O(1)


    // ---- PARA OPERAR CON EL EXAMEN 

    // Resuelve una pregunta y mantiene nota actualizada en O(1) 
    public void resolverPregunta(int nroPregunta, int respuesta, int respuestaCorrecta, int totalPreguntas) {
        
        // para hacer algo, no la tiene que tener respondida 
        if (this.examen.obtenerRespuesta(nroPregunta) == -1) {
            this.examen.resolverPregunta(nroPregunta, respuesta);
            this.respondidas++;

            if (respuesta == respuestaCorrecta) {
                this.correctas++; 
            }

            this.actualizarNota(totalPreguntas);
        } 
    } // O(1)

    // copia la pregunta de otro examen
    public void copiarPregunta(int nroPregunta, Examen examenCanonico) {
        if (this.examen.tienePreguntaSinResponder(nroPregunta)) {
            int respuesta = examenCanonico.obtenerRespuesta(nroPregunta); 
            this.examen.resolverPregunta(nroPregunta, respuesta); 
        }        
    }

    // reemplaza todo el examen y recalcula todo
    public void cambiarExamenCompleto(int[] nuevoExamen, Examen examenCanonico) {
        this.examen.cambiarTodasLasRespuestas(nuevoExamen); 

        // como se cambió todo, tengo que recalcular las cosas 
        this.respondidas = 0; 
        this.correctas = 0; 

        for (int i = 0; i < nuevoExamen.length; i++) {
            if (nuevoExamen[i] != -1) {
                this.respondidas++; 
                if (nuevoExamen[i] == examenCanonico.obtenerRespuesta(i)) {
                    this.correctas++; 
                }
            }
        } 
        this.actualizarNota(nuevoExamen.length);  
    } // O(R)

    // verifica si una pregunta fue respondida o no 
    public boolean tienePreguntaSinResponder(int nroPregunta) {
        return this.examen.tienePreguntaSinResponder(nroPregunta); 
    } // O(1)

    // calcula nota comparando con las correctas (O(R)!!)
    public int calcularNota(int[] examenCanonico) {
        int totalPreguntas = examenCanonico.length;
        int respuestasCorrectas = 0; 

        for (int i = 0; i < totalPreguntas; i++) {
            if (this.examen.obtenerRespuesta(i) != -1 && this.examen.obtenerRespuesta(i) == examenCanonico[i]) {
                respuestasCorrectas++; 
            }
        }
        return (respuestasCorrectas * 100) / totalPreguntas; 
    } // O(R)

    // actualizar nota (O(1)!!)
    public void actualizarNota(int totalPreguntas) {
        this.nota = (this.correctas * 100) / totalPreguntas; 
    }


    // ---- PARA OPERAR CON VECINOS 

    // cuántas preguntas tengo sin responder que el otro tiene respondidas
    public int cantidadPreguntasQueMeFaltan(Estudiante otro) {
        return this.examen.cantidadPreguntasFaltantes(otro.obtenerExamen()); 
    } // O(R)

    // encuentra la primera pregunta para copiar 
    public int encontrarPrimeraPreguntaParaCopiar(Estudiante otro) {
        return this.examen.encontrarPrimeraPreguntaParaCopiar(otro.obtenerExamen()); 
    } // O(R)

    // copia la primera pregunta para copiar de otro estudainte
    public void copiarPrimeraPregunta(Estudiante otro, int[] examenCanonico) {
        int preguntaACopiar = this.encontrarPrimeraPreguntaParaCopiar(otro); // O(R)

        if (preguntaACopiar != -1) {
            int respuesta = otro.obtenerRespuesta(preguntaACopiar); 
            this.examen.resolverPregunta(preguntaACopiar, respuesta);
            this.respondidas++; 

            // me fijo si la respuesta copiada es correcta 
            if (this.examen.esRespuestaCorrecta(preguntaACopiar, examenCanonico[preguntaACopiar])) {
                this.correctas++; 
            }
            this.actualizarNota(examenCanonico.length);
        }
    } // O(R)

    // encontrar el mejor vecino para copiarse 
    public Estudiante mejorVecinoParaCopiarse(Estudiante[] vecinos) {
        if (vecinos.length == 0) {
            return this; 
            // si no hay vecinos, no se puede copiar de nadie 
        }
        
        Estudiante res = vecinos[0]; 
        int mayor = this.cantidadPreguntasQueMeDa(res); 

        for (int i = 1; i< vecinos.length; i++){
            int contador = this.cantidadPreguntasQueMeDa(vecinos[i]); 
            if (contador > mayor || (contador == mayor && vecinos[i].obtenerId() > res.obtenerId())) {
                mayor = contador; 
                res = vecinos[i]; 
            }
        }
        return res;
    } // O(R)

    // cuántas preguntas me puede dar un vecino
    public int cantidadPreguntasQueMeDa(Estudiante otro) {
        int contador = 0; 
        for (int j = 0; j < this.examen.cantidadPreguntas(); j++) {
            if (this.tienePreguntaSinResponder(j) && !otro.tienePreguntaSinResponder(j)) {
                contador++; 
            }
        }
        return contador; 
    } // O(R)

    //------ COMPARAR
    @Override
    public int compareTo(Estudiante otro){
        int idOriginal= this.id;
        int idOtro = otro.id;

        double notaOriginal = this.nota;
        double notaOtro = otro.nota;

        boolean estadoEntregaOriginal = this.entrego;
        boolean estadoEntregaOtro = otro.entrego;

        int res;

        if( estadoEntregaOriginal != estadoEntregaOtro){  // caso uno entrego y otro no.

            if(estadoEntregaOriginal == true){   // caso original  entrego y otro no. entonces original es menor.
                res=-1;
            }
            else{
                res =1;                         // caso original  no entrego y otro si. entonces original es mayor.
                }
            }
        else{                                               // si ambos entregar tienen iguales, desempata menor nota.

            if( notaOriginal != notaOtro){                  // caso no tienen notas iguales y tienen la misma entrego.

                if(notaOriginal > notaOtro){                // si el orginal tiene mayor nota que el otro, va a tener menor prioridad segun el criterio.
                    res=-1;
                }
                else{
                    res = 1;                                // si el original tiene menor nota que el otro, va a tener mayor prioridad segun el criterio.
                }
            }
            else{                                           // caso tienen mismo estado de entrega y misma nota

                if( idOriginal >= idOtro){
                    res=-1;                                 // si tienen la misma nota, se prioriza el de menor id.
                }
                else{
                    res=1;
                }
            }
        }
        return res;
    }   


}