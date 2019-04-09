package frc.team6957;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

public final class Limelight {
    private NetworkTable table;
    private NetworkTableEntry camMode;
    private NetworkTableEntry tx;

    /**
     * Dashboard Constructor.
     *
     * Set up the Network table variables.
     */
    public Limelight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
        camMode = table.getEntry("camMode");
        tx = table.getEntry("tx");
    }

    /**
     * Limelight Camera Mode Setting to Drive Mode.
     * This switches the camera mode to a driver vision mode
     */
    public void setDriveMode() {
        camMode.setNumber(1);
    }

    /**
     * Limelight Camera Mode Setting to Track Mode.
     * This switches the camera mode to a tracking vision mode
     */
    public void setTrackMode() {
        camMode.setNumber(0);
     }

    /**
     * Limelight value accessor method
     * @return Returns a double value that is the offest of the target (+ or - 26)
     */
    public double getXOffset() {
        return tx.getDouble(0);
    }
}