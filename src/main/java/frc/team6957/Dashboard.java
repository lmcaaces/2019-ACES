/*
 * Manage Dashboard data (including constants!).
 * 
 * Utility to help get and set values in the SmartDashboard.
 *
 * The constants in this class are set as the default value
 * for those 'values' we store in the Dashboard.
 * 
 * To keep the value in the SmartDashboard, just comment out
 * the put* operation in the Constructor.
 * 
 * There is probably a better way to do this.
 * 
 * <h2>Setting values in SmartDashboard</h2>
 *
 * To change the constants:
 * <ul>
 * <li>go to "View -&gt Add; -&gt; Robot Preferences"</li>
 * <li>Add each robot preference by entering the string name, the data type,
 * and the data to enter.</li>
 * </ul>
 *
 * <h2>Robot Preferences</h2>
 * 
 * <ul>
 * <li>Tank Drive - false (default) = Arcade Mode</li>
 * <li>Arm Scale - motor scale for Arm [0.0 .. 1.0]</li>
 * <li>Drive Scale - more scale for drive motors [0.0 .. 1.0]</li>
 * <li>Turn Scale - motor scale for turns [0.0 .. 1.0] (Arcade Mode) only.</li>
 * <li>Deadband Arm - deadband removal (around 0) for Arm motor control.</li>
 * </ul>
 */

package frc.team6957;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class Dashboard {
    // *** Constants ***

    // TODO This is NOT an obvious place for global constants to live!

    // Scale Factors (multiplier for motor speed)
    // < 1.0 == Slower; = 1.0 == No change; > 1.0 == Faster

    private static final String TANK_DRIVE = "Tank Drive";
    private static final boolean DEFAULT_TANK_DRIVE = false;

    private static final String ARM_SCALE = "Arm Scale";
    private static final double DEFAULT_ARM_SCALE = 0.6;

    private static final String DRIVE_SCALE = "Drive Scale";
    private static final double DEFAULT_DRIVE_SCALE = 0.7;

    private static final String TURN_SCALE = "Turn Scale";
    private static final double DEFAULT_TURN_SCALE = 0.5;

    private static final String DEADBAND_ARM = "Deadband Arm";
    private static final double DEFAULT_DEADBAND_ARM = 0.05;

    /**
     * Dashboard Constructor.
     *
     * Initialize all values to defaults from constants.
     */
    public Dashboard() {
        // Make sure we always start with our default values.
        SmartDashboard.putBoolean(TANK_DRIVE, DEFAULT_TANK_DRIVE);
        SmartDashboard.putNumber(ARM_SCALE, DEFAULT_ARM_SCALE);
        SmartDashboard.putNumber(DRIVE_SCALE, DEFAULT_DRIVE_SCALE);
        SmartDashboard.putNumber(TURN_SCALE, DEFAULT_TURN_SCALE);
        SmartDashboard.putNumber(DEADBAND_ARM, DEFAULT_DEADBAND_ARM);

        update(); // Is this needed?
    }

    /** 
     * Updates all data for SmartDashboard.
     */
    public void update() {
        SmartDashboard.updateValues();
    }

    /**
     * Displays error mesage.
     * 
     * @param err String: Error to Display
     */
    public void error(String err) {
        SmartDashboard.putString("Errors", err);
    }

    /**
     * Displays general mesage.
     * 
     * @param msg String: Message to Display
     */
    public void message(String msg) {
        SmartDashboard.putString("Messages", msg);
    }

    /**
     * Displays data to dashboard.
     * 
     * @param name String: display name of the data
     * @param data String: data to be displayed
     */
    public void display(String name, String data) {
        SmartDashboard.putString(name, data);
    }

    /**
     * Displays data to dashboard.
     * 
     * @param name String: display name of the data
     * @param data double: data to be displayed
     */
    public void display(String name, double data) {
        SmartDashboard.putNumber(name, data);
    }

    /**
     * Displays data to dashboard.
     * 
     * @param name String: display name of the data
     * @param data boolean: data to be displayed
     */
    public void display(String name, boolean data) {
        SmartDashboard.putBoolean(name, data);
    }

    // DATA ACCESS METHODS

    /**
     * Get Arm Scale from Dashboard.
     * 
     * @return double ARM_SCALE
     */
    public double getArmScale() {
        return SmartDashboard.getNumber(ARM_SCALE, DEFAULT_ARM_SCALE);
    }

    /**
     * Get Drive Scale (Arcade and Tank modes) from Dashbaord.
     * 
     * @return double DRIVE_SCALE
     */
    public double getDriveScale() {
        return SmartDashboard.getNumber(DRIVE_SCALE, DEFAULT_DRIVE_SCALE);
    }

    /**
     * Get Turn Scale (Arcade mode only) from Dashboard.
     * 
     * @return double DRIVE_SCALE
     */
    public double getTurnScale() {
        return SmartDashboard.getNumber(TURN_SCALE, DEFAULT_TURN_SCALE);
    }

    /**
     * Get Tank Drive (true/false) from Dashboard.
     * 
     * @return boolean TANK_DRIVE
     */
    public boolean getTankDrive() {
        return SmartDashboard.getBoolean(TANK_DRIVE, DEFAULT_TANK_DRIVE);
    }

    /**
     * Get Arm Deadband from Dashboard.
     * 
     * @return double DEADBAND_ARM
     */
    public double getDeadbandArm() {
        return SmartDashboard.getNumber(DEADBAND_ARM, DEFAULT_DEADBAND_ARM);
    }
}
