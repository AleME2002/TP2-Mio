package aed;

public class NotaFinal implements Comparable<NotaFinal> {
    public double _nota;
    public int _id;

    public NotaFinal(double nota, int id){
        _nota = nota;
        _id = id;
    }

    public int compareTo(NotaFinal otra){
        if (otra._id != this._id){
            return this._id - otra._id;
        }
        return Double.compare(this._nota, otra._nota);
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (otro == null || this.getClass() != otro.getClass()) return false;

        NotaFinal other = (NotaFinal) otro;
        return this._id == other._id && this._nota == other._nota;
    }
}
