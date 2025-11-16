package aed;
import java.util.ArrayList;
import java.util.Arrays;

public class Edr {
    private Estudiante[] estudiantes;
    private Examen examenCanonico;
    private HeapMin idPorNotas;
    private HeapMin estEnAulaPorNotas;
    private int ladoAula;
    private int cantEst;
    private int cantEntregados;
    private int cantSospechosos;
    private int cantPreguntas;
    // Usamos muchos atributos ya que no nos dieron limite de almacenamiento, pero muchos se pueden sacar de otro lado

    // Se puede modificar si agregamos cosas
    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico){
        this.examenCanonico = new Examen(ExamenCanonico.length);                            // O(R)
        
        this.estudiantes = new Estudiante[Cant_estudiantes];                                // O(E)
        this.ladoAula = LadoAula;                                                           // O(1)
        this.cantEst = Cant_estudiantes;                                                    // O(1)
        this.cantEntregados = 0;                                                            // O(1)
        this.cantSospechosos = 0;
        this.cantPreguntas = ExamenCanonico.length;
        this.idPorNotas = new HeapMin(Cant_estudiantes);                                    // O(E)
        this.estEnAulaPorNotas = new HeapMin(Cant_estudiantes);                             // O(E)
        this.examenCanonico = new Examen(ExamenCanonico.length);
        for (int p = 0; p < ExamenCanonico.length; p++) {
            this.examenCanonico.preguntas[p] = ExamenCanonico[p];
        }
        for (int i = 0; i < Cant_estudiantes; i++){                                         // O(E)
            this.estudiantes[i] = new Estudiante(i, ExamenCanonico.length, ladoAula);       // O(R)
        }
    } // O(R) + O(1) + O(E) + O(1) + O(E) * O(R) = O(E*R)
    

//------------------------------------------------------------------------NOTAS--------------------------------------------------------------------------

    // Terminado
    public double[] notas(){
        double[] res = new double[estudiantes.length];                                  // O(E)
        for (int i = 0; i < estudiantes.length; i++){                                   // O(E)
            res[i] = estudiantes[i].nota;                                               // O(1)
        }
        return res;
    } // O(E) + O(E) + O(1) = O(E)


//------------------------------------------------------------------------COPIARSE------------------------------------------------------------------------

    public void copiarse(int est){
        Estudiante[] vecinos = consguirVecinos(est);                                                    // O(1)
        Estudiante mejorVecino = mejorVecinoParaCopiarse(estudiantes[est], vecinos);                    // O(R)
        for (int i = 0; i < cantPreguntas; i++){                                                        // O(R)
            if (estudiantes[est].examen.preguntas[i] == -1 && mejorVecino.examen.preguntas[i] != -1){   // O(1)
                resolver(est, i, mejorVecino.examen.preguntas[i]);                                      // O(log E)
                break;                                                                                  // O(1)
            }
        }
    } // O(R + log E)
    

    public Estudiante[] consguirVecinos(int est) {
    int estPorFila = (ladoAula + 1) / 2;                    
    ArrayList<Estudiante> vecinos = new ArrayList<>();
    if (est + 1 < estudiantes.length && estaEnRango(estudiantes[est], estudiantes[est + 1])) {
        vecinos.add(estudiantes[est + 1]);
    }
    if (est - 1 >= 0 && estaEnRango(estudiantes[est], estudiantes[est - 1])) {
        vecinos.add(estudiantes[est - 1]);
    }
    if (est - estPorFila >= 0 && estaEnRango(estudiantes[est], estudiantes[est - estPorFila])) {
        vecinos.add(estudiantes[est - estPorFila]);
    }
    return vecinos.toArray(new Estudiante[0]);
    }

    public Estudiante mejorVecinoParaCopiarse(Estudiante est, Estudiante[] vecinos){
        if (vecinos.length == 0) {
            return est;
        }
        Estudiante res = vecinos[0];                                                            // O(1)
        int mayor = 0;                                                                          // O(1)
        for (int i = 1; i < vecinos.length; i++){                                               // O(1) (Como maximo tiene 3 vecinos)
            int contador = 0;                                                                   // O(1)
            for (int j = 0; j < cantPreguntas; j++){                                            // O(R)
                if (est.examen.preguntas[j] == -1 && vecinos[i].examen.preguntas[j] != -1){     // O(1)
                    contador += 1;                                                              // O(1)
                }
            }
            if (contador > mayor || (contador == mayor && vecinos[i].id > res.id)) {            // O(1)
            mayor = contador;                                                                   // O(1)
            res = vecinos[i];                                                                   // O(1)
            }
        }
        return res;                                                                             // O(1)
    } // O(R)

    public boolean estaEnRango(Estudiante est, Estudiante vecino){
        if (est.fila - 1 == vecino.fila && est.columna == vecino.columna) {
            return true;
        }
        if (est.fila == vecino.fila && est.columna == vecino.columna + 2) {
            return true;
        }
        if (est.fila == vecino.fila && est.columna == vecino.columna - 2) {
            return true;
        }
        return false;
    }


