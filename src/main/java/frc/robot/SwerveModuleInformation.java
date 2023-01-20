package frc.robot;

public class SwerveModuleInformation {
    
    public int motorId1, motorId2, encoderPin1, encoderPin2;

    public SwerveModuleInformation(int motorId1, int motorId2, int encoderPin1, int encoderPin2) {
        this.motorId1 = motorId1;
        this.motorId2 = motorId2;
        this.encoderPin1 = encoderPin1;
        this.encoderPin2 = encoderPin2;
    }
}
