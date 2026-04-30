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

    // Somehow the RIO is getting stressed by calling this multiple times?
    Translation2d joystickLeftValue = joystickLeft.get();
    Translation2d joystickRightValue = joystickRight.get();
    
    double driveHypot = Math.hypot(joystickLeftValue.getX(), joystickLeftValue.getY());
    boolean deadband = Math.abs(driveHypot) < 0.07;
    // On the controller, Y is up (as it should be), but in robot coordinates, X is up
    // so you have to mix and match.
    double speedX = deadband ? 0 : joystickLeftValue.getY() * maxSpeed;
    double speedY = deadband ? 0 : joystickLeftValue.getX() * maxSpeed;
    
    
    double rightHypot = Math.hypot(joystickRightValue.getX(), joystickRightValue.getY());
    boolean turnDeadband = Math.abs(rightHypot) < 0.07;

    // If (in TurdSwerve.java) manualTurn = true, speedOmega is used.
    // If manualTurn = false, targetAngle is used. 
    double speedOmega = turnDeadband ? 0 : joystickRightValue.getX() * maxSpeed;
    double targetAngle = Math.atan2(joystickRightValue.getY(), joystickRightValue.getX()) + (Math.PI / 2);

    ChassisSpeeds speeds = new ChassisSpeeds(speedX, speedY, speedOmega);
    if(!turnDeadband) swerve.setTargetAngle(targetAngle);
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