//------------------------------------------------------------------------RESOLVER------------------------------------------------------------------------

    // Terminado
    public void resolver(int estudiante, int NroEjercicio, int res) {
        estudiantes[estudiante].examen.preguntas[NroEjercicio] = res;                                 // Cambia la respuesta del estudiante en la pregunta      // O(1)
        estudiantes[estudiante].respondidas += 1;
        if (examenCanonico.preguntas[NroEjercicio] == res){                                           // Se fija si le respuesta esta bien                      // O(1)
            estudiantes[estudiante].correctas += 1;                                                   // Si esta bien actualizo las respuestas correctas        // O(1)
        }
        estudiantes[estudiante].nota = estudiantes[estudiante].correctas * 10;                        // Actualizo la nota                                      // O(1)
        this.idPorNotas.actualizarNotaDesdeHandle(estudiante, estudiantes[estudiante].nota);          // Actualizo el heap                                      // O(log E)
        this.estEnAulaPorNotas.actualizarNotaDesdeHandle(estudiante, estudiantes[estudiante].nota);                                                             // O(log(E))
    } // O(log E)


//------------------------------------------------------------------------CONSULTAR DARK WEB--------------------------------------------------------------

    // Falta terminar
    public void consultarDarkWeb(int k, int[] examenDW) {
        int notaNueva = calcularNota(examenDW, this.examenCanonico.preguntas);
        int i = 0;
        if(k <= this.cantEst){                                      // Si los que consultanDW son menos que la cant de est
            if(k <= cantEst - cantEntregados){                      // Si hay menos personas que cantidad de entregados
                while(i < k){                                        // O(K)
                    int e = estEnAulaPorNotas.desencolar();                             // O(log E) como max
                    if(estudiantes[e].entrego == false){
                        estudiantes[e].examen.preguntas = examenDW;                         // O(1)
                        estudiantes[e].nota = notaNueva;                                    // O(1)
                        estEnAulaPorNotas.encolar(e);                                       // O(log E)
                        idPorNotas.actualizarNotaDesdeHandle(e, estudiantes[e].nota);       // O(log E)
                        i ++;
                    }
                }
            }
            else{                                                   // Si k es mayor a cantEst - cantEntregados
                intercambiarExamen(cantEst - cantEntregados, notaNueva, examenDW);
            }
        }
        else{                                                       // Si todos los estudiantes consultanDW
            intercambiarExamen(cantEst, notaNueva, examenDW);
        }
    }

    public int calcularNota(int[] otro, int[] canonico){
        int nota = 0;                                                           // O(1)
        for (int i = 0; i < canonico.length; i++){                              // O(R)
            if (otro[i] == canonico[i]){                                        // O(1)
                nota += 10;                                                     // O(1)
            }                   
        }
        return nota;
    } // Complejidad: O(R)

    public void intercambiarExamen(int hasta, int notaNueva, int[] examenDW){
        for (int i = 0; i < hasta; i++){                                        // O(K)
            int e = estEnAulaPorNotas.desencolar();                             // O(log E) como max
            estudiantes[e].examen.preguntas = examenDW;                         // O(1)
            estudiantes[e].nota = notaNueva;                                    // O(1)
            estEnAulaPorNotas.encolar(e);                                       // O(log E)
            idPorNotas.actualizarNotaDesdeHandle(e, estudiantes[e].nota);       // O(log E)
        }
    }


