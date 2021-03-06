package org.usfirst.frc.team6083.robot.auto.modes;

import org.usfirst.frc.team6083.robot.Robot;
import org.usfirst.frc.team6083.robot.auto.AutoEngine;


public class PutMark extends AutoEngine {

	private static final double zoneDis = 95;// In inch
	private static final double walk2 = 25;
	private static final double walk3 = 39;

	public static void loop() {
		switch (step) {
		case 0:
			currentStep = "Walk 1";
			walk(zoneDis);
			break;
		case 1:
			currentStep = "Set Turn 1";
			gyrowalker.setTargetAngle((station == 2)?90:-90);
			leftSpeed = 0;
			rightSpeed = 0;
			if(autoTimer.get()>0.5) {
				// wait a second before turn
				nextStep();
			}
			break;
		case 2:
			currentStep = "Turn 1";
			leftSpeed = 0;
			rightSpeed = 0;
			if (gyrowalker.getErrorAngle() < 10&&autoTimer.get()>1) {
				nextStep();
			}
			break;
		case 3:
			currentStep = "Put 1";
			leftSpeed = 0;
			rightSpeed = 0;
			if(autoTimer.get()>1) {
				markOut();
				nextStep();
			}
			break;
		case 4:
			currentStep = "Walk 2";
			walk(walk2);
			break;
		case 5:
			currentStep = "Put 2";
			leftSpeed = 0;
			rightSpeed = 0;
			if(autoTimer.get()>1) {
				markOut();
				nextStep();
			}
			break;
		case 6:
			currentStep = "Walk 3";
			walk(walk3);
			break;
		case 7:
			currentStep = "Put 3";
			leftSpeed = 0;
			rightSpeed = 0;
			if(autoTimer.get()>1) {
				markOut();
				nextStep();
			}
			break;
		default:
			currentStep = "none";
			leftSpeed = 0;
			rightSpeed = 0;
			gyrowalker.setTargetAngle(gyrowalker.getTargetAngle());
			break;
		}
	}

}
