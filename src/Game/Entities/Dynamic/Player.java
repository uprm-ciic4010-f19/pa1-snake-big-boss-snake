package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.GameStates.State;
import Game.Entities.Static.Apple;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

    public int lenght;
    public boolean justAte;
    public boolean notRotten;
    private Handler handler;

    public int xCoord;
    public int yCoord;
    public int Score = 0;
    public int CurrScore = -1;
    public int stepCount = 0;
    public int speed = 2;
    public int LastDigitID = 0;

    public int moveCounter;

    public String direction;//is your first name one?

    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        speed = 2;
        direction= "Right";
        justAte = false;
        notRotten = true;
        lenght= 1;

    }

    public void tick(){
        moveCounter++;
        if(moveCounter >= 15) {
            checkCollisionAndMove();
            moveCounter = 0;
            
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)){
        	if (direction == "Down") {
        		moveCounter = 0;
        	}else
        		direction="Up";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)){
        	if (direction == "Up") {
        		moveCounter = 0;
        	}else
        		direction="Down";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)){
        	if (direction == "Right") {
        		moveCounter = 0;
        	}else
        		direction="Left";
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)){
        	if (direction == "Left") {
        		moveCounter = 0;
        	}else
        		direction="Right";
        }
        
        
        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {
        	handler.getWorld().body.add(new Tail(this.xCoord, this.yCoord, handler));
        }
        moveCounter += speed;
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)){
      	  checkCollisionAndMove();
          speed++;
      }
        
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)){
      	  checkCollisionAndMove();
          speed--;
      }
        
        //Game Over when snake collides with itself
        for (int e = 1; e < handler.getWorld().body.size(); e++) {
        	if (!handler.getWorld().body.isEmpty()) {
        		if ( (handler.getWorld().body.getFirst().x == handler.getWorld().body.get(e).x) &&
        				(handler.getWorld().body.getFirst().y == handler.getWorld().body.get(e).y)) {
        			 State.setState(handler.getGame().gameOverState);
        		}
        	}
        }
        if (stepCount >= 70 && stepCount < 200) {
        	handler.getWorld().apple.setisGood(false);
        }
        if (stepCount >= 200) {
        	handler.getWorld().apple.setisGood(notRotten);;
        }
    }

    public void checkCollisionAndMove(){
        handler.getWorld().playerLocation[xCoord][yCoord]=false;
        int x = xCoord;
        int y = yCoord;
        switch (direction){
            case "Left":
                if(xCoord==0){
                	xCoord = handler.getWorld().GridWidthHeightPixelCount-1;
                }else{
                    xCoord--;
                }
                break;
            case "Right":
                if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    xCoord = 0;
                }else{
                    xCoord++;
                }
                break;
            case "Up":
                if(yCoord==0){
                	yCoord = handler.getWorld().GridWidthHeightPixelCount-1;
                }else{
                    yCoord--;
                }
                break;
            case "Down":
                if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    yCoord = 0;
                }else{
                    yCoord++;
                }
                break;
        }
        handler.getWorld().playerLocation[xCoord][yCoord]=true;


        if(handler.getWorld().appleLocation[xCoord][yCoord]){
            Eat();
            if ((!handler.getWorld().body.isEmpty()) && (handler.getWorld().apple.isGood() == false)) {
    			handler.getWorld().body.removeLast();
    			kill();
            }
            LastDigitID++;
            if (handler.getWorld().apple.isGood() == true) {
            	CurrScore += Score;
            	speed = LastDigitID + 1;
            }else {
            	CurrScore -= Score;
            	LastDigitID--;
            	if (!handler.getWorld().body.isEmpty()) {
            		speed = LastDigitID - 1;
            	}
            }
            stepCount = 0;
        }

        if(!handler.getWorld().body.isEmpty()) {
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast();
            handler.getWorld().body.addFirst(new Tail(x, y,handler));
        }
        stepCount++;
        System.out.println(stepCount); 
    }

    public void render(Graphics g,Boolean[][] playeLocation){
        Random r = new Random();
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
            	Color multiColor = new Color (r.nextInt(176) , r.nextInt(176) , r.nextInt(176)); //This is use to make the Snake MultiColor
            	g.setColor(multiColor);
            	
            	//Counter of the Score everytime the snake eats 
            	if(handler.getWorld().appleLocation[i][j]){
            		g.setFont(new Font("",1,20));
            		Score= (int) Math.sqrt(2* CurrScore + 1);
            		g.drawString("Score: "+ Score ,10,30);
            		g.setColor(Color.red); //changed color here so only apple is red
            		if (handler.getWorld().apple.isGood() == false) {
            			g.setColor(Color.YELLOW);
            			Score--;
            		}
            		Score++;
            		
            		
            	
            	}
  
                if(playeLocation[i][j]||handler.getWorld().appleLocation[i][j]){
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }
            }
        }

    }

    public void Eat(){
        lenght++;
        Tail tail= null;
        handler.getWorld().appleLocation[xCoord][yCoord]=false;
        handler.getWorld().appleOnBoard=false;
        switch (direction){
            case "Left":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail = new Tail(this.xCoord+1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail = new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail =new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

                        }
                    }

                }
                break;
            case "Right":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=0){
                        tail=new Tail(this.xCoord-1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail=new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                        }
                    }

                }
                break;
            case "Up":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(this.xCoord,this.yCoord+1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
            case "Down":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=0){
                        tail=(new Tail(this.xCoord,this.yCoord-1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        } System.out.println("Tu biscochito");
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
        }
        if (handler.getWorld().apple.isGood() == true) {
        	 handler.getWorld().body.addLast(tail);
             handler.getWorld().playerLocation[tail.x][tail.y] = true;
             kill();
        }
    }

    public void kill(){
        lenght = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

                handler.getWorld().playerLocation[i][j]=false;
            }
        }
    }

    public boolean isJustAte() {
        return justAte;
    }

    public void setJustAte(boolean justAte) {
        this.justAte = justAte;
    }
}
