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
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.cameraserver.CameraServer;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
//import edu.wpi.first.wpilibj.controller.PIDController;


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
  //private final PIDController m_controller = new PIDController(Kp, Ki, Kd);

  private final CANSparkMax l_spinner = new CANSparkMax(31, MotorType.kBrushless);
  private final CANSparkMax r_spinner = new CANSparkMax(32, MotorType.kBrushless);
  private final CANSparkMax pickupMotor = new CANSparkMax(34, MotorType.kBrushless);

  private final WPI_VictorSPX launcherLeader = new WPI_VictorSPX(41);
  private final WPI_VictorSPX launchLifter = new WPI_VictorSPX(42);

  public void robotInit() {

    m_left.setInverted(true);
    CameraServer.getInstance().startAutomaticCapture();
    
  }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    m_robotDrive.arcadeDrive(m_stick.getX(), m_stick.getY());

      
         launcherLeader.set(0);
         l_spinner.set(0);
         r_spinner.set(0);
         pickupMotor.set(0);
         launchLifter.set(0);
         boolean runner = false;
       if(runner == false){
           if(m_stick.getRawButton(1)){
               runner = true;
                 launcherLeader.set(-1);
                 l_spinner.set(1);
                  r_spinner.set(-1);    
           }
           else if(runner == true){
               if(m_stick.getRawButton(1)){
               runner = false;
                launcherLeader.set(0);
                l_spinner.set(0);
                r_spinner.set(0);    
               }              
           }
          }

          boolean beamup = false;
    if (beamup == false) {
      if (m_stick.getRawButton(2)) {
        beamup = true;
        launcherLeader.set(-.7);
        pickupMotor.set(.25);
      } else if (beamup == true) {
        if (m_stick.getRawButton(2)) {
          beamup = false;
          launcherLeader.set(0);
          pickupMotor.set(0);
        }
      }
    }

    boolean beamout = false;
    if (beamout == false) {
      if (m_stick.getRawButton(5)) {
        beamout = true;
        launcherLeader.set(.7);
      } else if (beamout == true) {
        if (m_stick.getRawButton(5)) {
          beamout = false;
          launcherLeader.set(0);
        }
      }
    }

       boolean belt = false;
       if(belt == false){
           if(m_stick.getRawButton(4)){
               belt = true;
               launchLifter.set(1);    
           }
           else if(belt == true){
               if(m_stick.getRawButton(4)){
               belt = false;
               launchLifter.set(0);    
               }              
           }
          }

       boolean drop = false;
       if(drop == false){
           if(m_stick.getRawButton(3)){
               drop = true;
               launchLifter.set(-1);    
           }
           else if(drop == true){
               if(m_stick.getRawButton(3)){
               drop = false;
               launchLifter.set(0);    
               }              
           }

          }    
   if(m_stick.getRawButton(7)){
     launcherLeader.set(-1);
   } 
   
    if (m_stick.getRawButton(8)) {
      launcherLeader.set(0);
    }

  }
  

}


