package Game.Entities.Static;

import java.util.Random;

import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

    private Handler handler;

    public int xCoord;
    public int yCoord;
    public boolean notRotten;

    public Apple(Handler handler,int x, int y){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
        notRotten = true;
    }
    public boolean isGood() {
		return notRotten;
	    	
    }
    public void setisGood(boolean isRotten) {
    	this.notRotten = isRotten;
    }

}
