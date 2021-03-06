/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
// import edu.wpi.first.wpilibj.controller.PIDController;

import org.opencv.core.Mat;


/**
 * This is a demo program showing the use of the DifferentialDrive class.
 * Runs the motors with arcade steering.
 */
public class Robot extends TimedRobot {
  private final WPI_VictorSPX m_leftFront = new WPI_VictorSPX(11);
  private final WPI_VictorSPX m_leftFollower = new WPI_VictorSPX(12);
  private final SpeedControllerGroup m_left = new SpeedControllerGroup(m_leftFront,m_leftFollower);

  private final WPI_VictorSPX m_rightFront = new WPI_VictorSPX(21);
  private final WPI_VictorSPX m_rightFollower = new WPI_VictorSPX(22);
  private final SpeedControllerGroup m_right = new SpeedControllerGroup(m_rightFront, m_rightFollower);

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_left, m_right);
  private final Joystick m_stick = new Joystick(0);
  private final Joystick m_controller = new Joystick(1);

  // private final PIDController m_controller = new PIDController

  private final CANSparkMax l_spinner = new CANSparkMax(31, MotorType.kBrushless);
  private final CANSparkMax r_spinner = new CANSparkMax(32, MotorType.kBrushless);
  private final WPI_VictorSPX pickupMotor = new WPI_VictorSPX(35);

  private final WPI_VictorSPX launcherLeader = new WPI_VictorSPX(41);
  private final WPI_VictorSPX launchLifter = new WPI_VictorSPX(42);

  private double startTime;

  public void robotInit() {

    m_left.setInverted(true);

  //mts - Attempting to copy camera code from Chief Delphi
    Thread t = new Thread(() -> {

      boolean allowCam1 = false;

      UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
      camera1.setResolution(320, 240);
      camera1.setFPS(30);
      UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(1);
      camera2.setResolution(320, 240);
      camera2.setFPS(30);

      CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
      CvSink cvSink2 = CameraServer.getInstance().getVideo(camera2);
      CvSource outputStream = CameraServer.getInstance().putVideo("Switcher", 320, 240);

      Mat image = new Mat();

      while (!Thread.interrupted()) {

        //!!!! Fix this button mapping !!!!
        if (m_stick.getRawButton(100)) {
          allowCam1 = !allowCam1;
        }

        if (allowCam1) {
          cvSink2.setEnabled(false);
          cvSink1.setEnabled(true);
          cvSink1.grabFrame(image);
        } else {
          cvSink1.setEnabled(false);
          cvSink2.setEnabled(true);
          cvSink2.grabFrame(image);
        }

        outputStream.putFrame(image);
      }

    });
    t.start();

   
  }

  @Override
  public void autonomousInit() {
    startTime = Timer.getFPGATimestamp();
  }

  @Override
  public void autonomousPeriodic() {
    
    double time = Timer.getFPGATimestamp();
  
  if (time - startTime < 3) {
    m_left.set(.2);
    m_right.set(-.2);
    
  } else {
    m_left.set(0);
    m_right.set(0);

  }

  }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    m_robotDrive.arcadeDrive(m_stick.getX()/-1, m_stick.getY());

    /*
    double value;
    value = m_controller.getX();
    value = m_controller.getY();
    value = m_controller.getZ();
    value = m_controller.getThrottle();
    value = m_controller.getTwist();
     */ 
         launcherLeader.set(0);
         l_spinner.set(0);
         r_spinner.set(0);
         pickupMotor.set(0);
         boolean runner = false;
       if(runner == false){
           if(m_stick.getRawButton(1)){
               runner = true;
                 l_spinner.set(1);
                  r_spinner.set(-1);    
           }
           else if(runner == true){
               if(m_stick.getRawButton(1)){
               runner = false;
                l_spinner.set(0);
                r_spinner.set(0);    
               }              
           }
          }

          boolean beamup = false;
    if (beamup == false) {
      if (m_stick.getRawButton(2)) {
        beamup = true;
        launcherLeader.set(-.4);
        pickupMotor.set(-.5);
        l_spinner.set(-.2);
        r_spinner.set(.2);
      } else if (beamup == true) {
        if (m_stick.getRawButton(2)) {
          beamup = false;
          launcherLeader.set(0);
          pickupMotor.set(0);
          l_spinner.set(0);
          r_spinner.set(0);
        }
      }
    }

    boolean beamout = false;
    if (beamout == false) {
      if (m_stick.getRawButton(3)) {
        beamout = true;
        launcherLeader.set(.4);
        l_spinner.set(-.4);
        r_spinner.set(.4);
      } else if (beamout == true) {
        if (m_stick.getRawButton(5)) {
          beamout = false;
          launcherLeader.set(0);
          l_spinner.set(0);
          r_spinner.set(0);
        }
      }
    }
           if(m_stick.getRawButton(7)){
               launchLifter.set(1);    
          }
       
           if(m_stick.getRawButton(8)){
               launchLifter.set(-1);   
          }    

          if (m_stick.getRawButton(9)) {
            launchLifter.set(0);
          }

   if(m_stick.getRawButton(4)){
     launcherLeader.set(-1);
   } 

    if (m_stick.getRawButton(5)) {
      pickupMotor.set(.4);
    }
   

  
    //End teleopPeriodic
  }
  

}


