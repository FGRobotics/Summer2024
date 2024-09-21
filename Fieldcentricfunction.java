public void fieldCentricPlus() {
        double x, y, mag, rads, rangle;

        fieldCentricMultiplier = (fieldCentricMultiplier == 0) ? actfieldCentricMultiplier : fieldCentricMultiplier;
        x = fieldCentricMultiplier * (gamepad1.left_stick_x);
        y = fieldCentricMultiplier * (gamepad1.left_stick_y);

        double rotational = gamepad1.right_stick_x * rotationalMult;

        //find the resultant vector of the joystick
        mag = Math.pow(x, 2) + Math.pow(y, 2);
        mag = Math.sqrt(mag);
        //angle of the vector
        rads = Math.atan2(y, x);

        rads = (rads >= 0 && rads < Math.toRadians(270)) ? (-1 * rads) + Math.toRadians(90) : (-1 * rads) + Math.toRadians(450);
        //makes joystick angles go from 0(which is pointing the joystick straight up) to 180 back down to 0

        //find the robot angle
        rangle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle;
        //find angle robot needs to turn in order to reach joystick angle


        //normalizes two angles
        rads = (rads < 0) ? Math.toRadians(360) + rads : rads;
        rangle = (rangle < 0) ? Math.toRadians(360) + rangle : rangle;
        //subtracts reset position
        rads = rads - lastResetpos;
        rangle = rangle - lastResetpos;
        //turns those back positive
        rads = (rads < 0) ? Math.toRadians(360) + rads : rads;
        rangle = (rangle < 0) ? Math.toRadians(360) + rangle : rangle;
        //calculates the distance between joystick and heading as a positive int
        double turn = (rads < rangle) ? (Math.toRadians(360) - rangle) + (Math.abs(0 - rads)) : rads - rangle;
       
        double equationone = (Math.sin(turn + (Math.PI / 4)) * mag);
        double equationtwo = (Math.sin(turn - (Math.PI / 4)) * mag);


        rightFront.setPower((equationone + rotational));
        leftRear.setPower((equationone - rotational));
        rightRear.setPower((-equationtwo + rotational));
        leftFront.setPower((-equationtwo - rotational));


    }
