package aed; 

public class HeapMin {
     private int[] heap;         // ids de estudiantes  
     private int[] posiciones;   // posiciones[id] = índice en heap 
     private double[] notas;        // notas[id] = nota actual 
     private int tamaño;         // cuántos hay realmente (por si desencolamos gente)
     private int capacidad;      // max. estudiantes que puede tener el heap (siempre = E)


//------------------------------------------------------------------------Handle--------------------------------------------------------


    public class Handle {
        private int id; 
        private HeapMin miHeap; 

        private Handle(int id, HeapMin heap) { 
            this.id = id;                                           // O(1)
            this.miHeap = heap;                                     // O(1)
        }

        public int id() {
            return id;                                              // O(1)
        }

        public double nota() {
            return miHeap.notas[id];                                // O(1)
        }

        public void actualizarNota(double nuevaNota) {
            miHeap.actualizarNotaDesdeHandle(id, nuevaNota);        // O(log E)
        }
    }


//------------------------------------------------------------------------Constructor--------------------------------------------------
    

    public HeapMin(int cantEstudiantes) {
        this.capacidad = cantEstudiantes;                       // O(1)
        this.tamaño = cantEstudiantes;                          // O(1)
        this.heap = new int[cantEstudiantes];                   // O(E)
        this.posiciones = new int[cantEstudiantes];             // O(E)
        this.notas = new double[cantEstudiantes];               // O(E)
        this.armarHeap();                                       // O(E)
    } // Complejidad: O(E)

    private void armarHeap() {
        for (int i = 0; i < capacidad; i++) {         // Recorre todos los estudiantes                        // O(E)
            heap[i] = i;                              // Guarda el ID del estudiante                          // O(1)
            posiciones[i] = i;                        // Registra en que posicion del heap esta ese id        // O(1)
            notas[i] = 0;                             // Asigna la nota inicial de ese estudiant              // O(1)
        }
    } // Complejidad: O(E)
    
    /*
    Primero se inicializan todas las estructuras internas del heap.
    Luego, en armarHeap() se cargan los IDs de los estudiantes, como todos comienzan con un examen vacío, 
    el heap ya cumple el invariante y no es necesario aplicar Heapify().
    */


//------------------------------------------------------------------------Encolar--------------------------------------------------


    public void encolar(int id) {
        heap[tamaño] = id;              // Inserta el nuevo estudiante en la última posición libre del heap                       // O(1)
        posiciones[id] = tamaño;        // Registra en qué posición del heap quedó almacenado ese estudiante                      // O(1)
        tamaño++;                       // Aumenta el tamaño del heap, ya que ahora contiene un elemento más                      // O(1)
        siftUp(tamaño - 1);             // Reacomoda el nuevo elemento hacia arriba si su nota es menor que la de su padre        // O(log E)
    } // Complejiidad: O(log E)


//------------------------------------------------------------------------Desencolar--------------------------------------------------


    public int desencolar() {
        int peorId = heap[0];               // Guarda el ID del estudiante con peor nota (raíz del heap)                      // O(1)
        intercambiar(0, tamaño - 1);        // Intercambia la raíz con el último elemento del heap                            // O(1)
        tamaño--;                           // Reduce el tamaño del heap, "eliminando" el último elemento (el peor)           // O(1)
        siftDown(0);                        // Reacomoda el nuevo elemento en la raíz para restaurar el orden del heap        // O(log E)
        return peorId;                      // Devuelve el ID del estudiante con peor nota                                    // O(1)
    } // Complejidad: O(log E)


//------------------------------------------------------------------------Actualizar nota--------------------------------------------------


    public void actualizarNotaDesdeHandle(int id, double nuevaNota) { 
        double notaVieja = notas[id];          // Guarda la nota actual del estudiante antes de actualizarla           // O(1)
        notas[id] = nuevaNota;              // Asigna la nueva nota al estudiante en el arreglo de notas            // O(1)
        int pos = posiciones[id];           // Obtiene la posición actual del estudiante dentro del heap            // O(1)
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
} // Complejidad total: O(log E)


//------------------------------------------------------------------------Extras--------------------------------------------------------


    public void intercambiar(int i, int j) { 
        int idEnI = heap[i]; 
        int idEnJ = heap[j]; 
        
        // intercambiamos ids en el heap y actualizamos las posiciones
        heap[i] = idEnJ;
        heap[j] = idEnI;
        posiciones[idEnI] = j;
        posiciones[idEnJ] = i;
    }

    // USAR COMPARE TO ¿?
    private boolean debeSubir(int i, int j) { 
        int idPosI = heap[i];                                                                           
        int idPosJ = heap[j]; 
        boolean res = true;
        // comparamos la nota. si es la misma, se desempata por menor id
        if ((notas[idPosI] < notas[idPosJ]) || (notas[idPosI] == notas[idPosJ] && idPosI < idPosJ)) { 
            res = true; 
        } else {
            res = false;
        }
        return res; 
    }





        // Complejidad = O(1)
    public Handle obtenerHandle(int id) {
        Handle handleParaId = new Handle(id, this); 
        return handleParaId;
    }

    // TAL VEZ SIRVE ¿? BORRAR SI NO
    public boolean vacio() {
        return tamaño == 0;
    }
}
