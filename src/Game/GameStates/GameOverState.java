package Game.GameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import Main.Handler;
import Resources.Images;

public class GameOverState extends State{

	public GameOverState(Handler handler) {
		super(handler);
	}

	@Override
	public void tick() {
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
			 State.setState(handler.getGame().menuState);
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.setFont(new Font ("ComicSans", Font.BOLD, 28));
		g.drawString("Press 'Escape' to see the Menu", 95, 450);
		g.drawImage(Images.GameOver,0,0,600,600,null);
		
	}

}
