package frc.team6957;

import edu.wpi.first.networktables.NetworkTableInstance;

public final class Limelight {

    /**
     * Limelight Camera Mode Setting 
     * This switches the camera mode to a driver vision mode 
     */
    public void setDriveMode() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
    }

    /** 
     * Limelight Camera Mode Setting
     * This switches the camera mode to a tracking vision mode
     */
    public void setTrackMode() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
     }

    /** 
     * Limelight value accessor method
     * @return Returns a double value that is the offest of the target (+ or - 26)
     */
    public double getXOffset() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    }



} 