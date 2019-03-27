/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team6957;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;

/**
 * Team 6957 - Basic Robot Control
 */
public class Robot extends TimedRobot {
  // *** Constants ***

  // Deadband for ARM control
  private static final double DEADBAND_ARMS = 0.05;
  private static final double DEADBAND_HANDS = 0.05;

  // For XBox Joystick (Axis)
  private static final int LX_AXIS = 0;
  private static final int LY_AXIS = 1;
  private static final int L_TRIGGER = 2;
  private static final int R_TRIGGER = 3;
  private static final int RX_AXIS = 4;
  private static final int RY_AXIS = 5;

  // XBox Joystick (Buttons) - Just ones I need
  private static final int BUTTON_A = 1;
  private static final int BUTTON_B = 2;

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

  private DifferentialDrive m_drive;
  private SpeedControllerGroup m_right_speedgroup;
  private SpeedControllerGroup m_left_speedgroup;

  private Spark m_arm_large;
  private Spark m_arm_small;

  private Spark m_hand_left;
  private Spark m_hand_right;

  private Joystick m_control_driver;
  private Joystick m_control_operator;

  // Used for joystick positions
  private double leftX, leftY, rightY;

  // Value sent to control hands
  private double hand;

  // Push hatch covers
  private DoubleSolenoid solenoid;

  private boolean op_button_a;
  private boolean op_button_b;

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
    m_control_driver = new Joystick(0);
    m_control_operator = new Joystick(1);

    // Operator Arm Control
    m_arm_large = new Spark(MOTOR_ARM_LARGE_VIO);
    m_arm_large.setInverted(true);
    m_arm_small = new Spark(MOTOR_ARM_SMALL_WHT);

    // Operator Hand Control
    m_hand_left = new Spark(HAND_LEFT_YEL);
    m_hand_right = new Spark(HAND_RIGHT_BRN);
    m_hand_right.setInverted(true);

    // DOES THIS NEED TO BE GAURDED?  Can it be null?
    solenoid = new DoubleSolenoid(SOL_FORWARD_PCM_ID, SOL_REVERSE_PCM_ID);

    // Turn on USB Camera (if present)
    cameraserver = CameraServer.getInstance();
    if (cameraserver != null) {
      cameraserver.startAutomaticCapture();
    } else {
      System.out.println("TEAM6957: No Camera Found.");
    }
  }

  @Override
  public void teleopPeriodic() {
    //
    // Driver Control
    //

    // Get Joystick positions
    leftY = m_control_driver.getY();
    leftX = m_control_driver.getX();
    rightY = m_control_driver.getRawAxis(RY_AXIS);   // Only for Tank mode.

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
    leftY = Deadband(m_control_operator.getY(), DEADBAND_ARMS);
    rightY = Deadband(m_control_operator.getRawAxis(RY_AXIS), DEADBAND_ARMS);

    m_arm_large.set(leftY);
    m_arm_small.set(rightY);

    // Hand control - Use the Operator Left and Right trigger.   They return
    // 0..1.   Blend them together for a value.
    hand = m_control_operator.getRawAxis(L_TRIGGER) - m_control_operator.getRawAxis(R_TRIGGER);
    hand = Deadband(hand, DEADBAND_HANDS);

    // Operator Control Hands (Grab/Release balls)
    m_hand_left.set(hand);
    m_hand_right.set(hand);

    // Buttons to control solenoid to push hatch covers
    op_button_a = m_control_driver.getRawButton(BUTTON_A);
    op_button_b = m_control_driver.getRawButton(BUTTON_B);

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

  // HELPER FUNCTION(S)

  /** Deadband with the given percent.
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
}
