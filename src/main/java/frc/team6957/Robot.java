/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team6957;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * Team 6957 - Basic Robot Control
 */
public class Robot extends TimedRobot {
  // *** Constants ***

  // Deadband for ARM control
  private static final double DEADBAND_ARMS = 0.05;
  private static final double DEADBAND_HANDS = 0.05;

  // ARM Scale Factor (multiplier for motor speed)
  // < 1.0 == Slower
  // = 1.0 == No change
  // > 1.0 == Faster (limits range)
  private static final double ARM_LARGE_SCALE = 0.60;
  private static final double ARM_SMALL_SCALE = 0.50;

  // There are PWM Control mapping constants

  // Drive Train Motors
  // The color corresponds to tape on the motor and control wire
  // NOTE: Each on of these motors has there own SPARK controller

  private static final int MOTOR_RIGHT_BACK_RED = 0;   // Red
  private static final int MOTOR_RIGHT_FRONT_GRN = 1;  // Green
  private static final int MOTOR_LEFT_BACK_BLU = 2;    // Blue
  private static final int MOTOR_LEFT_FRONT_ORG = 3;   // Orange

  // Control Arm Motors
  private static final int MOTOR_ARM_LARGE_VIO = 4;    // Violet(Purple)
  private static final int MOTOR_ARM_SMALL_WHT = 5;    // White

  // Hand (Intake) Motors
  private static final int HAND_LEFT_YEL = 6;          // Yellow
  private static final int HAND_RIGHT_BRN = 7;         // Brown

  // CAN Controller IDs
  // Reference, not used
  // private static final int PDP_CAN_ID = 1;  // Power Distribution Panel
  private static final int PCM_CAN_ID = 0;  // Pneumatic Control Module  (Assumed in Solenoid code!)

  // PCM Controller IDs
  // Tyler = Operator; Curtis(sp?) = Drive
  // Tyler wants B to push the solenoid out, and A to pull it back in (SHOULD THAT BE AUTOMATIC?  TIMED AFTER RELEASE?)

  private static int SOL_FORWARD_PCM_ID = 0;
  private static int SOL_REVERSE_PCM_ID = 1;

  // *** Variables ***

  // TODO Have this be settable on the dashboard
  private boolean TANK_DRIVE = false;

  // TODO: Make this an enum?
  // When true, reverse the values of the joystick - so the driver
  // can drive backwards (to place/get hatces) using forward controls.
  private boolean drive_reversed = false;

  private DifferentialDrive m_drive;
  private SpeedControllerGroup m_right_speedgroup;
  private SpeedControllerGroup m_left_speedgroup;

  private Spark m_arm_large;
  private Spark m_arm_small;

  private Spark m_hand_left;
  private Spark m_hand_right;

  private XboxController joystick_driver;
  private XboxController joystick_operator;

  // Used for joystick positions
  private double leftX, leftY, rightY;

  // Value sent to control hands
  private double hand;

  // Push hatch covers
  private Compressor compressor;
  private DoubleSolenoid solenoid;

  private boolean op_button_a;
  private boolean op_button_b;

  private boolean drv_button_reverse;
  private boolean drv_button_check_drive; // Want this to rumble

  // USB Camera Server
  private CameraServer cameraserver;

  @Override
  public void robotInit() {
    // Should most of this should be in teleopInit?
    m_right_speedgroup = new SpeedControllerGroup(
      new Spark(MOTOR_RIGHT_FRONT_GRN),
      new Spark(MOTOR_RIGHT_BACK_RED)
      );
    m_left_speedgroup = new SpeedControllerGroup(
      new Spark(MOTOR_LEFT_FRONT_ORG),
      new Spark(MOTOR_LEFT_BACK_BLU)
      );
    m_drive = new DifferentialDrive(m_right_speedgroup, m_left_speedgroup);
    joystick_driver = new XboxController(0);
    joystick_operator = new XboxController(1);

    // Operator Arm Control
    m_arm_large = new Spark(MOTOR_ARM_LARGE_VIO);
    m_arm_large.setInverted(true);
    m_arm_small = new Spark(MOTOR_ARM_SMALL_WHT);

    // Operator Hand Control
    m_hand_left = new Spark(HAND_LEFT_YEL);
    m_hand_right = new Spark(HAND_RIGHT_BRN);
    m_hand_right.setInverted(true);

    // Set up compressor.  Have it controlled with PCM Pressure Switch
    compressor = new Compressor(PCM_CAN_ID);
    if (compressor != null) {
      // PCM Aut⌂omatically turns on compressor when 'Pressure SW' is closed
      compressor.setClosedLoopControl(true);
    } else {
      System.out.println("TEAM6957: Can't setup Compressor/PCM");
    }

    // DOES THIS NEED TO BE GAURDED?  Can it be null?
    solenoid = new DoubleSolenoid(SOL_FORWARD_PCM_ID, SOL_REVERSE_PCM_ID);
    if (solenoid != null) {
      System.out.println("TEAM6957: Solenoid NOT Instantiated.");
    }

    // Turn on USB Camera (if present)
    cameraserver = CameraServer.getInstance();
    if (cameraserver != null) {
      cameraserver.startAutomaticCapture();
    } else {
      System.out.println("TEAM6957: No Camera Found.");
    }
  }

