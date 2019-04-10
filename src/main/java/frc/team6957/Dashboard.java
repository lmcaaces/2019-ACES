// ***Dashboard class***
// Makes it simpler to display and get values
// from the SmartDashboard

// NOTE: These are old.  Defaults specified here are set
//       At start up.   We can comment some out to change that!
// INSTRUCTIONS FOR SMARTDASHBOARD:
// In order to change the constants, go to
// View -> Add -> Robot Preferences
// Then add each robot preference by
// entering the string name, the data type,
// and the data to enter

// ROBOT PREFERENCE LIST:
// Tank Drive
// Arm Scale
// Drive Scale
// Turn Scale
// Deadband Arm

package frc.team6957;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class Dashboard {
    // *** Constants ***

    // Scale Factors (multiplier for motor speed)
    // < 1.0 == Slower;   = 1.0 == No change;  > 1.0 == Faster

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

    /** Dashboard Constructor.
     *
     * Initialize all values to defaults from constants.
     */

    public Dashboard () {
        // Make sure we always start with our default values.
        SmartDashboard.putBoolean(TANK_DRIVE, DEFAULT_TANK_DRIVE);
        SmartDashboard.putNumber(ARM_SCALE, DEFAULT_ARM_SCALE);
        SmartDashboard.putNumber(DRIVE_SCALE, DEFAULT_DRIVE_SCALE);
        SmartDashboard.putNumber(TURN_SCALE, DEFAULT_TURN_SCALE);
        SmartDashboard.putNumber(DEADBAND_ARM, DEFAULT_DEADBAND_ARM);

        update(); // Is this needed?
    }

    /** Updates all data for SmartDashboard */
    public void update() {
        SmartDashboard.updateValues();
    }

    /**
     * Displays error mesage
     * @param err String: Error to Display
     */
    public void error(String err) {
        SmartDashboard.putString("Errors", err);
    }

    /**
     * Displays general mesage
     * @param msg String: Message to Display
     */
    public void message(String msg) {
        SmartDashboard.putString("Messages", msg);
    }

    /**
     * Displays data to dashboard
     * @param name String: display name of the data
     * @param data String: data to be displayed
     */
    public void display(String name, String data) {
        SmartDashboard.putString(name, data);
    }

    /**
     * Displays data to dashboard
     * @param name String: display name of the data
     * @param data double: data to be displayed
     */
    public void display(String name, double data) {
        SmartDashboard.putNumber(name, data);
    }

    /**
     * Displays data to dashboard
     * @param name String: display name of the data
     * @param data boolean: data to be displayed
     */
    public void display(String name, boolean data) {
        SmartDashboard.putBoolean(name, data);
    }

    // DATA ACCESS METHODS

    /**
     * Get Arm Scale from Dashboard.
     * @return double ARM_SCALE
     */
    public double getArmScale() {
        return SmartDashboard.getNumber(ARM_SCALE, DEFAULT_ARM_SCALE);
    }

    /**
     * Get Drive Scale (Arcade and Tank modes) from Dashbaord.
     * @return double DRIVE_SCALE
     */
    public double getDriveScale() {
        return SmartDashboard.getNumber(DRIVE_SCALE, DEFAULT_DRIVE_SCALE);
    }

    /**
     * Get Turn Scale (Arcade mode only) from Dashboard.
     * @return double DRIVE_SCALE
     */
    public double getTurnScale() {
        return SmartDashboard.getNumber(TURN_SCALE, DEFAULT_TURN_SCALE);
    }

    /**
     * Get Tank Drive (true/false) from Dashboard.
     * @return boolean TANK_DRIVE
     */
    public boolean getTankDrive() {
        return SmartDashboard.getBoolean(TANK_DRIVE, DEFAULT_TANK_DRIVE);
    }

    /**
     * Get Arm Deadband from Dashboard.
     * @return double DEADBAND_ARM
     */
    public double getDeadbandArm() {
        return SmartDashboard.getNumber(DEADBAND_ARM, DEFAULT_DEADBAND_ARM);
    }
}
