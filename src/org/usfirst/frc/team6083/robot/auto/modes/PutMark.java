package org.usfirst.frc.team6083.robot.auto.modes;

import org.usfirst.frc.team6083.robot.auto.AutoEngine;

public class Baseline extends AutoEngine {

	private static final double zoneDis = 105;// In inch

	public static void loop() {
		switch (step) {
		case 0:
			currentStep = "Walk 1";
			walk(zoneDis);
			break;
		default:
			currentStep = "none";
			leftSpeed = 0;
			rightSpeed = 0;
			break;
		}
	}

}
