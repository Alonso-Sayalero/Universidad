public class Punto {
    private double coordenadaX;
    private double coordenadaY;
    private double modulo;

    public Punto(double x, double y){
        this.coordenadaX = x;
        this.coordenadaY = y;
        this.modulo = 0;
    }

    public Punto(double x, double y, Punto pivote){
        this.coordenadaX = x;
        this.coordenadaY = y;
        this.modulo = modulo(pivote);
    }

    public void setCoordenadaX(double x){
        this.coordenadaX = x;
    }

    public double getCoordenadaX(){
        return this.coordenadaX;
    }

    public void setCoordenadaY(double y){
        this.coordenadaY = y;
    }

    public double getCoordenadaY(){
        return this.coordenadaY;
    }

    private double modulo(Punto pivote){
        return Math.sqrt(Math.pow(this.coordenadaX - pivote.getCoordenadaX(), 2) + Math.pow(this.coordenadaY - pivote.getCoordenadaY(), 2));
    }

    public double getModulo(){
        return this.modulo;
    }

    @Override
    public String toString(){
        return "(" + this.coordenadaX + ", " + this.coordenadaY + " {" + this.modulo + "})";
    }
}