  @Override
  public void autonomousPeriodic() {
    teleopPeriodic();
  }

  @Override
  public void teleopPeriodic() {
    //
    // Driver Control
    //

    // Get Joystick positions
    leftY = joystick_driver.getY(Hand.kLeft);
    leftX = joystick_driver.getX(Hand.kLeft);
    rightY = joystick_driver.getY(Hand.kRight);   // Only for Tank mode.


    drv_button_reverse = joystick_driver.getBackButtonReleased();
    if (drv_button_reverse) {
      drive_reversed = !drive_reversed;
    }

    drv_button_check_drive =  joystick_driver.getStartButton();
    if (drv_button_check_drive) {
      String drivetype;
      if (drive_reversed) {
        drivetype = "Reversed";
      } else {
        drivetype = "Normal";
      }
      System.out.println("TEAM6957: Drive " + drivetype);
    }

    if (drive_reversed) {
      leftY = -leftY;
      // leftX = -leftX;
      rightY = -rightY;
    }

    if (TANK_DRIVE) {
      // NOTE: This uses leftStick and rightStick
      // QUESTION: How to make it use the right joystick controller on leftStick?
      m_drive.tankDrive(leftY, rightY);
    } else {
      // NOTE: This uses only the leftStick
      m_drive.arcadeDrive(leftY, leftX);
    }

    //
    // Operator Control
    //

    // Get Joystick positions and send to arm motors
    //TEMP leftY = Deadband(joystick_operator.getY(Hand.kLeft), DEADBAND_ARMS);
    //TEMP rightY = Deadband(joystick_operator.getY(Hand.kRight), DEADBAND_ARMS);

    //TEMP Arms disabled - motor issues.
    //TEMP m_arm_large.set(leftY * ARM_LARGE_SCALE);
    //TEMP m_arm_small.set(rightY * ARM_SMALL_SCALE);

    // Hand control - Use the Operator Left and Right trigger.   They return
    // 0..1.   Blend them together for a value.
    hand = joystick_operator.getTriggerAxis(Hand.kLeft) - joystick_operator.getTriggerAxis(Hand.kRight);
    hand = scale_hands(hand);

    // Operator Control Hands (Grab/Release balls)
    m_hand_left.set(hand);
    m_hand_right.set(hand);

    // Buttons to control solenoid to push hatch covers
    op_button_a = joystick_operator.getAButton();
    op_button_b = joystick_operator.getBButton();

    if (solenoid != null) {
      if (op_button_b) {
        // PUSH has priority
        solenoid.set(DoubleSolenoid.Value.kForward);
      } else if (op_button_a) {
        // Otherwise, see if we should pull.
        solenoid.set(DoubleSolenoid.Value.kReverse);
      } else {
        // Otherwise, stop pressure to solenoid.
        solenoid.set(DoubleSolenoid.Value.kOff);
      }
    }
  }

  // HELPER FUNCTION(S)

  /** Deadband removal with the given percent.
   *
   * The Joy stick axis output is -1.0..1.0.   The value sent to the motor
   * controllers is the same.   However, the joysticks wiggle some around
   * 0, so we remove a small percentage around there so the motors don't
   * 'jump' around while stationary.
   *
   * @param value The floating point input value (-1.0 .... 1.0, range not checked)
   * @param deadband The deadband.
   * @return The value, except values between -deadband to +deadband are returned as 0.0.
   */
  double Deadband(double value, double deadband) {
    /* Upper deadband */
    if (value >= +deadband)
      return value;

    /* Lower deadband */
    if (value <= -deadband)
      return value;

    /* Outside deadband */
    return 0;
  }

  /**
   * Scale motor control for the hands.
   * Flatten out control on the low end.
   * 
   * Expects input in range [0..1]
   */
  double scale_hands(double input)
  {
     return Deadband(
        Math.pow(input, 2),
        DEADBAND_HANDS);
  }
}
