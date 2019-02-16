/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * Team 6957 - Basic Robot Control
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_drive;
  private SpeedControllerGroup m_right_speedgroup;
  private SpeedControllerGroup m_left_speedgroup;

  private Joystick m_control_driver;
  private Joystick m_control_operator;

  boolean TANK_DRIVE = false;

  // Used for joystick positions
  private double leftX, leftY, rightY;

  // For XBox Joystick

  private static final int LX_AXIS = 0;
  private static final int LY_AXIS = 1;
  private static final int L_TRIGGER = 2;
  private static final int R_TRIGGER = 3;
  private static final int RX_AXIS = 4;
  private static final int RY_AXIS = 5;

  // There are three motors on each side (RIGHT/LEFT)
  // in three positions (TOP, then bottom: FRONT/BACK)
  // The color corresponds to tape on the motor and control wire

  private static final int MOTOR_RIGHT_BACK_RED = 0;
  private static final int MOTOR_RIGHT_TOP_BLU = 1;
  private static final int MOTOR_RIGHT_FRONT_GRN = 2;
  private static final int MOTOR_LEFT_BACK_BLK = 3;
  private static final int MOTOR_LEFT_TOP_WHT = 4;
  private static final int MOTOR_LEFT_FRONT_YEL = 5;

  @Override
  public void robotInit() {
    m_right_speedgroup = new SpeedControllerGroup(
      new Spark(MOTOR_RIGHT_TOP_BLU),
      new Spark(MOTOR_RIGHT_FRONT_GRN),
      new Spark(MOTOR_RIGHT_BACK_RED)
      );
    m_left_speedgroup = new SpeedControllerGroup(
      new Spark(MOTOR_LEFT_TOP_WHT),
      new Spark(MOTOR_LEFT_FRONT_YEL),
      new Spark(MOTOR_LEFT_BACK_BLK)
      );
    m_drive = new DifferentialDrive(m_right_speedgroup, m_left_speedgroup);
    m_control_driver = new Joystick(0);
    m_control_operator = new Joystick(1);
  }

  @Override
  public void teleopPeriodic() {

    // Get Joystick positions
    leftY = m_control_driver.getY();
    leftX = m_control_driver.getX();
    rightY = m_control_driver.getRawAxis(RY_AXIS);
//
// BETTER: Create XboxGamePad class that gets all the values I need, so I can
//         use it for multiple JoySticks.

    if (TANK_DRIVE) {
      // NOTE: This uses leftStick and rightStick
      // QUESTION: How to make it use the right joystick controller on leftStick?
      m_drive.tankDrive(leftY, rightY);
    } else {
      // NOTE: This uses only the leftStick
      m_drive.arcadeDrive(leftY, leftX);
    }
  }
}
