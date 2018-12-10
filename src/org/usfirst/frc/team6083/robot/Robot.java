/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6083.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.VictorSP;
import org.team6083.drive.DifferentialDrive;
import org.team6083.util.Joysticks;
import org.usfirst.frc.team6083.robot.auto.AutoEngine;

public class Robot extends IterativeRobot {
	private DifferentialDrive drive;
	private VictorSP left_1, left_2, right_1, right_2;
	
	@Override
	public void robotInit() {
		left_1 = new VictorSP(0);
		left_2 = new VictorSP(1);
		right_1 = new VictorSP(2);
		right_2 = new VictorSP(3);
		drive = new DifferentialDrive(left_1, left_2, right_1, right_2);
		
		AutoEngine.init();
		Joysticks.init();
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
	}

	
	@Override
	public void testPeriodic() {
	}
}
