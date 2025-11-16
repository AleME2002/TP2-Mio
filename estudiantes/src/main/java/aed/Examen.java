package aed;

// Crear Examen O(R)

public class Examen {
    int[] preguntas;

//------------------------------------------------------------------------Constructor----------------------------------------------------------

    public Examen(int cantPreguntas){
        this.preguntas = new int[cantPreguntas];                                        // O(R)
        for (int i = 0; i < cantPreguntas; i++){                                        // O(R)
            this.preguntas[i] = -1;                                                     // O(1)
        }
    } // Complejidad: O(R)

    public int[] obtenerExamen(){
        return this.preguntas;
    }

    public void reolverPregunta()

//------------------------------------------------------------------------Preguntas faltantes--------------------------------------------------

    public int cantidadPreguntasFaltantes(Examen vecino){
        int faltantes = 0;                                                                    // O(1)
        for (int i = 0; i < this.preguntas.length; i++){                                // O(R)
            if (this.preguntas[i] == -1 && vecino.preguntas[i] != -1)                   // O(1)
                faltantes += 1;                                                               // O(1)
        }
        return faltantes;                                                                     // O(1)
    }// Complejidad: O(R)

//------------------------------------------------------------------------Copiar pregunta-------------------------------------------------------

//Esta mal. chequear
    public void copiarPrimeraPregunta(Examen vecino){    
        for (int i = 0; i < this.preguntas.length; i++) {
            if (this.preguntas[i] == -1 &&vecino.preguntas[i] != -1) {
                this.preguntas[i] = vecino.preguntas[i]; 
            }
        }
        
        
        /*  // O(1)
        while (encontre == false){                                                      // O(R)
            for (int i = 0; i < this.preguntas.length; i++){                            // O(R)
                if (this.preguntas[i] == -1 && vecino.preguntas[i] != -1){              // O(1)
                    this.preguntas[i] = vecino.preguntas[i];                            // O(1)
                    encontre = true;                                                    // O(1)
                }
            }
        }*/
    }
    

//----------- resolverPregunta 
    public void resolverPregunta(int nroPregunta, int respuesta) {
        preguntas[nroPregunta] = respuesta;
    }

}