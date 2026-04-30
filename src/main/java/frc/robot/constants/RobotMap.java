// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.constants;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

/** CAN ID, Invert, Pod Positions, Offsets, Conversion Rates */
public final class RobotMap {
    public static final int leftAzimuthID = 17;// BL
    public static final int rightAzimuthID = 14;// FR

    public static final int leftDriveID = 18;// BL
    public static final int rightDriveID = 13;// FR

    public static final int leftAbsoluteEncoderID = 4;
    public static final int rightAbsoluteEncoderID = 3;

    public static final int pigeonID = 25;

    public static final boolean leftAzimuthInvert = false;
    public static final boolean rightAzimuthInvert = false;
    public static final boolean leftDriveInvert = true;
    public static final boolean rightDriveInvert = true;

    public static final Translation2d robotCenter = new Translation2d(0, 0); // serves as "center of robot for
                                                                             // calculations; robot will turn about this
                                                                             // point
    public static final Translation2d leftPodPosition = new Translation2d(-Units.inchesToMeters(5.5),
            Units.inchesToMeters(5.5)); // units in meters
    public static final Translation2d rightPodPosition = new Translation2d(Units.inchesToMeters(5.5),
            -Units.inchesToMeters(5.5));
    public static final SwerveDriveKinematics drivetrainKinematics = new SwerveDriveKinematics(
            robotCenter.plus(leftPodPosition), robotCenter.plus(rightPodPosition));

    public static final double leftAbsoluteEncoderOffset = 1.2916118233997098;// absolute encoder reading at position
    public static final double rightAbsoluteEncoderOffset = 0.044485442848683596;// gears facing inwards: fwd/bck TODO:
                                                                                // less janky alignment
    
    
    // Calculations required for driving motor conversion factors and feed forward
    public static final double kFreeSpeedRpm = 5676;
    public static final double kDrivingMotorFreeSpeedRps = kFreeSpeedRpm / 60;

    public static final double kWheelDiameterMeters = Units.inchesToMeters(2);
    public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
    // NS 33T motor pinion, 45T wheel
    public static final double kDrivingMotorReduction = 45.0 / 33.0;
    public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters)
            / kDrivingMotorReduction;
    public static final double drivingVelocityFeedForward = 1 / kDriveWheelFreeSpeedRps;

    // Circumference (pi * d) / (drive reduction ratio)
    public static final double driveMetersPerMotorRotation = Math.PI * kWheelDiameterMeters / kDrivingMotorReduction;
    public static final double azimuthRadiansPerMotorRotation = 2 * Math.PI * 18.0 / 33.0;
    public static final double absoluteRadiansPerEncoderRotation = 2 * Math.PI;

}
