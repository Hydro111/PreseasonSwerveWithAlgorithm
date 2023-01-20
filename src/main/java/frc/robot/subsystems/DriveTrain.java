// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.Utils;
import frc.robot.microsystems.DiferentialSwerveModule;
import frc.robot.microsystems.SwerveModule;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.ArrayList;

import javax.vecmath.Vector2d;

public class DriveTrain extends SubsystemBase {
  /** Creates a new DriveTrain. */
  public DriveTrain() {}

  public SwerveModule moduleFrontLeft  =  new DiferentialSwerveModule(Constants.swerveModuleInformationFL);
  public SwerveModule moduleFrontRight =  new DiferentialSwerveModule(Constants.swerveModuleInformationFR);
  public SwerveModule moduleBackLeft   =  new DiferentialSwerveModule(Constants.swerveModuleInformationBL);
  public SwerveModule moduleBackRight  =  new DiferentialSwerveModule(Constants.swerveModuleInformationBR);
  
  /** Sets wheel speed and swivel speed for each module individually. 
   * Avoid using this function when ever possible */
  public void setSpeedsWheelSwivel(
    double wheelSpeedFL, double swivelSpeedFL,
    double wheelSpeedFR, double swivelSpeedFR,
    double wheelSpeedBL, double swivelSpeedBL,
    double wheelSpeedBR, double swivelSpeedBR) {
      
    moduleFrontLeft.setWheelSpeedSwivelSpeed( wheelSpeedFL, swivelSpeedFL);
    moduleFrontRight.setWheelSpeedSwivelSpeed(wheelSpeedFR, swivelSpeedFR);
    moduleBackLeft.setWheelSpeedSwivelSpeed(  wheelSpeedBL, swivelSpeedBL);
    moduleBackRight.setWheelSpeedSwivelSpeed( wheelSpeedBR, swivelSpeedBR);
  }


  /** Sets wheel speed and swivel speed for all modules */
  public void setEqualSpeedsWheelSwivel(double wheelSpeed, double swivelSpeed) {
    moduleFrontLeft.setWheelSpeedSwivelSpeed(wheelSpeed, swivelSpeed);
    moduleFrontRight.setWheelSpeedSwivelSpeed(wheelSpeed, swivelSpeed);
    moduleBackLeft.setWheelSpeedSwivelSpeed(wheelSpeed, swivelSpeed);
    moduleBackRight.setWheelSpeedSwivelSpeed(wheelSpeed, swivelSpeed);
  }

