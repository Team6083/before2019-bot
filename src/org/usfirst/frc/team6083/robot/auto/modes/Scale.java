package org.usfirst.frc.team6083.robot.auto.modes;

import org.usfirst.frc.team6083.robot.auto.AutoEngine;

public class Scale extends AutoEngine {

	public static void loop() {
		switch (step) {
		case 0:
			currentStep = "Walk1";
			walk(100);
			gyrowalker.setTargetAngle(0);
			break;
		default:
			currentStep = "none";
			break;
		}
	}

}
