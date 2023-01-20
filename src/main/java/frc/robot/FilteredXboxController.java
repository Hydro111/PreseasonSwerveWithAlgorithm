// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Optional;

import edu.wpi.first.wpilibj.XboxController;

/** A modified XboxController with features including...
 * Deadzoning (That's it for now)
 */
public class FilteredXboxController extends XboxController {

    private double deadzoneSize;

    /** Creates a FilteredXboxController object 
     * @param port The port to be used
     * @param deadzoneSize The square radius of the deadzone. Defaults to 0.1
    */
    public FilteredXboxController(final int port, Optional<Double> deadzoneSize) {
        super(port);
        this.deadzoneSize = deadzoneSize.orElse(0.1);
    }

    @Override
    public double getRawAxis(int axis) {
        double rawValue = super.getRawAxis(axis);
        return Utils.clamp(rawValue, -deadzoneSize, deadzoneSize);
    }



}
