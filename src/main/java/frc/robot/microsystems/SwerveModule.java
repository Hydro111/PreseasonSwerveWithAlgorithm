package frc.robot.microsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface SwerveModule extends Subsystem {
    
    public void setWheelSpeedSwivelSpeed(double wheelSpeed, double swivelSpeed);
    public void setWheelSpeedSwivelRotation(double wheelSpeed, double swivelRotation);
    public void enable();
    public void disable();
}
