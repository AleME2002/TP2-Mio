package aed; 

public class HeapMin {
     private HandleEst[] heap;         // ids de estudiantes  
     private int tamaño;         // cuántos hay realmente (por si desencolamos gente)
     private int capacidad;      // max. estudiantes que puede tener el heap (siempre = E)


//------------------------------------------------------------------------Handle--------------------------------------------------------


    public class HandleEst {
        private int pos; 
        private Estudiante est; 

        public HandleEst(Estudiante e) { 
            this.est = e;                                           // O(1)
        }

        public Estudiante obtenerEstudiante() {
            return this.est;                                        // O(1)
        }

        public double obtenerNota() {
            return this.est.obtenerNota();                          // O(1)
        }

        public boolean obtenerEntrego() {
            return this.est.obtenerEstadoEntrega();                 // O(1)
        }

        public int obtenerId() {
            return this.est.obtenerId();                            // O(1)
        }

        public int obtenerPosicion() {
            return this.pos;                            // O(1)
        }

        public void cambiarPosicionEnHeap(int pos){
            this.pos = pos;
        }
        
        
        public int compareTo(HandleEst otro){
            int idOriginal= this.obtenerId();
            int idOtro = otro.obtenerId();

            double notaOriginal = this.obtenerNota();
            double notaOtro = otro.obtenerNota();

            boolean estadoEntregaOriginal = this.obtenerEntrego();
            boolean estadoEntregaOtro = otro.obtenerEntrego();

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

        //public vo id actualizarNota(double nuevaNota) {
        //    miHea p.actualizarNotaDesdeHandle(id, nuevaNota);        // O(log E)
        //} 
    }   


//------------------------------------------------------------------------Constructor--------------------------------------------------
    

    public HeapMin(int cantEstudiantes) {
        this.capacidad = cantEstudiantes;
        this.tamaño = 0;
        this.heap = new HandleEst[cantEstudiantes];
    }

    
    /*
    Primero se inicializan todas las estructuras internas del heap.
    Luego, en armarHeap() se cargan los IDs de los estudiantes, como todos comienzan con un examen vacío, 
    el heap ya cumple el invariante y no es necesario aplicar Heapify().
    */


//------------------------------------------------------------------------Encolar--------------------------------------------------


    public void encolar(HandleEst est) {
        
        heap[tamaño] = est;              // Inserta el handle del nuevo estudiante en la última posición libre del heap              // O(1)
        est.cambiarPosicionEnHeap(tamaño);

        tamaño++;                       // Aumenta el tamaño del heap, ya que ahora contiene un elemento más                      // O(1)
        siftUp(tamaño - 1);             // Reacomoda el nuevo elemento hacia arriba si su nota es menor que la de su padre        // O(log E)
    } // Complejiidad: O(log E)


//------------------------------------------------------------------------Desencolar--------------------------------------------------


    public Estudiante desencolar() {
        if (tamaño == 0) {
            return null;    // o lanzar exception, pero null está bien para TP
        }
        // El handle con la peor prioridad (la raíz)
        HandleEst raiz = heap[0];
        Estudiante peorEstudiante = raiz.obtenerEstudiante();
        // Mover el último elemento a la raíz
        intercambiar(0, tamaño - 1);
        tamaño--;     // "Eliminamos" el último handle (el peor)
        // Restaurar el heap
        if (tamaño > 0) {    // evitar siftDown cuando queda vacío
            siftDown(0);
        }
        return peorEstudiante;
    }



//------------------------------------------------------------------------Actualizar nota--------------------------------------------------


    public void actualizarNotaDesdeHandle(Estudiante est, double nuevaNota) {

        // Obtener el handle del estudiante
        HandleEst h = est.obtenerHandle();
        double notaVieja = est.obtenerNota();
        // Actualizar la nota en el estudiante real
        est.cambiarNota(nuevaNota);
        int pos = h.obtenerPosicion();   // posición actual del handle en el heap
        // Reacomodar en el heap según corresponda
        if (nuevaNota < notaVieja) {
            siftUp(pos);
        } else if (nuevaNota > notaVieja) {
            siftDown(pos);
        }
    }


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

}