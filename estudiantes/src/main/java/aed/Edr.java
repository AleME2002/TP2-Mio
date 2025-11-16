package aed;
import java.util.ArrayList;

public class Edr {

    private HandleEst[] estudiantes;
    private Examen examenCanonico;
    private HeapMin idPorNotas;
    private int ladoAula;
    private int cantEst;
    private int cantSospechosos;
    private int cantPreguntas;
    // Usamos muchos atributos ya que no nos dieron limite de almacenamiento, pero muchos se pueden sacar de otro lado

    public Estudiante obtenerEstudiante(int id) {
        return this.estudiantes[id].obtenerEstudiante();
    }
    


//-------------------------------------------------CREAR EDR-----------------------------------------------------------------------

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico){

        this.examenCanonico = new Examen(ExamenCanonico.length);                            // O(R)
        
        this.estudiantes = new HandleEst[Cant_estudiantes];                                 // O(E)

        this.ladoAula = LadoAula;                                                           // O(1)
        this.cantEst = Cant_estudiantes;                                                    // O(1)
        this.cantSospechosos = 0;

        this.cantPreguntas = ExamenCanonico.length;

        this.examenCanonico = new Examen(ExamenCanonico.length);
        for (int p = 0; p < ExamenCanonico.length; p++) {
            this.examenCanonico.resolverPregunta(p, ExamenCanonico[p]); 
        }
        for (int i = 0; i < Cant_estudiantes; i++){                                         // O(E)
            Estudiante est = new  Estudiante(i, ExamenCanonico.length, ladoAula);           // O(R)
            HandleEst h = new HandleEst(est);                                               // O(1)                        
            this.estudiantes[i] = h;                                                        // O(1)
        }

        this.idPorNotas = new HeapMin(Cant_estudiantes,this.estudiantes);                   // O(E)

      
    } // O(R) + O(1) + O(E) + O(1) + O(E) * O(R) = O(E*R)

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){

        double[] res = new double[estudiantes.length];                                  // O(E)

        for (int i = 0; i < estudiantes.length; i++){                                   // O(E)
            res[i] = estudiantes[i].obtenerEstudiante().obtenerNota();                                               // O(1)
        }
        return res;

    } // O(E) + O(E) + O(1) = O(E)

//------------------------------------------------COPIARSE------------------------------------------------------------------------

    public void copiarse(int est){
        Estudiante[] vecinos = consguirVecinos(est);                                                                        // O(1)
        Estudiante mejorVecino = mejorVecinoParaCopiarse(estudiantes[est].obtenerEstudiante(), vecinos);                    // O(R)
        for (int i = 0; i < cantPreguntas; i++){                                                                            // O(R)
            if (estudiantes[est].obtenerEstudiante().obtenerExamen().obtenerRespuesta(i) == -1 && mejorVecino.obtenerExamen().obtenerRespuesta(i) != -1){   // O(1)
                resolver(est, i, mejorVecino.obtenerExamen().obtenerRespuesta(i));                                        // O(log E)
                break;                                                                                                      // O(1)
            }
        }
    } // O(R + log E)


    public Estudiante[] consguirVecinos(int est) {
    int estPorFila = (ladoAula + 1) / 2;                    
    ArrayList<Estudiante> vecinos = new ArrayList<>();
    if (est + 1 < estudiantes.length && estaEnRango(estudiantes[est].obtenerEstudiante(), estudiantes[est + 1].obtenerEstudiante())) {
        vecinos.add(estudiantes[est + 1].obtenerEstudiante());
    }
    if (est - 1 >= 0 && estaEnRango(estudiantes[est].obtenerEstudiante(), estudiantes[est - 1].obtenerEstudiante())) {
        vecinos.add(estudiantes[est - 1].obtenerEstudiante());
    }
    if (est - estPorFila >= 0 && estaEnRango(estudiantes[est].obtenerEstudiante(), estudiantes[est - estPorFila].obtenerEstudiante())) {
        vecinos.add(estudiantes[est - estPorFila].obtenerEstudiante());
    }
    return vecinos.toArray(new Estudiante[0]);
    }

    public Estudiante mejorVecinoParaCopiarse(Estudiante est, Estudiante[] vecinos){
        if (vecinos.length == 0) {
            return est;
        }
        Estudiante res = null;
        int mayor = -1;
        for (int i = 0; i < vecinos.length; i++){
            int contador = 0;
            for (int j = 0; j < cantPreguntas; j++){
                if (est.tienePreguntaSinResponder(j) && !vecinos[i].tienePreguntaSinResponder(j)){
                    contador++;
                }
            }
            if (contador > mayor || (contador == mayor && (res == null || vecinos[i].obtenerId() > res.obtenerId()))) {
                mayor = contador;
                res = vecinos[i];
            }
        }
        return res;
    }

    public boolean estaEnRango(Estudiante est, Estudiante vecino){
        if (est.obtenerFila() - 1 == vecino.obtenerFila() && est.obtenerColumna() == vecino.obtenerColumna()) {
            return true;
        }
        if (est.obtenerFila() == vecino.obtenerFila() && est.obtenerColumna() == vecino.obtenerColumna() + 2) {
            return true;
        }
        if (est.obtenerFila() == vecino.obtenerFila() && est.obtenerColumna() == vecino.obtenerColumna() - 2) {
            return true;
        }
        return false;
    }


