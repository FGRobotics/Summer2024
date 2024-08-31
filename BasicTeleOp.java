//This is the package statement, it must be the first line of code in every java file
//Essentially, every class created in this file will be part of the package
package org.firstinspires.ftc.teamcode;

//So what is a Java class?
//Think of it like an object, it has attributes and functions
//A car class for example, might have attributes like color, engine type.. & functions like drive, reverse...

//Below, we import some classes
//This allows us to have access to code that other people created for us to use(we don't have to reinvent the wheel every time we program)

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.robotcore.external.navigation.*;

//The @ symbol adds a special attribute to the class
//This one denotes that you can find this program in the group "MecanumBot" under the name "tele op" in the driver station
//In onBot Java, you can delete the group variable and just leave it : @TeleOp(name = "tele op")
@TeleOp(name = "tele op", group = "MecanumBot")

//Now we create our tele-op class
//The 'extends LinearOpMode' part denotes that this class will INHERIT the everything of a pre created LinearOpMode class
//If you look at the imports section, LinearOpMode was imported there
//public means that the attributes of this class are directly available to outside files
public class BasicTeleOp extends LinearOpMode {

    //On this line, if this was onBot java, you would include the statement below(obviously without the comment)
    //@Override
    //override makes sure that this function takes priority over the inherited function from the LinearOpMode class
    //if you don't understand it right now, don't fret it

    //This is the runOpMode function, an overridden function from LinearOpMode
    //A function is a defined piece of code that can be called multiple times
    //The code here is what happens when the program is run
    //the 'void' means that this function isn't expected to return anything
    public void runOpMode(){
        //Here we intialize the motors
        //We need to create variables from the Hardware Classes
        //These lines of code create instances of DcMotor Objects
        //The hardwareMap.dcMotor.get function assigns the motor we configured on the actual robot to the variable we just created
        //make sure whatever is inside the .get() matches the name in the configuration settings
        DcMotor back_left = hardwareMap.dcMotor.get("back_left_motor");
        DcMotor front_left = hardwareMap.dcMotor.get("front_left_motor");
        DcMotor front_right = hardwareMap.dcMotor.get("front_right_motor");
        DcMotor back_right = hardwareMap.dcMotor.get("back_right_motor");

        //DcMotor class has a ton of functions
        //if you are ever confused about what to use, just type '.' after DcMotor and android studio will autocomplete
        //The functions below are pretty self explanatory - they reverse the right side motors
        back_right.setDirection(DcMotor.Direction.REVERSE);
        front_right.setDirection(DcMotor.Direction.REVERSE);




        //telemetry is the text you see on the side of the driver station
        //you must call a telemetry.update(); for the code to be read
        telemetry.addData("Press Start When Ready","");
        telemetry.update();

        //waitForStart() is a function that creates a listener
        //the code won't execute later lines because it is waiting for the program to be started
        waitForStart();

        //This is a while loop - which repeats the code inside the brackets until the condition isn't true anymore
        //opModeIsActive is a function that returns true if the program is running and false if it isn't
        while (opModeIsActive()){
            //Now we set a variable for left and right motor power
            //a variable is a value
            //you declare a variable by : type name = value;
            //there are many different number data types for variables
            //An 'int' is an integer - a whole number
            //Doubles and floats can have decimals, but doubles have 64 bit storage while floats have 32
            double left_power = gamepad1.left_stick_y;
            double right_power = gamepad1.right_stick_y;


            //Now we call the set power function to assign our power variables to each side
            back_left.setPower(left_power);
            front_left.setPower(left_power);
            back_right.setPower(right_power);
            front_right.setPower(right_power);

        }
    }
}