  /** Makes the swerve drive move and rotate, using wheel speed diferences to rotate
   * @param xSpeed determines the speed to the right
   * @param ySpeed determines the speed forward
   * @param zRotation determines the rotaion speed
   */
  public void setMovementValues(double xSpeed, double ySpeed, double zRotation) {
    class SwerveModulePackage {
      SwerveModule swerveModule;
      double speed;
      double efficiency;
      SwerveModulePackage pair;

      SwerveModulePackage(SwerveModule swerveModule, Vector2d speed, double efficiency) {
        this.swerveModule = swerveModule;
        this.speed = speed.length();
        this.efficiency = efficiency;
      }

      public void setPair(SwerveModulePackage pair) {
        this.pair = pair;
      }
    }




    Vector2d speed = new Vector2d(xSpeed, ySpeed);
    if (speed.lengthSquared() > 1) {
      speed.normalize();
    }
    Vector2d speedDirection = new Vector2d(0,0);
    speedDirection.normalize(speed);



    Vector2d modulePerpendicularFL = new Vector2d( Constants.wheelDistanceY,  Constants.wheelDistanceX);
    Vector2d modulePerpendicularFR = new Vector2d( Constants.wheelDistanceY, -Constants.wheelDistanceX);
    Vector2d modulePerpendicularBL = new Vector2d(-Constants.wheelDistanceY,  Constants.wheelDistanceX);
    Vector2d modulePerpendicularBR = new Vector2d(-Constants.wheelDistanceY, -Constants.wheelDistanceX);
    modulePerpendicularFL.normalize();
    modulePerpendicularFR.normalize();
    modulePerpendicularBL.normalize();
    modulePerpendicularBR.normalize();
    double efficiencyFL = speedDirection.dot(modulePerpendicularFL);
    double efficiencyFR = speedDirection.dot(modulePerpendicularFR);
    double efficiencyBL = speedDirection.dot(modulePerpendicularBL);
    double efficiencyBR = speedDirection.dot(modulePerpendicularBR);


    SwerveModulePackage modulePackageFL = new SwerveModulePackage(moduleFrontLeft, speed, efficiencyFL);
    SwerveModulePackage modulePackageFR = new SwerveModulePackage(moduleFrontRight, speed, efficiencyFR);
    SwerveModulePackage modulePackageBL = new SwerveModulePackage(moduleBackLeft, speed, efficiencyBL);
    SwerveModulePackage modulePackageBR = new SwerveModulePackage(moduleBackRight, speed, efficiencyBR);

    modulePackageFL.setPair(modulePackageBR);
    modulePackageFR.setPair(modulePackageBL);
    modulePackageBL.setPair(modulePackageFR);
    modulePackageBR.setPair(modulePackageFL);




    double maxEfficiency = Utils.maxArray(new Double[]{Math.abs(efficiencyFL), Math.abs(efficiencyFR), Math.abs(efficiencyBL), Math.abs(efficiencyBR)});


    ArrayList<SwerveModulePackage> usedForTurningP = new ArrayList<SwerveModulePackage>();

    if (efficiencyFL + Constants.usedForTurningThreshhold >= maxEfficiency) {
      if (efficiencyFL * Math.signum(zRotation) > 0) {
        usedForTurningP.add(modulePackageFL);
      }
    }
    if (efficiencyFR + Constants.usedForTurningThreshhold >= maxEfficiency) {
      if (efficiencyFR * Math.signum(zRotation) > 0) {
        usedForTurningP.add(modulePackageFR);
      }
    }
    if (efficiencyBL + Constants.usedForTurningThreshhold >= maxEfficiency) {
      if (efficiencyBL * Math.signum(zRotation) > 0) {
        usedForTurningP.add(modulePackageBL);
      }
    }
    if (efficiencyBR + Constants.usedForTurningThreshhold >= maxEfficiency) {
      if (efficiencyBR * Math.signum(zRotation) > 0) {
        usedForTurningP.add(modulePackageBR);
      }
    }


    // New, shiny code
    for (SwerveModulePackage swerveModulePackage : usedForTurningP) {
      double maxdifP = 1. - swerveModulePackage.speed;
      double maxdifN = 1. + swerveModulePackage.speed;
      double maxdif = maxdifP < maxdifN ? maxdifP : maxdifN;

      swerveModulePackage.speed += maxdif;
      swerveModulePackage.pair.speed -= maxdif;
    }


    /* Bad old code ()
    usedForTurningP.sort( (v1, v2) -> -((Double) (Math.abs(v1.efficiency))).compareTo(Math.abs(v2.efficiency)) );
    usedForTurningN.sort( (v1, v2) -> -((Double) (Math.abs(v1.efficiency))).compareTo(Math.abs(v2.efficiency)) );


    SwerveModulePackage currentHighestEffP = null;
    SwerveModulePackage currentHighestEffN = null;
    double currentCapacityP = 0;
    double currentCapacityN = 0;
    double leastCapacity = 0;

    while (usedForTurningP.size() > 0 && usedForTurningN.size() > 0) {
      if (currentHighestEffP == null) {
        currentHighestEffP = usedForTurningP.remove(0);
      }
      if (currentHighestEffN == null) {
        currentHighestEffN = usedForTurningN.remove(0);
      }

      if (currentCapacityP == 0) {
        currentCapacityP = (1. - Math.abs(speedMagnitude));
      }
      if (currentCapacityN == 0) {
        currentCapacityN = (1. + Math.abs(speedMagnitude));
      }
      

      if (currentCapacityP < currentCapacityN) {
        leastCapacity = currentCapacityP;
      } else {
        leastCapacity = currentCapacityN;
      }

      if (Math.abs(leastCapacity * currentHighestEffP.efficiency) + Math.abs(leastCapacity * currentHighestEffN.efficiency) > remainingZRotation) {
        leastCapacity = remainingZRotation / (Math.abs(currentHighestEffP.efficiency) + Math.abs(currentHighestEffN.efficiency));
        
        speedDirection.scale(leastCapacity);
        currentHighestEffP.speed.scaleAdd(1, speedDirection);
        speedDirection.scale(1/leastCapacity);

        speedDirection.scale(-leastCapacity);
        currentHighestEffN.speed.scaleAdd(1, speedDirection);
        speedDirection.scale(-1/leastCapacity);
        break;
      }

      currentCapacityP -= leastCapacity;
      currentCapacityN -= leastCapacity;

      speedDirection.scale(leastCapacity);
      currentHighestEffP.speed.scaleAdd(1, speedDirection);
      speedDirection.scale(1/leastCapacity);

      speedDirection.scale(-leastCapacity);
      currentHighestEffN.speed.scaleAdd(1, speedDirection);
      speedDirection.scale(-1/leastCapacity);

      remainingZRotation -= Math.abs(leastCapacity * currentHighestEffP.efficiency) + Math.abs(leastCapacity * currentHighestEffN.efficiency);

      if (currentCapacityP == 0) {
        currentHighestEffP = null;
      }
      if (currentCapacityN == 0) {
        currentHighestEffN = null;
      }
    }
    */
    modulePackageFL.swerveModule.setWheelSpeedSwivelRotation(modulePackageFL.speed, Math.signum(speed.getX()) * (180./Math.PI) * speed.angle(new Vector2d(0,1)));
    modulePackageFR.swerveModule.setWheelSpeedSwivelRotation(modulePackageFR.speed, Math.signum(speed.getX()) * (180./Math.PI) * speed.angle(new Vector2d(0,1)));
    modulePackageBL.swerveModule.setWheelSpeedSwivelRotation(modulePackageBL.speed, Math.signum(speed.getX()) * (180./Math.PI) * speed.angle(new Vector2d(0,1)));
    modulePackageBR.swerveModule.setWheelSpeedSwivelRotation(modulePackageBR.speed, Math.signum(speed.getX()) * (180./Math.PI) * speed.angle(new Vector2d(0,1)));

  }


  public void disable() {
    moduleFrontLeft.disable();
    moduleFrontRight.disable();
    moduleBackLeft.disable();
    moduleBackRight.disable();
  }

  public void enable() {
    moduleFrontLeft.enable();
    moduleFrontRight.enable();
    moduleBackLeft.enable();
    moduleBackRight.enable();
  }



  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    moduleFrontLeft.periodic();
    moduleFrontRight.periodic();
    moduleBackLeft.periodic();
    moduleBackRight.periodic();
  }
}
