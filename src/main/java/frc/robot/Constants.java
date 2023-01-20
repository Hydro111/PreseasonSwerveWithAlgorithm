// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final int driveTrainMotorIdFL1 = 0; //TODO Add your motor ids here
    public static final int driveTrainMotorIdFL2 = 0;
    public static final int driveTrainMotorIdFR1 = 0;
    public static final int driveTrainMotorIdFR2 = 0;
    public static final int driveTrainMotorIdBL1 = 0;
    public static final int driveTrainMotorIdBL2 = 0;
    public static final int driveTrainMotorIdBR1 = 0;
    public static final int driveTrainMotorIdBR2 = 0;


    // H! Give left right and forward back distances to the wheels here
    public static final double wheelDistanceX = 3;
    public static final double wheelDistanceY = 5;

    // H! Encoder pins
    public static final int encoderPinFL1 = 0; //TODO Add your encoder pins here
    public static final int encoderPinFL2 = 0;
    public static final int encoderPinFR1 = 0;
    public static final int encoderPinFR2 = 0;
    public static final int encoderPinBL1 = 0;
    public static final int encoderPinBL2 = 0;
    public static final int encoderPinBR1 = 0;
    public static final int encoderPinBR2 = 0;


    // H! All swerveModule information in one structure
    public static final SwerveModuleInformation swerveModuleInformationFL = new SwerveModuleInformation(driveTrainMotorIdFL1, driveTrainMotorIdFL2, encoderPinFL1, encoderPinFL2);
    public static final SwerveModuleInformation swerveModuleInformationFR = new SwerveModuleInformation(driveTrainMotorIdFR1, driveTrainMotorIdFR2, encoderPinFR1, encoderPinFR2);
    public static final SwerveModuleInformation swerveModuleInformationBL = new SwerveModuleInformation(driveTrainMotorIdBL1, driveTrainMotorIdBL2, encoderPinBL1, encoderPinBL2);
    public static final SwerveModuleInformation swerveModuleInformationBR = new SwerveModuleInformation(driveTrainMotorIdBR1, driveTrainMotorIdBR2, encoderPinBR1, encoderPinBR2);


    // H! Determines how close to the optimal module for turning the robot a module should be so that it is used for turning
    public static final double usedForTurningThreshhold = 0.2 * Math.sqrt(Math.pow(wheelDistanceX,2) + Math.pow(wheelDistanceY,2));
}
