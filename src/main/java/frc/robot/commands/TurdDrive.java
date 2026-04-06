// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.TurdSwerve;

public class TurdDrive extends Command {
  
  TurdSwerve swerve;
  Supplier<Translation2d> joystickRight, joystickLeft;
  Supplier<Integer> DPAD;
  Supplier<Boolean> boost;
  Rotation2d rotation = new Rotation2d();
  double maxSpeed = Constants.robotMaxSpeed;

  public TurdDrive(TurdSwerve swerve, Supplier<Translation2d> joystickLeft, Supplier<Translation2d> joystickRight, Supplier<Integer> DPAD, Supplier<Boolean> boost) {
    this.swerve = swerve;
    this.joystickRight = joystickRight;
    this.joystickLeft = joystickLeft;
    this.DPAD = DPAD;
    this.boost = boost;
    addRequirements(swerve);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    swerve.resetGyro();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if (DPAD.get() != -1) {
      swerve.targetAngle = -Units.degreesToRadians(DPAD.get());
    }

    if (boost.get()) {
      swerve.setAmpLimit(Constants.driveTopAmpLimit);
      maxSpeed = 1;
    } else {
      swerve.setAmpLimit(Constants.driveAmpLimit);
      maxSpeed = Constants.robotMaxSpeed;
    }
    
    double leftHypot = Math.hypot(joystickLeft.get().getX(), joystickLeft.get().getY());
    boolean deadband = Math.abs(leftHypot) < 0.05;
    // On the controller, Y is up (as it should be), but in robot coordinates, X is up
    // so you have to mix and match.
    double speedX = deadband ? 0 : joystickLeft.get().getY() * maxSpeed;
    double speedY = deadband ? 0 : joystickLeft.get().getX() * maxSpeed;
    
    
    double rightHypot = Math.hypot(joystickRight.get().getX(), joystickRight.get().getY());
    boolean turnDeadband = Math.abs(rightHypot) < 0.05;
    double speedOmega = turnDeadband ? 0 : -joystickRight.get().getX() * maxSpeed;


    ChassisSpeeds speeds = new ChassisSpeeds(speedX, speedY, speedOmega);
    swerve.setRobotSpeeds(speeds);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
