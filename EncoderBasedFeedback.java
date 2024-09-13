package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "week-three-auto", group = "MecanumBot")
public class EncoderBasedFeedback extends LinearOpMode {

    DcMotor back_right, back_left, front_right, front_left;

    //NEW SENSOR! The Internal Measurement unit
    IMU imu;



    @Override
    public void runOpMode() throws InterruptedException {
        back_right = hardwareMap.get(DcMotor.class,"back_right_motor");
        back_left = hardwareMap.get(DcMotor.class,"back_left_motor");
        front_right = hardwareMap.get(DcMotor.class,"front_right_motor");
        front_left = hardwareMap.get(DcMotor.class,"front_left_motor");

        //This autonomous is a demo of different ways to use motor encoders
        //Motor encoders are built in to the motors and will measure the internal rotations of the motor
        //DEAD WHEEL encoders are external pods built into the chassis of the robot that measure when a wheel connected to the encoder rotates
        //Dead wheel encoders are more complicated and expensive, but also accurate as they only change when the robot moves
        //Motor encoders can be inaccurate because internal rotations do not always correspond to actual movement of the bot
        //We will use motor encoders in this demo though

        //We reset each of our motor encoders to 0 ticks
        back_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        //The IMU should be on port 0 of the I2C
        //It finds angular acceleration, then integrates that twice to find heading
        imu = hardwareMap.get(IMU.class, "imu");


        back_right.setDirection(DcMotorSimple.Direction.REVERSE);
        front_right.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();

        //There are two different ways we can use motor encoder
        //The first is run-to-position
        //set a target encoder position to start
        back_left.setTargetPosition(1000);
        back_right.setTargetPosition(1000);
        front_left.setTargetPosition(1000);
        front_right.setTargetPosition(1000);
        //We set the mode to run_to_position
        back_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        front_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        front_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //get the motors running
        drive(1);
        //use a while loop
        //.isBusy returns true if this motor is working towards a target position and false if it is there
        while(back_left.isBusy()){
            telemetry.addData("encoder: ", back_left.getCurrentPosition());
            telemetry.update();
            continue;
        }
        drive(0);
        //This is pretty cool, but if you want more customization over velocity/acceleration, you do it yourself


        sleep(2000);

        //For the second method, we need to set mode to run without encoder, this for some reason makes the real robot faster
        back_right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //lets make a target variable
        //going to make this larger than 1000 because the encoders are already at 1000
        int target = 3000;

        //another while loop using the .getCurrentPosition method
        while(back_left.getCurrentPosition() < target){
            drive(1);
            telemetry.addData("encoder: ", back_left.getCurrentPosition());
            telemetry.update();
        }
        //once the while loop ends, we stop the motors
        drive(0);
        //Due to inertia, we are going to overshoot, we will talk about this solution later
        sleep(1000);
        //Now let's move on to the IMU
        //The IMU Heading starts at 0 and goes to 180
        //Everything past 180 is negative, and goes back down to zero
        //This is a frankly confusing system that makes little practical sense, so let's normalize our angles
        //We pass the value from imu.getAngularOrientation into our imuNormalize function
        //IDK what the first parameter does, second specifies the order in the list each heading is, third is what unit it is returned in
        //We will use the third angle from the Orientation object returned by the function
        //Depending on how you setup the IMU, your lateral heading axis will differ
        //here we need Z
        double heading = imuNormalize(imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle);
        //our heading is now normalized into a 0-360 range; if you turn clockwise, you are greater than target angle and vice versa
        telemetry.addData("heading: ", heading);
        telemetry.update();
        int target_heading = 270;
        //let's write a while loop
        turn(-0.6);
        while(heading > target_heading){
            //let's update our heading each time
            heading = imuNormalize(imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle);
            telemetry.addData("heading: ", heading);
            telemetry.update();
            turn(-0.6);
        }
        turn(0);

        //This method has a lot of overshoot, can you think of a way to fix this
        //EXTRA: if you've taken calc 1, try and work in your calculus skills into this solution


    }


    public void drive(double power){
        back_right.setPower(power);
        back_left.setPower(power);
        front_left.setPower(power);
        front_right.setPower(power);
    }
    //turn function, power on one side is reversed
    public void turn(double power){
        back_right.setPower(-power);
        back_left.setPower(power);
        front_left.setPower(power);
        front_right.setPower(-power);
    }

    //IMU normalization function
    //returns a double in DEGREES
    //takes in a double in DEGREES
    public double imuNormalize(double angle){
        double heading = angle;
        //Short hand if else statements
        //if heading is less than 0 but less than or equal to -90(what should be 270), add 360
        heading = (heading < 0.0) ? heading + 360 : heading;
        return heading;
    }//0-360 normalization
}
