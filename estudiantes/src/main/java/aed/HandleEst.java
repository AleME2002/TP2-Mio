package aed;

public class HandleEst {
    private int posEnHeap; 
    private Estudiante est; 


    /*/
    ¿QUE HAY ACA?
        CONSTRUCTOR
        - HandleEst(Estudiante e) O(1)

        GETTERS
        - obternerEstudiante()  O(1)
        - ObtenerPosicionEnHeap()   O(1)

        SETTERS
        - cambiarEstudiante(Estudiante e) O(1)
        - cambiarPosicionEnHeap(int pos) O(1)
    /*/

    //CONSTRUCTOR
    public HandleEst(Estudiante e) { 
        this.est = e;                                           // O(1)
    }

    //GETTERS
    public Estudiante obtenerEstudiante() {
        return this.est;                                        // O(1)
    }

    public int ObtenerPosicionEnHeap() {
        return this.posEnHeap;                                  // O(1)
    }

    //SETERS
    public void cambiarEstudiante(Estudiante e){
        this.est = e;                                           // O(1)
    }

    public void cambiarPosicionEnHeap(int pos){
        this.posEnHeap = pos;                                   // O(1)
    }
}