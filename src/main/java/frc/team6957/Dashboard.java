// ***Dashboard class***
// Makes it simpler to display and get values
// from the SmartDashboard

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

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;;

public final class Dashboard {
    
    private static String errors;
    private static String messages;

    private static boolean TANK_DRIVE;
    private static double ARM_SCALE;
    private static double DRIVE_SCALE;
    private static double TURN_SCALE;
    private static double DEADBAND_ARM;

    private static final boolean DEFAULT_TANK_DRIVE = false;
    private static final double DEFAULT_ARM_SCALE = 0.6;
    private static final double DEFAULT_DRIVE_SCALE = 0.7;
    private static final double DEFAULT_TURN_SCALE = 0.5;
    private static final double DEFAULT_DEADBAND_ARM = 0.05;



    private void readTable() {
        TANK_DRIVE = SmartDashboard.getBoolean("Tank Drive", DEFAULT_TANK_DRIVE);
        ARM_SCALE = SmartDashboard.getNumber("Arm Scale", DEFAULT_ARM_SCALE);
        DRIVE_SCALE = SmartDashboard.getNumber("Drive Scale", DEFAULT_DRIVE_SCALE);
        TURN_SCALE = SmartDashboard.getNumber("Turn Scale", DEFAULT_TURN_SCALE);
        DEADBAND_ARM = SmartDashboard.getNumber("Deadband Arm", DEFAULT_DEADBAND_ARM);
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
        errors = "<" + err + "> ";
        SmartDashboard.putString("Errors", errors);
    }

    /**
     * Displays general mesage
     * @param msg String: Message to Display
     */
    public void message(String msg) {
        messages = "<" + msg + "> ";
        SmartDashboard.putString("Messages", messages);
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
     * Accessor method for ARM_SCALE
     * @return double ARM_SCALE
     */
    public double getArmScale() {
        readTable();
        return ARM_SCALE;
    }

    /** 
     * Accessor method for DRIVE_SCALE
     * @return double DRIVE_SCALE
     */
    public double getDriveScale() {
        readTable();
        return DRIVE_SCALE;
    }

    /** 
     * Accessor method for TURN_SCALE
     * @return double DRIVE_SCALE
     */
    public double getTurnScale() {
        readTable();
        return TURN_SCALE;
    }

    /** 
     * Accessor method for TANK_DRIVE
     * @return boolean TANK_DRIVE
     */
    public boolean getTankDrive() {
        readTable();
        return TANK_DRIVE;
    }

    /** 
     * Accessor method for DEADBAND_ARM
     * @return double DEADBAND_ARM
     */
    public double getDeadbandArm() {
        readTable();
        return DEADBAND_ARM;
    }
    
}