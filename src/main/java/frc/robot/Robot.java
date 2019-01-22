/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot1;
  private DifferentialDrive m_myRobot2;
  private DifferentialDrive m_myRobot3;
  private Joystick m_leftStick;
  private Joystick m_rightStick;

  // There are three motors on each side (RIGHT/LEFT)
  // in three positions (TOP, then bottom: FRONT/BACK)
  // The color corrosponds to tape on the motor and control wire

  private static final int MOTOR_RIGHT_TOP_BLU = 1;
  private static final int MOTOR_LEFT_TOP_WHT = 4;
  private static final int MOTOR_RIGHT_FRONT_GRN = 2;
  private static final int MOTOR_LEFT_FRONT_YEL = 5;
  private static final int MOTOR_RIGHT_BACK_RED = 0;
  private static final int MOTOR_LEFT_BACK_BLK = 3;

  @Override
  public void robotInit() {
    m_myRobot1 = new DifferentialDrive(new PWMVictorSPX(MOTOR_RIGHT_TOP_BLU), new PWMVictorSPX(MOTOR_LEFT_TOP_WHT));
    m_myRobot2 = new DifferentialDrive(new PWMVictorSPX(MOTOR_RIGHT_FRONT_GRN), new PWMVictorSPX(MOTOR_LEFT_FRONT_YEL));
    m_myRobot3 = new DifferentialDrive(new PWMVictorSPX(MOTOR_RIGHT_BACK_RED), new PWMVictorSPX(MOTOR_LEFT_BACK_BLK));
    m_leftStick = new Joystick(0);
    m_rightStick = new Joystick(1);
  }

  @Override
  public void teleopPeriodic() {
    double left, right;
    left = m_leftStick.getY();
    right = m_rightStick.getY();
    m_myRobot1.tankDrive(left, right);
    m_myRobot2.tankDrive(left, right);
    m_myRobot3.tankDrive(left, right);
  }
}
