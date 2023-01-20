// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.microsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import frc.robot.SwerveModuleInformation;

public class DiferentialSwerveModule extends PIDSubsystem
  implements SwerveModule{

  /** Creates a new SwerveModule. */
  public DiferentialSwerveModule(int motorId1, int motorId2, int encoderPin1, int encoderPin2) {
    super(
      new PIDController(0.1, 0, 0), 
      0.0);
    motor1 = new CANSparkMax( motorId1, MotorType.kBrushless);
    motor2 = new CANSparkMax( motorId2, MotorType.kBrushless);
    encoder = new Encoder(encoderPin1, encoderPin2, false);
    encoder.setDistancePerPulse(360. / 8192.);
    
  }


  public DiferentialSwerveModule(SwerveModuleInformation packedInfo) {
    this(
      packedInfo.motorId1,
      packedInfo.motorId2,
      packedInfo.encoderPin1,
      packedInfo.encoderPin2);
  }


  MotorController motor1;
  MotorController motor2;

  Encoder encoder;

  private double currentWheelSpeed = 0;




  /** Sets the wheel speed and swivel speed of a swerve module */
  public void setWheelSpeedSwivelSpeed(double wheelSpeed, double swivelSpeed) {
    // H! Formulas that follow are dervied from the fact that wheelspeed = m1+m2, and swivelspeed = m1-m2
    motor1.set( (wheelSpeed + swivelSpeed)/2. );
    motor2.set( (wheelSpeed - swivelSpeed)/2. );
  }

  
  /** Sets the wheel speed and swivel location of a swerve module */
  public void setWheelSpeedSwivelRotation(double wheelSpeed, double swivelRotation) {
    currentWheelSpeed = wheelSpeed;
    this.setSetpoint(swivelRotation);
  }


  protected double getMeasurement() {
    return encoder.getDistance();
  }


  protected void useOutput(double output, double setpoint) {
    double wheelSpeed = 0.0;
    if (Math.abs(setpoint - getMeasurement()) <= 10) {
      wheelSpeed = currentWheelSpeed;
    } else if (Math.abs(setpoint - getMeasurement()) <= 20) {
      wheelSpeed = currentWheelSpeed * (Math.abs(setpoint - getMeasurement()) - 10) / 10.;
    }
    
    this.setWheelSpeedSwivelSpeed(wheelSpeed, output);

  }
}
