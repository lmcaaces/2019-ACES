/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team6957;

import java.lang.Math;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;

/**
 * Team 6957 - Basic Robot Control
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_drive;
  private SpeedControllerGroup m_right_speedgroup;
  private SpeedControllerGroup m_left_speedgroup;

  private Spark m_arm_large;
  private Spark m_arm_small;

  private Joystick m_control_driver;
  private Joystick m_control_operator;

  // TODO Have this be settable on the dashboard
  boolean TANK_DRIVE = false;

  // Used for joystick positions
  private double leftX, leftY, rightY;

  // Deadband for ARM control
  private static final double DEADBAND = 0.2;

  // For XBox Joystick

  private static final int LX_AXIS = 0;
  private static final int LY_AXIS = 1;
  private static final int L_TRIGGER = 2;
  private static final int R_TRIGGER = 3;
  private static final int RX_AXIS = 4;
  private static final int RY_AXIS = 5;

  // There are PWM Control mapping constants
  
  // Drive Train Motors
  // three motors on each side (RIGHT/LEFT)
  // in three positions (TOP, then bottom: FRONT/BACK)
  // The color corresponds to tape on the motor and control wire
  // NOTE: Each on of these motors has there own SPARK controller

  private static final int MOTOR_RIGHT_BACK_RED = 0;
  private static final int MOTOR_RIGHT_TOP_BLU = 1;
  private static final int MOTOR_RIGHT_FRONT_GRN = 2;
  private static final int MOTOR_LEFT_BACK_BLK = 3;
  private static final int MOTOR_LEFT_TOP_WHT = 4;
  private static final int MOTOR_LEFT_FRONT_YEL = 5;

  // Controll Arm Motors
  private static final int MOTOR_ARM_LARGE = 6;
  private static final int MOTOR_ARM_SMALL = 7;

  @Override
  public void robotInit() {
    // Should most of this should be in teleopInit?
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

    m_arm_large = new Spark(MOTOR_ARM_LARGE);
    m_arm_large.setInverted(true);
    m_arm_small = new Spark(MOTOR_ARM_SMALL);
  }

  @Override
  public void teleopPeriodic() {
    // TODO Make sections routines and pass in the joystick?

    //
    // Driver Control
    //

    // Get Joystick positions
    leftY = m_control_driver.getY();
    leftX = m_control_driver.getX();
    rightY = m_control_driver.getRawAxis(RY_AXIS);

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

    // Get Joystick positions
    leftY = Deadband(m_control_operator.getY());
    rightY = Deadband(m_control_operator.getRawAxis(RY_AXIS));

    // TODO Should I be using another class to controll this (with deadband, etc?)
    System.out.println("ARMS");

    m_arm_large.set(leftY);
    System.out.print("Large ");
    System.out.println(leftY);

    m_arm_small.set(rightY);
    System.out.print("Small ");
    System.out.println(rightY);
  }

  // HELPER FUNCTIONS

  /** Deadband with constant percent */
  double Deadband(double value) {
    return Deadband(value, DEADBAND);
  }

	/** Deadband percent passed in */
	double Deadband(double value, double def_deadband) {
		/* Upper deadband */
		if (value >= +def_deadband) 
			return value;
		
		/* Lower deadband */
		if (value <= -def_deadband)
			return value;
		
		/* Outside deadband */
		return 0;
	}
}
