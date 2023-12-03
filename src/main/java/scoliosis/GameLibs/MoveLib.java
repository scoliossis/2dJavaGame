package scoliosis.GameLibs;

import scoliosis.Libs.KeyLib;

import java.awt.event.KeyEvent;

import static scoliosis.Game.*;
import static scoliosis.GameLibs.Velocity.*;
import static scoliosis.Libs.ScreenLib.height;

public class MoveLib {

    public static boolean onGround = false;
    public static boolean onWall = false;
    public static boolean onCeiling = false;


    public static int wallx = 0;
    public static int wallw = 0;
    public static int floory = 0;


    static double leftovertime = 0;

    static double timelast = System.currentTimeMillis();
    static double timesinceleftground = System.currentTimeMillis();
    static double timesincespeedtoggle = System.currentTimeMillis();


    static boolean onground = MoveLib.onGround;

    static boolean sprintedInAir = false;

    public static void MoveLibChecks() {

        if (((System.currentTimeMillis() - timelast) + leftovertime) / 20 >= 1f) {


            // in case of low fps
            leftovertime = (System.currentTimeMillis() - timelast) - 50;

            while (leftovertime >= 50) {
                leftovertime-=50;
                xcoordinate += (int) xvelocity;
            }

            onCeiling = false;
            onGround = false;
            onWall = false;
            boolean doyVelocity = true;

            for (int i = 0; i < levelreaderSplit.length; i+=6) {

                if (Integer.parseInt(levelreaderSplit[i+5]) == 0) {
                    // check if over a x coordinate
                    if (Integer.parseInt(levelreaderSplit[i]) < xcoordinate + 25 && Integer.parseInt(levelreaderSplit[i]) + Integer.parseInt(levelreaderSplit[i+2]) >= xcoordinate) {

                        // coliding with wall check
                        if (Integer.parseInt(levelreaderSplit[i+1]) + Integer.parseInt(levelreaderSplit[i+3]) >= ycoordinate && Integer.parseInt(levelreaderSplit[i+1]) <= ycoordinate - 5) {
                            wallx = (int) Integer.parseInt(levelreaderSplit[i]);
                            wallw = (int) Integer.parseInt(levelreaderSplit[i+2]);
                            onWall = true;

                            if (yvelocity < 2f) {
                                yvelocity += 2f;
                            }

                        }


                        // on ground check
                        else if (Integer.parseInt(levelreaderSplit[i+1]) <= ycoordinate && Integer.parseInt(levelreaderSplit[i+1]) + Integer.parseInt(levelreaderSplit[i+3]) >= ycoordinate) {
                            floory = (int) Integer.parseInt(levelreaderSplit[i+1]);
                            onGround = true;
                            sprintedInAir = false;
                        }

                        // stops going through ceiling
                        if (Integer.parseInt(levelreaderSplit[i+1]) + Integer.parseInt(levelreaderSplit[i+3]) <= ycoordinate && Integer.parseInt(levelreaderSplit[i+1]) + Integer.parseInt(levelreaderSplit[i+3]) >= ycoordinate - yvelocity && !onWall && !onGround) {
                            yvelocity *= -2f;
                        }


                        // stops phasing through floor at high speeds
                        if (ycoordinate <= Integer.parseInt(levelreaderSplit[i+1]) + Integer.parseInt(levelreaderSplit[i+3]) && ycoordinate - yvelocity >=Integer.parseInt(levelreaderSplit[i+1]) + Integer.parseInt(levelreaderSplit[i+3])) {
                            yvelocity = 2f;
                            doyVelocity = false;
                        }

                    }

                    // about to hit wall check
                    if (Integer.parseInt(levelreaderSplit[i+1]) + Integer.parseInt(levelreaderSplit[i+3]) >= ycoordinate - 12 && Integer.parseInt(levelreaderSplit[i+1]) <= ycoordinate - 12) {
                        xvelocity = Math.min(25, Math.max(-25, xvelocity));

                        // if going through wall bounce back
                        if (xcoordinate <= Integer.parseInt(levelreaderSplit[i]) && xcoordinate + xvelocity >= Integer.parseInt(levelreaderSplit[i])) {
                            xcoordinate = (int) Integer.parseInt(levelreaderSplit[i]);
                            if (charecterheight > Integer.parseInt(levelreaderSplit[i+3])) yvelocity += 2f;
                            xvelocity *= -1.3f;
                        }

                        // check other side of wall
                        else if (xcoordinate >= Integer.parseInt(levelreaderSplit[i]) + Integer.parseInt(levelreaderSplit[i+2]) && xcoordinate + xvelocity <= Integer.parseInt(levelreaderSplit[i]) + Integer.parseInt(levelreaderSplit[i+2])) {
                            xcoordinate = (int) (Integer.parseInt(levelreaderSplit[i]) + Integer.parseInt(levelreaderSplit[i+2]));
                            if (charecterheight > Integer.parseInt(levelreaderSplit[i+3])) yvelocity += 2f;
                            xvelocity *= -1.3f;
                        }
                    }
                }

            }

            xcoordinate += (int) xvelocity;
            if (doyVelocity) ycoordinate -= (int) yvelocity;


            timelast = System.currentTimeMillis();

            if (KeyLib.isKeyDown(KeyEvent.VK_D)) {
                if (xvelocity <= 0) {
                    if (sprinting) xvelocity = 5f;
                    else xvelocity += 2f;
                }

                if (!sprinting) {
                    if (xvelocity < 5) {
                        xvelocity += 0.5f;
                    }
                }
                else {
                    if (xvelocity < 10 || !onGround) {
                        xvelocity += 1f;
                    }
                }
            }

            else if (KeyLib.isKeyDown(KeyEvent.VK_A)) {
                if (xvelocity >= 0) {
                    if (sprinting) xvelocity = -5f;
                    else xvelocity -= 2f;
                }

                if (!sprinting) {
                    if (xvelocity > -5) {
                        xvelocity -= 0.5f;
                    }
                }
                else {
                    if (xvelocity > -10 || !onGround) {
                        xvelocity -= 1f;
                    }
                }
            }

            else {
                if (Velocity.xvelocity != 0f) {
                    if (Velocity.xvelocity < 0f) {
                        Velocity.xvelocity += 0.3f;
                    }
                    else {
                        Velocity.xvelocity -= 0.3f;
                    }
                }

            }


            if (onground != onGround && onGround) {
                yvelocity *= -0.3f;
            }

            // coyote time or smth
            if (onGround != onground && !onGround) {
                timesinceleftground = System.currentTimeMillis();
            }

            onground = onGround;

            if (KeyLib.keyPressed(KeyEvent.VK_SPACE)) {
                if (onGround || System.currentTimeMillis() - timesinceleftground < 50 && !onWall) {
                    timesinceleftground=0;
                    yvelocity = 7;
                }
            }


            // if not on ground decrease y velocity
            if (!onGround) {
                if (yvelocity > -10f) yvelocity -= 0.5f;
            }

            if (System.currentTimeMillis() - timesincespeedtoggle > 250 || !KeyLib.isKeyDown(KeyEvent.VK_SHIFT)) {
                sprinting = false;
            }

            if (KeyLib.isKeyDown(KeyEvent.VK_SHIFT) && (System.currentTimeMillis() - timesincespeedtoggle > 500 || onGround) && !sprintedInAir ) {
                sprinting = true;
                sprintedInAir = true;
                timesincespeedtoggle = System.currentTimeMillis();
            }

            if (KeyLib.isKeyDown(KeyEvent.VK_S) && !sprinting && !onGround) {
                yvelocity -= 1f;
            }

            if (KeyLib.isKeyDown(KeyEvent.VK_W) && sprinting) {
                yvelocity = 0.1f;
            }

            if (KeyLib.isKeyDown(KeyEvent.VK_S) && sprinting && !onGround) {
                yvelocity -= 2.5f;
            }

        }

    }



}