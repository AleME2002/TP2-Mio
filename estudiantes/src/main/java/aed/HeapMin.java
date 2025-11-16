package aed; 

public class HeapMin {
     private HandleEst[] heap;          // los elementos del heap son handles con estudiantes y su psocion en el heaps  
     private int tamaño;                // cuántos hay realmente (por si desencolamos gente)
     private int capacidad;             // max. estudiantes que puede tener el heap (siempre = E)

//------------------------------------------------------------------------Constructor--------------------------------------------------
    

    public HeapMin(int cantEstudiantes, HandleEst[] estudiantes) {

        this.capacidad = cantEstudiantes;                       // O(1)
        this.tamaño = cantEstudiantes;                          // O(1)

        this.heap = new HandleEst[cantEstudiantes];             // O(E)
        this.armarHeap(estudiantes);                            // O(E)

    } // Complejidad: O(E)

    private void armarHeap(HandleEst[] estudiantes) {
        for (int i = 0; i < capacidad; i++) {                   // Recorre todos los estudiantes                                // O(E)
            HandleEst h = estudiantes[i];                       // copio por alaising el handle que estaba en estduaintes       // O(1)
            heap[i] = h;                                        // Guarda el handle del estudiante en el heap                   // O(1)
            h.cambiarPosicionEnHeap(i);                         // Registra en que posicion del heap esta ese id                // O(1)
        }
    } // Complejidad: O(E)
    
    /*
    Primero se inicializan todas las estructuras internas del heap.
    Luego, en armarHeap() se cargan los IDs de los estudiantes, como todos comienzan con un examen vacío, 
    el heap ya cumple el invariante y no es necesario aplicar Heapify().
    */


//------------------------------------------------------------------------Encolar--------------------------------------------------


    public void encolar(HandleEst hEst) {
        
       
        heap[tamaño] = hEst;              // Inserta el handle del nuevo estudiante en la última posición libre del heap              // O(1)

        hEst.cambiarPosicionEnHeap(tamaño);
        tamaño++;                       // Aumenta el tamaño del heap, ya que ahora contiene un elemento más                      // O(1)
        siftUp(tamaño - 1);             // Reacomoda el nuevo elemento hacia arriba si su nota es menor que la de su padre        // O(log E)
    } // Complejiidad: O(log E)


//------------------------------------------------------------------------Desencolar--------------------------------------------------


    public Estudiante desencolar() {
        Estudiante estConPeorNota = heap[0].obtenerEstudiante();  //guardo el estudiante con peor nota(raiz del heap)                // O(1)

        intercambiar(0, tamaño - 1);        // Intercambia la raíz con el último elemento del heap                            // O(1)
        tamaño--;                           // Reduce el tamaño del heap, "eliminando" el último elemento (el peor)           // O(1)
        siftDown(0);                        // Reacomoda el nuevo elemento en la raíz para restaurar el orden del heap        // O(log E)
        return estConPeorNota;                      // Devuelve el ID del estudiante con peor nota                                    // O(1)
    } // Complejidad: O(log E)


//------------------------------------------------------------------------Actualizar nota--------------------------------------------------


    public void actualizarNotaDesdeHandle(HandleEst h) {
        int pos = h.ObtenerPosicionEnHeap();            // como los handels estan pasados por referencia.
                                                        // la nota ya esta actualizada. lo unico que nos falta,
                                                        // es actualizar la posicion del handel en el heap.

        siftUp(pos);                    // Sube al estudiante en el heap hasta restaurar el orden               // O(log E)
        siftDown(pos);                  // Baja al estudiante en el heap hasta restaurar el orden               // O(log E)
    } // Complejidad: O(log E)

    /*
    Este método actualiza la nota de un estudiante.
    Luego determina si esa nota subio o bajo para reacomodarla en el heap.
    */

//------------------------------------------------------------------------SiftUp--------------------------------------------------------------
   


    private void siftUp(int pos) { 

        while (pos > 0 && debeSubir(pos, (pos - 1) / 2)) {   // Recorre hacia arriba mientras no sea la raíz y el estudiante actual deba subir      // O(log E)
            int padre = (pos - 1) / 2;                       // Calcula el índice del estudiante padre                                              // O(1)
            intercambiar(pos, padre);                        // Intercambia el estudiante con su padre en el heap                                   // O(1)
            pos = padre;                                     // Actualiza la posición actual al nuevo índice                                        // O(1)
        }        
    }   // Complejidad total: O(log E)



//------------------------------------------------------------------------SiftDown--------------------------------------------------------------

    private void siftDown(int pos) { 
        while (pos < tamaño) {                                           // O(log E)
            int izq = 2 * pos + 1;                                       // O(1)
            int der = 2 * pos + 2;                                       // O(1)
            int menor = pos;                                             // O(1)
            if (izq < tamaño && debeSubir(izq, menor)){                   // O(1)
                menor = izq;
            }
            if (der < tamaño && debeSubir(der, menor)){                   // O(1)
                menor = der;
            }
            if (menor == pos){                                            // O(1)
                break;        
            }                                                            // Ya está en el lugar correcto
            intercambiar(pos, menor);                                    // O(1)
            pos = menor;                                                 // O(1)
            }
    }
 // Complejidad total: O(log E)


//------------------------------------------------------------------------Extras--------------------------------------------------------


    public void intercambiar(int i, int j) { 
        HandleEst hEnI = heap[i]; 
        HandleEst hEnJ = heap[j]; 
        
        // intercambiamos ids en el heap y actualizamos las posiciones
        heap[i] = hEnJ;
        heap[j] = hEnI;
        hEnI.cambiarPosicionEnHeap(j);
        hEnJ.cambiarPosicionEnHeap(i);
    }




    // USAR COMPARE TO ¿?
    private boolean debeSubir(int hijo, int padre) { 
        HandleEst hHijo = heap[hijo];                                                                           
        HandleEst hPadre = heap[padre]; 
        boolean res;

        Estudiante estHijo = hHijo.obtenerEstudiante();
        Estudiante estPadre = hPadre.obtenerEstudiante();

        if (estHijo.compareTo(estPadre)==1) {   // si el hijo tiene mayor prioridad que el padre, el hijo debe subir. 
            res = true; 
        } else {
            res = false;
        }
        return res; 
    }

    


   

    // TAL VEZ SIRVE ¿? BORRAR SI NO
    public boolean vacio() {
        return tamaño == 0;
    }

    

}