//CB-4 "FARQUAD" REMASTER

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  
  //Auto Chooser 
  private static final String NoAutoSelected = "No Auto Selected";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

   //Motors
   private final PWMVictorSPX frontleftMotor = new PWMVictorSPX(1);
   private final PWMVictorSPX backleftMotor = new PWMVictorSPX(2);
   private final PWMVictorSPX frontrightMotor = new PWMVictorSPX(3);
   private final PWMVictorSPX backrightMotor = new PWMVictorSPX(4);
   private final PWMVictorSPX shooterMotor = new PWMVictorSPX(5);
   private final PWMVictorSPX intakeMotor = new PWMVictorSPX(6);
   
   //Drivetrain
   private DifferentialDrive m_drive;

   //Controls
   private final Joystick rightstick = new Joystick(1);
   private final Joystick leftstick = new Joystick(0);
   private final XboxController xbox = new XboxController(2);

   //Timers
   private final Timer autoTimer = new Timer();
   private final Timer teleTimer = new Timer();

   //Pneumatics
   private final DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 6, 7);
   

  @Override
  public void robotInit() {

    //Auto Chooser
    m_chooser.setDefaultOption(NoAutoSelected, NoAutoSelected);
    SmartDashboard.putData("Auto Chooser", m_chooser);

    //Camera
    CameraServer.startAutomaticCapture();

    //Drivetrain
    frontrightMotor.setInverted(true);
    frontleftMotor.addFollower(backleftMotor);
    frontrightMotor.addFollower(backrightMotor);
    m_drive = new DifferentialDrive(frontleftMotor, frontrightMotor);
    
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {

    //Auto Chooser
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);

    //Auto Timer
    autoTimer.reset();
    autoTimer.start();
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    
    //Auto Timer
    autoTimer.stop();
    autoTimer.reset();

    //Tele Timer
    teleTimer.reset();
    teleTimer.start();
  }

  @Override
  public void teleopPeriodic() {

  // Tank drive with a given left and right rates
    m_drive.tankDrive(leftstick.getY(), rightstick.getY());

  // Shooter Motors Control
    if(xbox.getRightBumper()) { // Shooter Wheel
      shooterMotor.set(0.8);
    }  else if (xbox.getLeftBumper()) {
      shooterMotor.set(-0.3);  
    }
      else {
      shooterMotor.set(0);
    }

  // Intake Motors Control  
    if(xbox.getAButton()) { //Intakes Ball
      intakeMotor.set(0.7);
    } else if (xbox.getBButton()) {//Ejects Ball Out
      intakeMotor.set(-0.7); 
    }  else {
      intakeMotor.set(0.0);
    }

  // Backboard's Pneumatics Control
    if(xbox.getStartButton()) { //Backboard Extend
      solenoid.set(DoubleSolenoid.Value.kReverse);
    } else if(xbox.getBackButton()) { //Backboard Retract
      solenoid.set(DoubleSolenoid.Value.kForward);
   }

  }
      
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
