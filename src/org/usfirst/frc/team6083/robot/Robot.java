/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6083.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;

import org.team6083.RobotPower;
import org.team6083.drive.DifferentialDrive;
import org.team6083.util.DashBoard;
import org.team6083.util.Joysticks;
import org.usfirst.frc.team6083.robot.auto.AutoEngine;

public class Robot extends IterativeRobot {
	public static DifferentialDrive drive;
	private static VictorSP left_1, left_2, right_1, right_2;
	public static RobotPower pobotpower;
	public static Servo markServo;
	
	@Override
	public void robotInit() {
		left_1 = new VictorSP(5);
		left_2 = new VictorSP(6);
		right_1 = new VictorSP(9);
		right_2 = new VictorSP(8);
		drive = new DifferentialDrive(left_1, left_2, right_1, right_2);
		markServo = new Servo(3);
		
		AutoEngine.init();
		Joysticks.init();
		DashBoard.init();
		RobotPower.init(1);
	}

	
	@Override
	public void autonomousInit() {
		AutoEngine.start();
	}

	
	@Override
	public void autonomousPeriodic() {
		AutoEngine.loop();
	}

	
	@Override
	public void teleopPeriodic() {
		Joysticks.update_data();
		drive.tankDrive();
		if(Joysticks.x) {
			markServo.set(1);
		}
		else if(Joysticks.y) {
			markServo.set(0);
		}
	}

	
	@Override
	public void testPeriodic() {
	}
}
