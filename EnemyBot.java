package ehi1vso2;

public class EnemyBot implements Comparable<EnemyBot>{

    protected String name;
    protected double energy;
    protected double bearing;
    protected double distance;
    protected double heading;
    protected double velocity;

    protected double prevEnergy;
    protected double prevDistance;

    protected double damageToUs = 0;
    protected double prevDamageToUs = 0;

    protected double delX;
    protected double delY;

    protected double threatLevel = 0;

    public EnemyBot(String name, double energy, double bearing, double distance, double heading, double velocity) {
        this.name = name;
        this.energy = energy;
        this.bearing = bearing;
        this.distance = distance;
        this.heading = heading;
        this.velocity = velocity;
    }

    public void setCoords(double X, double Y){
        this.delX = X;
        this.delY = Y;
    }

    public void updateData(String name, double energy, double bearing, double distance, double heading, double velocity){
        this.name = name;
        this.prevEnergy = this.energy;
        this.energy = energy;
        this.bearing = bearing;
        this.prevDistance = distance;
        this.distance = distance;
        this.heading = heading;
        this.velocity = velocity;
    }


    @Override
    public int compareTo(EnemyBot o) {
        if (this.distance < o.distance){
            return -1;
        } else if (this.distance == o.distance && this.velocity < o.velocity){
            return -1;
        } else if (this.distance == o.distance){
            return 0;
        } else if (this.distance == o.distance && this.velocity == o.velocity){
            return 0;
        } else return 1;
    }
}
