package aed;

import aed.Edr.HandleEst;

public class HeapMin {
     private HandleEst[] heap;         // ids de estudiantes  
     private int[] posEnHeapDeHandel;
     private int tamaño;         // cuántos hay realmente (por si desencolamos gente)
     private int capacidad;      // max. estudiantes que puede tener el heap (siempre = E)





//------------------------------------------------------------------------Constructor--------------------------------------------------
    

    public HeapMin(int cantEstudiantes, HandleEst[] estudiantes) {
        this.capacidad = cantEstudiantes;                       // O(1)
        this.tamaño = cantEstudiantes;                          // O(1)
        this.heap = new HandleEst[cantEstudiantes];                // O(E)
        this.posEnHeapDeHandel = new int[cantEstudiantes];
        this.armarHeap(estudiantes);                                       // O(E)
    } // Complejidad: O(E)

    private void armarHeap(HandleEst[] estudiantes) {
        for (int i = 0; i < capacidad; i++) {                   // Recorre todos los estudiantes                        // O(E)
            HandleEst h = estudiantes[i];        // creo el handles con mi estudiante                    // O(1)
            heap[i] = h;                                        // Guarda el handle del estudiante en el heap           // O(1)
            h.cambiarPosicionEnHeap(i);                         // Registra en que posicion del heap esta ese id        // O(1)
            this.posEnHeapDeHandel[h.obtenerId()]=i;                        // registro la posciicon donde esta el handle en el heap.
        }
    } // Complejidad: O(E)
    
    /*
    Primero se inicializan todas las estructuras internas del heap.
    Luego, en armarHeap() se cargan los IDs de los estudiantes, como todos comienzan con un examen vacío, 
    el heap ya cumple el invariante y no es necesario aplicar Heapify().
    */


//------------------------------------------------------------------------Encolar--------------------------------------------------


    public void encolar(Estudiante est) {
        
        HandleEst h = new HandleEst(est);
        heap[tamaño] = h;              // Inserta el handle del nuevo estudiante en la última posición libre del heap              // O(1)

        h.cambiarPosicionEnHeap(tamaño);
        this.posEnHeapDeHandel[est.obtenerId()] = tamaño; // Registra en qué posición del heap quedó almacenado ese estudiante    // O(1)

        tamaño++;                       // Aumenta el tamaño del heap, ya que ahora contiene un elemento más                      // O(1)
        siftUp(tamaño - 1);             // Reacomoda el nuevo elemento hacia arriba si su nota es menor que la de su padre        // O(log E)
    } // Complejiidad: O(log E)


//------------------------------------------------------------------------Desencolar--------------------------------------------------


    public Estudiante desencolar() {
        Estudiante peorNota = heap[0].obtenerEstudiante();  //guardo el estudiante con peor nota(raiz del heap)                // O(1)

        intercambiar(0, tamaño - 1);        // Intercambia la raíz con el último elemento del heap                            // O(1)
        tamaño--;                           // Reduce el tamaño del heap, "eliminando" el último elemento (el peor)           // O(1)
        siftDown(0);                        // Reacomoda el nuevo elemento en la raíz para restaurar el orden del heap        // O(log E)
        return peorNota;                      // Devuelve el ID del estudiante con peor nota                                    // O(1)
    } // Complejidad: O(log E)


//------------------------------------------------------------------------Actualizar nota--------------------------------------------------


    public void actualizarNotaDesdeHandle(Estudiante est, double nuevaNota) {
        double notaVieja = est.obtenerNota(); // Guarda la nota actual del estudiante antes de actualizarla           // O(1)

        int pos = posEnHeapDeHandel[est.obtenerId()];           // Obtiene la posición actual del estudiante dentro del heap            // O(1)

        HandleEst h = heap[pos];
        h.obtenerEstudiante().cambiarNota(nuevaNota);               // Asigna la nueva nota al estudiante             // O(1)

        
        if (nuevaNota < notaVieja)                                                                                  // O(1)
            siftUp(pos);                    // Sube al estudiante en el heap hasta restaurar el orden               // O(log E)
        else if (nuevaNota > notaVieja)                                                                             // O(1) 
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
        if (izq < tamaño && debeSubir(izq, menor))                   // O(1)
            menor = izq;
        if (der < tamaño && debeSubir(der, menor))                   // O(1)
            menor = der;
        if (menor == pos)                                            // O(1)
            break;                                                   // Ya está en el lugar correcto
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
        posEnHeapDeHandel[hEnI.obtenerId()]=j;
        posEnHeapDeHandel[hEnJ.obtenerId()]=i;
    }




    // USAR COMPARE TO ¿?
    private boolean debeSubir(int hijo, int padre) { 
        HandleEst hEnHijo = heap[hijo];                                                                           
        HandleEst hEnPadre = heap[padre]; 
        boolean res;
        if (hEnHijo.compareTo(hEnPadre)==1) {   // si el hijo tiene mayor prioridad que el padre, el hijo debe subir. 
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