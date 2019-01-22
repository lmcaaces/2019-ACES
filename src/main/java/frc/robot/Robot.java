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

  @Override
  public void robotInit() {
    // TODO: Make constants
    // Right: 1 (BLU); Left: 4 (WHT)
    m_myRobot1 = new DifferentialDrive(new PWMVictorSPX(1), new PWMVictorSPX(4));
    // Right: 2 (GRN); Left: 5 (YEL)
    m_myRobot2 = new DifferentialDrive(new PWMVictorSPX(2), new PWMVictorSPX(5));
    // Right: 0 (RED); Left: 3 (BLK)
    m_myRobot3 = new DifferentialDrive(new PWMVictorSPX(0), new PWMVictorSPX(3));
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
