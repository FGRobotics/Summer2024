package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "week-two", group = "MecanumBot")
public class BasicAutoWeekTwo extends LinearOpMode {
    DcMotor back_right, back_left, front_right, front_left;
    DistanceSensor frontdist;
    @Override
    public void runOpMode() throws InterruptedException {

        back_right = hardwareMap.get(DcMotor.class,"back_right_motor");
        back_left = hardwareMap.get(DcMotor.class,"back_left_motor");
        front_right = hardwareMap.get(DcMotor.class,"front_right_motor");
        front_left = hardwareMap.get(DcMotor.class,"front_left_motor");

        //Distance sensor initialization
        frontdist = hardwareMap.get(DistanceSensor.class, "front_distance");

        back_right.setDirection(DcMotorSimple.Direction.REVERSE);
        front_right.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();
        //In this case, we don't need the while opMode is active loop, because that will repeat our autonomous forever and we don't want that
        //The goal of this week is to show off some basic autonomous navigation principles
        //If we want to get our robot to go someplace in auto, we have to run the drive motors, but how do we know where the robot is?
        //and when to stop?
        //There are two types of control, open loop feedback, and closed loop feedback

        //Well first, we could run our motors for a certain amount of time, this would be open loop feedback
        //There are too many factors at play for us to know where our robot is based on the time
        //HOWEVER - we plan to use time based movement as a fallback
        //if all of our sensor based code fails, the code will stop once we reach a certain time limit
        //let's demo :
        //Creating an object from the ElaspedTime class starts a timer
        //the .reset() method resets this timer
        ElapsedTime drive_timer = new ElapsedTime();
        drive_timer.reset();
        //We set up a while loop here using the function .seconds() from our elapsed time object as a conditional
        //The condition will be false after the timer has ran for 1.5 seconds
        while(drive_timer.seconds() < 1.5){
            //inside, we CALL our drive function, with full power
            drive(1);
        }
        //If we don't include the below line, our robot won't stop
        drive(0);

        //NOW let's write some closed loop feedback using the distance sensor
        //the distance sensor gives a kinda accurate reading
        //once we get under 10 cm, our program will stop
        while(frontdist.getDistance(DistanceUnit.CM) > 10){
            drive(-1);
        }
        drive(0);

    }


    //this is a method/function DECLARATION
    //One of the most common things people get confused with in Java is declaring vs calling
    //Declaring is when you set up the code to be run when a function is called
    //Calling is when you actually run the function
    //we first declare a scope(public or private)
    //public is visible to other classes, private is only visible to this class
    //Next we declare the variable type to be returned by the function, here it is void because we aren't returning anything
    //next, the function name
    //inside the parenthesis go arguments
    //arguments are values passed to the function for it to use when the function is called
    //here, we apply the power argument to the motors, so we don't have to write 4 lines of code each time
    public void drive(double power){
        back_right.setPower(power);
        back_left.setPower(power);
        front_left.setPower(power);
        front_right.setPower(power);
    }
}