//------------------------------------------------------------------------ENTREGAR------------------------------------------------------------------------

    // Terminado
    public void entregar(int estudiante) {
        estudiantes[estudiante].entrego = true;   // Cambia el atributo entrego
        this.cantEntregados += 1;                 // Incrementa cantEntregados
    }


//------------------------------------------------------------------------CORREGIR------------------------------------------------------------------------

    // Terminado
    public NotaFinal[] corregir() {
        
        // lleno el array de res con notasfinal de estudiantes, 
        // ordenadas de menor a mayor y desempatadas por menor id
        NotaFinal[]  res = notasFinalesEnOrdenInverso();                // O(E*log(E))

        // doy vuelta el array de res para que me quede en el orden correcto,
        // notas ordenadas de mayor a menor, desmpatada por mayor id
        NotaFinal[] resOrdenCorecto = invertirOrden(res);               // O(E)
        
        // saco del array de notaFinal, los estudiantes que se copiaron
        NotaFinal[] resOrdenCorectoSinCopiones = sacarSospechosos(resOrdenCorecto); // O(E), en el peor caso (ninguno se copio)
     
        // restauro idspornota
        restuararIdPorNotasCompleto();                                  // O(E*log(E))

        // devuelvo las notas de los examenes de los estudiantes que 
        //no se hayan copiado ordenada por NotaFinal.nota de forma
        //decreciente. En caso de empate, se desempata por mayor 
        //NotaFinal.id de estudiante
        return resOrdenCorectoSinCopiones;                             // - o(1)
 

    }

    private NotaFinal[] notasFinalesEnOrdenInverso(){                   // O(E*log(E))

        // lleno el array de res con notasfinal de estudiantes, ordenadas de menor a mayor y desempatadas por menor id

        NotaFinal[] res = new NotaFinal[(this.cantEst)];                // - O(E)
        int i = 0;                                                      // - O(1)

        while(i<this.cantEst){                                          // repito el cuerpo E-veces - O(E*(cuerpo))
            
            int id = idPorNotas.desencolar();                           // desencolo ids por nota al id con menor nota - O(log(E)) como maximo.
            Estudiante est = this.estudiantes[id];                      // obtengo el objeto de clase Estudiante corespondiente al id - O(1)

            NotaFinal notaFinalEst = new NotaFinal(est.nota, est.id);   // creo la nota final del estudiante con sus datos de sus atributos - O(1)

            res[i] = notaFinalEst;                                      // se agrega la notaFinal del estudiante con menor nota en la posicion 
                                                                        // i-esima del res -  O(1)
            i = i + 1;                                                  // aumeento en 1 el iterador -  O(1)

        }

        return res;                                                     // - O(1)
    }

    private NotaFinal[] invertirOrden( NotaFinal[] res ){               // - O(E)

        // da vuelta el orden de un array siendo E la longitud de dicho array

        NotaFinal[] resOrdenCorecto = new NotaFinal[(res.length)];      // - O(E)
        int j = 0;                                                      // - O(1)

        while(j<res.length){                                          // repito el cuerpo E-veces - O(E*(cuerpo))

            resOrdenCorecto[j]=res[res.length-1-j];                     // se invierte el orden de la ubicacion de elementos
                                                                        // lo que estaba ultimo en un array, ahota esta primero en el otro - O(1)
            j = j + 1;                                                  // aumento en 1 el itreador j - O(1)

        }

        return resOrdenCorecto;                                         // - O(1)
    }

    private NotaFinal[] sacarSospechosos(NotaFinal[] conSospechosos){     // - O(E) , en el peor caso

        //saco del array los estudiantes que se copiaron.
        //para eso a todos los estudiantes que no se copiaron,
        // los agrego a otro array

        int cantNoSospechosos = this.cantEst-this.cantSospechosos;        // cantidad de alumnos que no son sopechosos de copiarse - O(1)
        NotaFinal[] sinSospechosos = new NotaFinal[cantNoSospechosos];    // en el peor caso esto es de longitud E - O(E)

        int j = 0;                                                        // iterador sobre el array de notafinal de estudiantes sin copiones - O(1)
        int i = 0;                                                        // iterador sobre el array de NotaFinal de estudiantes con copiones - O(1)

        while(i<conSospechosos.length){                                   // itero sobla el array de NotaFinal de estudiantes  
            int id = conSospechosos[i]._id;                         
            if(this.estudiantes[id].sospechoso==false){                   // si el estudiante no es sospechoso de copiarse,
                sinSospechosos[j]=conSospechosos[i];                      // lo agrego al array de notaFinal de estudiantes que no se copiaron - O(1)                j=j+1;
                j = j + 1;
            }

            i=i+1;
        }                                                                 // la complejidad de este while en el peor caso es - O(E)

        return sinSospechosos;                                            // - O(1)

    }   

    private void restuararIdPorNotasCompleto(){                        // O(E*log(E))
        // apartir de Estudiantes, se  llena idpornotas con
        // todos los elementos de Estduiantes, se asume que 
        // id por nota esta vacio al momento de este proc.

        int k = 0;                                                      // inicializo el iterador - O(1)

        while (k<this.cantEst){                                         // repito el cuerpo E-veces - O(E*(cuerpo))

            this.idPorNotas.encolar(k);                                 // encolo uno a uno cada estudiante con su id -O(log(E)) como maximo
            k = k + 1;
        }
    }