//-----------------------------------------------RESOLVER----------------------------------------------------------------

    //HECHO 

    public void resolver(int estudiante, int NroEjercicio, int res) {
        Estudiante est = this.estudiantes[estudiante].obtenerEstudiante();
        int respuestaCorrecta = this.examenCanonico.obtenerRespuesta(NroEjercicio); 
        int totalPreguntas = examenCanonico.cantidadPreguntas();

        est.resolverPregunta(NroEjercicio, res, respuestaCorrecta, totalPreguntas); // O(1)
        
        this.idPorNotas.actualizarNotaDesdeHandle(this.estudiantes[estudiante]);          // Actualizo el heap con el handel de l estudiante que ya tuvo actualizada su nota // O(log E)

    } // O(log E)


//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int k, int[] examenDW) {
        int i = 0;
        Estudiante[] restaurar = new Estudiante[k];


        // desencolar los k peores
        while(i < k) {
            Estudiante e = new Estudiante(5,4,3); // creamos estudainte random, como placeHolder 
            e = idPorNotas.desencolar();
            restaurar[i] = e;
            i++;
        }

        // cambiarle el examen a los k en restaurar
        for (int j = 0; j < restaurar.length; j++){
            restaurar[j].cambiarExamenCompleto(examenDW, examenCanonico);
            idPorNotas.encolar(estudiantes[restaurar[j].obtenerId()]);
        }

        // comentario: en persona somos capaces de defenderlo mejor, hicimos ✨aliasing✨
        // la idea principal se baso en que los handles que estan adentro de Estudiantes son exactamente los mismo 
        // pues estan pasados por aliasing, que los del heap solo que en distinto orden. por ende, si modificamos la informacion 
        // en uno, se modificara en la del otro. 
        // en base a esta idea, nos sirvio para implementar consulsarDarkWEb, donde al desencolar en el heapMin idPorNotas, 
        // obtenemos un puntero de memoria (? o eso asumimos al crearlo) al estudiante. por lo tanto, si guardamos en un array, 
        // nuevos espacios de memoria que apunten a cada uno de estos punteros que apunten es esos estudiantes, con tan solo cambiar el estudiante en uno,
        // se modificaria el handle del atributo Estudiantes en EdR y, por lo tanto, lo unico que nos faltaria es encolar dicho handle con el alumno 
        // ya copiado de la darkWeb en idPorNotas.  

    }


//-------------------------------------------------ENTREGAR-------------------------------------------------------------
    //TERMIANDO
    public void entregar(int estudiante) {
        estudiantes[estudiante].obtenerEstudiante().marcarEntregado();  // O(1)
        this.idPorNotas.actualizarNotaDesdeHandle(estudiantes[estudiante]); // O(log (E))
    }

//-----------------------------------------------------CORREGIR---------------------------------------------------------

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
            
            Estudiante est  = idPorNotas.desencolar();                           // desencolo ids por nota al id con menor nota - O(log(E)) como maximo.
                                                                                // obtengo el objeto de clase Estudiante corespondiente al id - O(1)

            NotaFinal notaFinalEst = new NotaFinal(est.obtenerNota(), est.obtenerId());   // creo la nota final del estudiante con sus datos de sus atributos - O(1)

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
            if(this.estudiantes[id].obtenerEstudiante().obtenerEstadoSospechoso()==false){                   // si el estudiante no es sospechoso de copiarse,
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

            this.idPorNotas.encolar(estudiantes[k]);                                 // encolo uno a uno cada estudiante con su id -O(log(E)) como maximo
            k = k + 1;
        }
    }



//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        int[][] grilla = armarGrillaDeRespuestas();             // O(E*R)
        double umbral = (cantEst - 1) * 0.25;                   // O(1)
        clasificarSospechososSegunCriterio(grilla, umbral);     // O(E*R)
        int res[] = armarListaDeSospechosos();                  // O(E)
        this.cantSospechosos = res.length;                      // O(1)
        return res;                                             // O(1)
    } //O(E*R) 

    public int[][] armarGrillaDeRespuestas() {
        int[][] grilla = new int[this.cantPreguntas][10]; 

        for (int i = 0; i < cantEst; i++) { // O(E)
            for (int j = 0; j < this.cantPreguntas; j++) { // O(R)
                int respuesta = estudiantes[i].obtenerEstudiante().obtenerRespuesta(j); 
                if (respuesta == -1) {
                    continue; 
                } 
                grilla[j][respuesta] += 1; 
            }
        }
        return grilla; 
    } // O(E*R)

    public void clasificarSospechososSegunCriterio(int[][] grilla, double umbral) {
        for (int i = 0; i < cantEst; i++) { // O(E)
            boolean esSospechoso = true; 

            for (int j = 0; j < cantPreguntas; j++) { // O(R)
                int respuesta = estudiantes[i].obtenerEstudiante().obtenerRespuesta(j); 
                if (respuesta == -1) {
                    continue; 
                }

                if (grilla[j][respuesta] -1 < umbral) {
                    esSospechoso = false; 
                    break; 
                }
            }

            if (estudiantes[i].obtenerEstudiante().obtenerPreguntasRespondidas() == 0) {
                esSospechoso = false; 
            }

            estudiantes[i].obtenerEstudiante().marcarSospechoso(esSospechoso); 
        }
    } // O(E*R)

    public int[] armarListaDeSospechosos() {
        int tramposos = contarSospechosos(); // O(E)
        int[] res = new int[tramposos]; 
        int k = 0; 

        for (int i = 0; i<cantEst;i++){
            if (estudiantes[i].obtenerEstudiante().obtenerEstadoSospechoso()) {
                res[k] = i; 
                k++;
            }
        }
        return res; 
        // O(E)
    }

    public int contarSospechosos() {
        int contador = 0; 
        
        for (int i = 0; i < cantEst; i++) {
            if (estudiantes[i].obtenerEstudiante().obtenerEstadoSospechoso()) {
                contador++; 
            }
        }
        return contador; 
    } // O(E)
}