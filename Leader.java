package ehi1vso2;

import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import java.util.ArrayList;

public class Leader extends TeamRobot{

    private ArrayList<EnemyBot> enemyBots = new ArrayList<>();

    public void run(){
        while(true){
            setTurnRadarRight(360);
            execute();
            calcThreat();
        }
    }

    /**
     * Called when this robot scans another robot
     *
     * Checks if the scanned enemy is already in the enemybots array, if not adds them.
     * If the enemy is already present in the array this method updates all it's data.
     *
     * @param event scannedRobotEvent containing all information about the enemy robot scanned.
     * @author Matthew Waanders
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        boolean found = false;

        //for loop to check if the enemy is already  in the array
        for (int i = 0; i < enemyBots.size(); i++) {

            //if it is found call updateData function
            if (event.getName().equals(enemyBots.get(i))){
                enemyBots.get(i).updateData(event.getName(), event.getEnergy(), event.getBearing(), event.getDistance(), event.getHeading(), event.getVelocity());
                found = true;
            }
        }

        //if not found adds the enemy to the array
        if (!found) {
            enemyBots.add(new EnemyBot(event.getName(), event.getEnergy(), event.getBearing(), event.getDistance(), event.getHeading(), event.getVelocity()));
        }
    }


    /**
     * Method that automatically calculates the threatlevel for every enemy found by this robot.
     * ThreatLevel is calculated using the following known information about the enemy:
     * distance, velocity, previous energy and current energy, previous damage done to us and the current damage done to us.
     *
     * @author Matthew Waanders
     */
    public void calcThreat(){

        //Check to avoid nullpointer exceptions on the enemy variable
        if (enemyBots.size() > 0) {

            //For loop to make sure to update the threatlevel for every enemie known
            for (int i = 0; i < enemyBots.size(); i++) {

                //instantiating enemy variable to keep the code clean
                EnemyBot enemy = enemyBots.get(i);

                //printing out all known and relevant information about the enemy
                System.out.println("Enemy name is: " + enemy.name);
                System.out.println("Enemy threatlevel is: " + enemy.threatLevel);
                System.out.println("-------------------------------------------");
                System.out.println("Distance is: " + enemy.distance);
                System.out.println("Velocity is: " + enemy.velocity);
                System.out.println("Energy previous is: " + enemy.prevEnergy);
                System.out.println("Energy now is: " + enemy.energy);
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++");

                //Different calculations for threatlevels apply to enemies that are moving and those who are not
                if (enemy.velocity > 0) {

                    //Base 90% maximum threatlevel for distance only
                    double threatLevel = (90 - (enemy.distance * 0.09));

                    //optional 2.5% threat for enemy speed of movement IF it is moving towards this robot
                    if (enemy.distance <= enemy.prevDistance) {
                        threatLevel += (enemy.velocity * 0.3125);
                    }

                    //optional 5% threat for when an enemy
                    if (enemy.energy <= (enemy.prevEnergy - 3)) {
                        threatLevel += 5;
                    }

                    //optional 2.5% threat for enemies that have hit us between scans
                    if (enemy.damageToUs > enemy.prevDamageToUs) {
                        threatLevel += 2.5;
                    }

                    //output and storage of the threatlevel
                    System.out.println("Threat level is: " + threatLevel + "\n" + "\n");
                    enemy.threatLevel = threatLevel;

                } else {

                    //optional 80% base threatlevel for distance when enemy is not moving
                    double threatLevel = (80 - (enemy.distance * 0.08));

                    //optional +10% threat for when an enemy has probably shot
                    if (enemy.energy <= (enemy.prevEnergy - 3)){
                        threatLevel += 10;
                    }

                    //optional +10% threat for when an enemy has hit us between scans
                    if (enemy.damageToUs > enemy.prevDamageToUs){
                        threatLevel += 10;
                    }
                }
            }
        }
    }

    /**
     * Called when this robot is hit by a bullet
     *
     * tries to find the enemy in the enemybots array.
     * IF the enemy is found it will check to see if it did us damage before
     *
     * IF enemy has damaged us before, the old value is stored in the prevDamageToUs variable
     * and the new total will be stored in the damageToUs variable.
     *
     *
     * @param event hitByBulletEvent containing all information about the bullet that hit us
     * @author Matthew Waanders
     */
    @Override
    public void onHitByBullet(HitByBulletEvent event) {

        //Find the enemy
        EnemyBot enemy = findEnemyByName(event.getName());

        //If enemy was found continue
        if (enemy != null) {

            //If they didn't do damage to us before, set the damage to the damage now
            if (enemy.damageToUs == 0) {
                enemy.damageToUs = ((event.getPower() * 3) + 2);
            }

            //Set the value of previous damage to the current one, then update the current one
            else {
                enemy.prevDamageToUs = enemy.damageToUs;
                enemy.damageToUs += ((event.getPower() * 3) + 2);
            }
        }
    }


    /**
     * helper method to find an enemy in the enemybots array by name
     * @param name name of the enemy  to find
     * @return the enemy found, or null if none found
     * @author Matthew Waanders
     */
    public EnemyBot findEnemyByName(String name){

        //for loop through the entire enemyBots list to find the enemy that matches names
        for (int i = 0; i < enemyBots.size(); i++) {
            if (enemyBots.get(i).name.equals(name)){
                return enemyBots.get(i);
            }
        }

        return null;
    }
}

