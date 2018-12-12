package org.usfirst.frc.team6083.robot.auto;

import org.team6083.auto.GyroWalker;
import org.team6083.util.DashBoard;
import org.usfirst.frc.team6083.robot.Robot;
import org.usfirst.frc.team6083.robot.auto.modes.Baseline;
import org.usfirst.frc.team6083.robot.auto.modes.Scale;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoEngine {
	protected static final String kDoNithing = "Do nothing";
	protected static final String kM1 = "Mode 1";
	protected static final String kM2 = "Mode 2";
	protected static String m_autoSelected;

	protected static final String kR = "Red";
	protected static final String kB = "Blue";
	protected static String allianceSelected;
	protected static int station;

	protected static String gameData;
	protected static int switchPos, scalePos;

	protected static SendableChooser<String> m_chooser = new SendableChooser<>();
	protected static SendableChooser<String> a_chooser = new SendableChooser<>();

	protected static ADXRS450_Gyro gyro;
	protected static GyroWalker gyrowalker;
	protected static Encoder leftEnc, rightEnc;

	protected static final int leftEnc_ChA = 8;
	protected static final int leftEnc_ChB = 9;
	protected static final int rightEnc_ChA = 0;
	protected static final int rightEnc_ChB = 1;
	protected static final double disPerStep = 0.05236;
	protected static final double kP = 0.005;
	protected static final double kI = 0.001;

	protected static double leftSpeed;
	protected static double rightSpeed;
	protected static double leftDistance;
	protected static double rightDistance;
	protected static DashBoard dash;

	protected static int step;
	protected static String currentStep = "";
	protected static Timer autoTimer = new Timer();

	public static void init() {
		dash = new DashBoard("AutoEngine");
		dash.putWarning();

		m_chooser.addDefault("Do nothing", kDoNithing);
		m_chooser.addObject("Move 1", kM1);
		m_chooser.addObject("Mode 2", kM2);
		SmartDashboard.putData("Auto choices", m_chooser);

		a_chooser.addDefault("Red", kR);
		a_chooser.addObject("Blue", kB);
		SmartDashboard.putData("Auto point choices", a_chooser);

		gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
		gyro.calibrate();
		gyrowalker = new GyroWalker(gyro);
		
		leftEnc = new Encoder(leftEnc_ChA, leftEnc_ChB);
		leftEnc.setReverseDirection(true);
		rightEnc = new Encoder(rightEnc_ChA, rightEnc_ChB);
		rightEnc.setReverseDirection(false);
		
		SmartDashboard.putNumber("autoDelay", 0);
		SmartDashboard.putString("CurrentStep", "wait to start");
		SmartDashboard.putNumber("kP", gyrowalker.getkP());
		SmartDashboard.putNumber("kI", gyrowalker.getkI());

		dash.putReady();
	}

	public static void start() {
		m_autoSelected = m_chooser.getSelected();
		allianceSelected = a_chooser.getSelected();
		System.out.println("Auto selected: " + m_autoSelected + " on " + allianceSelected);
		gyro.reset();
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		switch (allianceSelected) {
		case kR:
			station = 1;
			break;
		case kB:
			station = 2;
			break;
		default:
			station = 1;
			break;
		}
		
		step = 0;
		leftSpeed = 0;
		rightSpeed = 0;
		leftEnc.reset();
		rightEnc.reset();
		//Reset everything
		
		Timer.delay(SmartDashboard.getNumber("autoDelay", 0));
		gyrowalker.setTargetAngle(0);
	}

	public static void loop() {
		SmartDashboard.putNumber("Gyro/angle", GyroWalker.translateAngle(gyro.getAngle()));
		leftDistance = leftEnc.getDistance() * disPerStep;
		rightDistance = rightEnc.getDistance() * disPerStep;

		switch (m_autoSelected) {
		case kM1:
			Baseline.loop();
			break;
		case kM2:
			Scale.loop();
			break;
		case kDoNithing:
		default:
			currentStep = "DoNothing";
			leftSpeed = 0;
			rightSpeed = 0;
			gyrowalker.setTargetAngle(SmartDashboard.getNumber("Target Angle", gyrowalker.getTargetAngle()));
			break;
		}
		
		gyrowalker.calculate(leftSpeed, rightSpeed);
		
		leftSpeed = gyrowalker.getLeftPower();
		rightSpeed = gyrowalker.getRightPower();
		
		Robot.drive.directControl(leftSpeed, -rightSpeed);
		
		
		gyrowalker.setkP(SmartDashboard.getNumber("kP", 0));
		gyrowalker.setkI(SmartDashboard.getNumber("kI", 0));
		// set kP, kI
		
		SmartDashboard.putNumber("ki_resault", gyrowalker.getkI_result());
		SmartDashboard.putString("CurrentStep", currentStep);
		SmartDashboard.putNumber("Current Angle", gyrowalker.getCurrentAngle());
		SmartDashboard.putNumber("Target Angle", gyrowalker.getTargetAngle());
		SmartDashboard.putNumber("Error Angle", gyrowalker.getErrorAngle());
		SmartDashboard.putNumber("Left Dis", leftDistance);
		SmartDashboard.putNumber("Right Dis", rightDistance);
		SmartDashboard.putNumber("Timer", autoTimer.get());
	}

	protected static void nextStep() {
		step++;
		System.out.println("Finish step:"+currentStep);
		autoTimer.stop();
		autoTimer.reset();
		autoTimer.start();
		System.out.println("Encoder reset on "+ leftDistance +", "+ rightDistance);
		leftEnc.reset();
		rightEnc.reset();
	}

	public static void walk(double dis) {
		if (dis > 0) {
			if (leftDistance < dis) {
				leftSpeed = 0.4;
				rightSpeed = 0.4;
			} else {
				leftSpeed = 0;
				rightSpeed = 0;
			}
			if (rightDistance < dis) {
				rightSpeed = 0.4;
			} 
			else {
				rightSpeed = 0;
			}
			if (rightDistance > dis || leftDistance > dis) {
				rightSpeed = 0;
				leftSpeed = 0;
				nextStep();

			}
		} else {
			if (leftDistance > dis) {
				leftSpeed = -0.4;
				rightSpeed = -0.4;
			} else {
				leftSpeed = 0;
				rightSpeed = 0;
			}
			if (rightDistance > dis) {
				rightSpeed = -0.4;
			} else {
				rightSpeed = 0;
			} 
			if (rightDistance < dis || leftDistance < dis) {
				rightSpeed = 0;
				leftSpeed = 0;
				nextStep();

			}
		}

	}
	
	public static double getTranslateAngle() {
		return GyroWalker.translateAngle(gyro.getAngle());
	}
	
	public static double getAngle() {
		return gyro.getAngle();
	}
	
	public static double getLefttEncVal() {
		return leftEnc.getDistance() * disPerStep;
	}
	
	public static double getRightEncVal() {
		return rightEnc.getDistance() * disPerStep;
	}
}