//------------------------------------------------------------------------CHEQUEAR COPIAS-----------------------------------------------------------------

    // Terminado
    public int[] chequearCopias() {
        int[][] grilla = new int[this.cantPreguntas][10];               // Creo la grilla de calificaciones                     // O(R)
        int tramposos = 0;                                                                                                      // O(1)
        for (int i = 0; i < cantEst; i++){                                                                                      // O(E)
            for (int j = 0; j < this.cantPreguntas; j++){                                                                       // O(R)
                int respuesta = estudiantes[i].examen.preguntas[j];     // Guardo la respuesta                                  // O(1)
                if (respuesta == -1){                                   // Si no respondio la pregunta, la saltea               // O(1)
                    continue;                                                                                                   // O(1)
                }
                grilla[j][respuesta] +=1;                               // Sumo 1 a la posicion de la respuesta                 // O(1)
            } 
        } // O(E) * O(R) = O(E*R)
        double umbral = (cantEst - 1) * 0.25;                           // Guardo el 25% de los alumnos                         // O(1)
        for (int i = 0; i < cantEst; i++){                                                                                      // O(E)
            for (int j = 0; j < cantPreguntas; j++){                                                                            // O(R)
                int respuesta = estudiantes[i].examen.preguntas[j];     // Guardo la respuesta                                  // O(1)
                if (respuesta == -1){                                   // Si no respondio la pregunta, la saltea               // O(1)
                    continue;                                                                                                   // O(1)
                }
                if (grilla[j][respuesta] - 1 < umbral) {                // Me fijo si respondio por debajo del 25%              // O(1)
                    estudiantes[i].sospechoso = false;                  // Si lo hizo, quiere decir que no se copio             // O(1)
                    break;                                                                                                      // O(1)
                } 
            } 
            if (estudiantes[i].respondidas == 0){
                estudiantes[i].sospechoso = false;
            }
            if (estudiantes[i].sospechoso){                                                                                     // O(1)
                tramposos += 1;                                                                                                 // O(1)
            } 
        } // O(E) * O(R) = O(E*R)
        int[] res = new int[tramposos];                                                                                         // O(1)
        int k = 0;                                                                                                              // O(1)
        for (int i = 0; i < cantEst; i++){                                                                                      // O(E)
            if (estudiantes[i].sospechoso){                             // Si es tramposo lo meto a la lista de tramposos       // O(1)
                res[k] = i;                                                                                                     // O(1)
                k++;                                                                                                            // O(1)
            } 
        } // O(E)
        this.cantSospechosos=res.length;
        return res;                                                                                                             // O(1)
    }// O(E*R) + O(E*R) + O(E) = O(E*R)
}
